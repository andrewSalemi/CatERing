package businesslogic.task;

import java.util.List;

public interface KitchenTaskEventReceiver {

  public void updateSummaryCreated(SummarySheet summary);

  public void updateSummaryReset(SummarySheet summary);

  public void updateTaskAddedTask(int summaryId, List<Task> tasks, Task addedTask);

  public void updateTaskAssigned(int summaryId, Task assignedTask);

  public void updateTaskModified(int summaryId, Task modifiedTask);

  public void updateTaskDeleted(int summaryId, Task deletedTask);

  public void updateTaskRearranged(SummarySheet summary);

}
