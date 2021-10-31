import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.task.SummarySheet;
import businesslogic.task.TaskException;

public class TestCatERingKitchen1b {

  public static void main(String[] args) {
    try {
      System.out.println("TEST RESET SUMMARY");


      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nSUMMARY RESET PHASE");
      SummarySheet summary = CatERing.getInstance().getTaskManager().getSummaries().get(0);
      System.out.println("\nSUMMARY BEFORE RESET");
      System.out.println(summary);
      CatERing.getInstance().getTaskManager().resetSummarySheet(summary);
      System.out.println("\nSUMMARY AFTER RESET");
      System.out.println(summary);
    } catch (UseCaseLogicException | TaskException e) {
      System.err.println("Use case logic exception occurred due to: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
