import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.recipe.Recipe;
import businesslogic.task.SummarySheet;
import businesslogic.task.TaskException;

public class TestCatERingKitchen3 {

  public static void main(String[] args) {
    try {
      System.out.println("TEST ADDING NEW TASK");

      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nTASK ADDING PHASE");
      SummarySheet summary = CatERing.getInstance().getTaskManager().getSummaries().get(0);
      CatERing.getInstance().getTaskManager().setCurrentSummary(summary);
      Recipe toAdd = CatERing.getInstance().getRecipeManager().getRecipes().get(0);
      System.out.println("SUMMARY BEFORE ADD\n" + summary);
      CatERing.getInstance().getTaskManager().addKitchenTask(toAdd, true);
      System.out.println("SUMMARY AFTER ADD\n" + summary);
    } catch (TaskException | UseCaseLogicException e) {
      e.printStackTrace();
    }
  }

}
