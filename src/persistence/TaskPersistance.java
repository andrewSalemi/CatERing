package persistence;

import businesslogic.task.KitchenTaskEventReceiver;
import businesslogic.task.SummarySheet;
import businesslogic.task.Task;

import java.util.List;

public class TaskPersistance implements KitchenTaskEventReceiver {

  @Override
  public void updateSummaryCreated(SummarySheet summary) {
    SummarySheet.saveNewSummary(summary);
  }

  @Override
  public void updateSummaryReset(SummarySheet summary) {
    SummarySheet.saveSummaryReset(summary);
  }

  @Override
  public void updateTaskAddedTask(int summaryId, List<Task> tasks, Task addedTask) {
    Task.saveTaskAdded(summaryId, tasks, addedTask);
  }

  @Override
  public void updateTaskAssigned(int summaryId, Task assignedTask) {
    Task.saveAssignedTask(summaryId, assignedTask);
  }

  @Override
  public void updateTaskModified(int summaryId, Task modifiedTask) {
    Task.saveModifiedTask(summaryId, modifiedTask);
  }

  @Override
  public void updateTaskDeleted(int summaryId, Task deletedTask) {
    Task.saveDeletedTask(summaryId, deletedTask);
  }

  @Override
  public void updateTaskRearranged(SummarySheet summary) {
    SummarySheet.saveSummaryOrder(summary);
  }


}
