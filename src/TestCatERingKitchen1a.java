import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.task.SummarySheet;

public class TestCatERingKitchen1a {

  public static void main(String[] args) {
    try {
      System.out.println("TEST CREAZIONE SUMMARY");

      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nSUMMARY MODIFICATION PHASE");
      System.out.println("Current summary should be null\nIs currentSummary null? " + (CatERing.getInstance().getTaskManager().getCurrentSummary() == null ? "Yes" : "No"));
      System.out.println("Setting summary to modify");
      SummarySheet summarySheet = CatERing.getInstance().getTaskManager().getSummaries().get(0);
      CatERing.getInstance().getTaskManager().modifySummarySheet(summarySheet);
      System.out.println("Current summary now is:\n" + CatERing.getInstance().getTaskManager().getCurrentSummary());
    } catch (UseCaseLogicException e) {
      System.err.println("Use case logic exception occurred due to: " + e.getMessage());
      e.printStackTrace();
    }

  }

}
