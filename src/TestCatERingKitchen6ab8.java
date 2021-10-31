import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.task.SummarySheet;
import businesslogic.task.Task;
import businesslogic.task.TaskException;

public class TestCatERingKitchen6ab8 {

  public static void main(String[] args) {
    try {
      System.out.println("TEST MODIFYING TASK");

      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nTASK MODIFY PHASE");
      SummarySheet summary = CatERing.getInstance().getTaskManager().getSummaries().get(0);
      CatERing.getInstance().getTaskManager().setCurrentSummary(summary);
      System.out.println("SUMMARY BEFORE MODIFY\n" + summary);
      Task toModify = CatERing.getInstance().getTaskManager().getCurrentSummary().getTasks().get(0);
      System.out.println("MODIFY PORTIONS");
      CatERing.getInstance().getTaskManager().modifyPortions(toModify, 34);
      System.out.println("MODIFY TIME");
      CatERing.getInstance().getTaskManager().modifyTime(toModify, 130);
      System.out.println("MODIFY COMPLETE");
      CatERing.getInstance().getTaskManager().setTaskCompleted(toModify, true);
      System.out.println("SUMMARY AFTER MODIFY\n" + summary);
    } catch (TaskException | UseCaseLogicException e) {
      e.printStackTrace();
    }
  }

}
