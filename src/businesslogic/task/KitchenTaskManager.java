package businesslogic.task;

import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.user.User;

import java.util.ArrayList;
import java.util.List;

public class KitchenTaskManager {

  private SummarySheet currentSummary;
  private List<KitchenTaskEventReceiver> eventReceivers;


  public KitchenTaskManager() {
    eventReceivers = new ArrayList<>();
  }

  public SummarySheet createSummarySheet(ServiceInfo service) throws UseCaseLogicException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    SummarySheet summary = new SummarySheet(user, service);

    service.setServiceSummary(summary);
    this.setCurrentSummary(summary);
    this.notifySummaryCreated(summary);

    return summary;

  }

  public void modifySummarySheet(SummarySheet summary) throws UseCaseLogicException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (user.isChef() && user.getId() == summary.getOwner().getId()) {
      this.currentSummary = summary;
    }
  }

  public void resetSummarySheet(SummarySheet summary) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!summary.isOwner(user)) {
      throw new TaskException();
    }

    summary.resetAllTask();

    this.currentSummary = summary;
    notifySummaryReset(summary);
  }

  public void addKitchenTask(Recipe kitchenTask, boolean isPreparation) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!this.currentSummary.isOwner(user)) {
      throw new TaskException();
    }

    Task addedTask = this.currentSummary.addTask(kitchenTask, isPreparation);

    this.notifyTaskAdded(addedTask);
  }

  public void removeTask(Task task) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!currentSummary.isOwner(user) ||
            task == null ||
            !currentSummary.hasTask(task) ||
            task.isCompleted()) {
      throw new TaskException();
    }

    Task deletedTask = this.currentSummary.removeTask(task);

    this.notifyTaskDeleted(deletedTask);
  }


  public void assignTask(Task task, Shift turn, User cook, float time, double portions) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (currentSummary != null &&
            (task != null && currentSummary.hasTask(task) &&
                    !task.isCompleted() ||
                    (turn != null && !turn.isComplete()))) {
      Task assignedTask = currentSummary.assignTask(task, turn, cook, time, portions);

      this.notifyTaskAssigned(assignedTask);


    } else {
      throw new TaskException();
    }
  }

  public void modifyPortions(Task task, double portions) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!currentSummary.isOwner(user) ||
            task == null ||
            !currentSummary.hasTask(task) ||
            task.isCompleted()) {
      throw new TaskException();
    }

    Task modified = currentSummary.modifyTaskPortions(task, portions);

    this.notifyTaskModified(modified);

  }

  public void modifyTime(Task task, float time) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!currentSummary.isOwner(user) ||
            task == null ||
            !currentSummary.hasTask(task) ||
            task.isCompleted()) {
      throw new TaskException();
    }

    Task modified = currentSummary.modifyTimePortions(task, time);

    this.notifyTaskModified(modified);
  }

  public void removeCookFromTask(Task task) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!currentSummary.isOwner(user) ||
            task == null ||
            !currentSummary.hasTask(task) ||
            task.isCompleted()) {
      throw new TaskException();
    }

    Task updatedTask = currentSummary.removeTaskCook(task);

    this.notifyTaskModified(updatedTask);
  }

  public void setTaskCompleted(Task task, boolean completed) throws UseCaseLogicException, TaskException {
    User user = CatERing.getInstance().getUserManager().getCurrentUser();

    if (!user.isChef()) {
      throw new UseCaseLogicException();
    }

    if (!currentSummary.isOwner(user) ||
            !currentSummary.hasTask(task)) {
      throw new TaskException();
    }

    Task changedTask = currentSummary.completeTask(task, completed);

    this.notifyTaskModified(changedTask);


  }

  public void moveTask(Task task, int position) throws UseCaseLogicException {
    if (currentSummary == null || !currentSummary.hasTask(task) || currentSummary.getTaskPosition(task) < 0) {
      throw new UseCaseLogicException();
    }

    currentSummary.moveTask(task, position);

    this.notifyTaskRearranged(currentSummary);
  }


  public void setCurrentSummary(SummarySheet summary) {
    this.currentSummary = summary;
  }

  // Event receivers methods

  private void notifySummaryCreated(SummarySheet summary) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateSummaryCreated(summary);
    }
  }

  private void notifySummaryReset(SummarySheet summary) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateSummaryReset(summary);
    }
  }

  private void notifyTaskAdded(Task task) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateTaskAddedTask(currentSummary.getId(), currentSummary.getTasks(), task);
    }
  }

  private void notifyTaskAssigned(Task assignedTask) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateTaskAssigned(currentSummary.getId(), assignedTask);
    }
  }

  private void notifyTaskModified(Task modifiedTask) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateTaskModified(currentSummary.getId(), modifiedTask);
    }
  }

  private void notifyTaskDeleted(Task deletedTask) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateTaskDeleted(currentSummary.getId(), deletedTask);
    }
  }

  private void notifyTaskRearranged(SummarySheet summary) {
    for (KitchenTaskEventReceiver er : eventReceivers) {
      er.updateTaskRearranged(summary);
    }
  }

  public void addEventReceiver(KitchenTaskEventReceiver er) {
    this.eventReceivers.add(er);
  }

  public void removeEventReceiver(KitchenTaskEventReceiver er) {
    this.eventReceivers.remove(er);
  }

  public SummarySheet getCurrentSummary() {
    return currentSummary;
  }


  public List<SummarySheet> getSummaries() {
    return SummarySheet.loadAllSummaries();
  }

}
