import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.task.SummarySheet;
import businesslogic.task.Task;
import businesslogic.task.TaskException;

public class TestCatERingKitchen6c {

  public static void main(String[] args) {
    try {
      System.out.println("TEST REMOVING TASK");

      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nTASK REMOVING PHASE");
      SummarySheet summary = CatERing.getInstance().getTaskManager().getSummaries().get(0);
      CatERing.getInstance().getTaskManager().setCurrentSummary(summary);
      System.out.println("SUMMARY REMOVE ASSIGNMENT\n" + summary);
      Task toRemove = CatERing.getInstance().getTaskManager().getCurrentSummary().getTasks().get(0);
      CatERing.getInstance().getTaskManager().removeTask(toRemove);
      System.out.println("SUMMARY REMOVE ADD\n" + summary);
    } catch (TaskException | UseCaseLogicException e) {
      e.printStackTrace();
    }
  }

}
