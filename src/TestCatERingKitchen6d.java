import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.shift.Shift;
import businesslogic.task.SummarySheet;
import businesslogic.task.Task;
import businesslogic.task.TaskException;
import businesslogic.user.User;

public class TestCatERingKitchen6d {

  public static void main(String[] args) {
    try {
      System.out.println("TEST REMOVING COOK FROM TASK");

      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nTASK ASSIGNING PHASE");
      SummarySheet summary = CatERing.getInstance().getTaskManager().getSummaries().get(0);
      CatERing.getInstance().getTaskManager().setCurrentSummary(summary);
      System.out.println("SUMMARY BEFORE ASSIGNMENT\n" + summary);
      Task assigned = CatERing.getInstance().getTaskManager().getCurrentSummary().getTasks().get(0);
      Shift turn = Shift.fakeShift();
      User cook = User.loadUserById(5);
      CatERing.getInstance().getTaskManager().assignTask(assigned, turn, cook, 2, 4);
      System.out.println("SUMMARY AFTER ASSIGNMENT\n" + summary);
      System.out.println("\nREMOVING ASSIGNMENT PHASE");
      CatERing.getInstance().getTaskManager().removeCookFromTask(CatERing.getInstance().getTaskManager().getCurrentSummary().getTasks().get(0));
      System.out.println("SUMMARY AFTER ASSIGNMENT REMOVAL\n" + summary);
    } catch (TaskException | UseCaseLogicException e) {
      System.out.println(e.getMessage() + "\nCause:" + e.getCause());
      e.printStackTrace();
    }
  }

}
