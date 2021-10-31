import businesslogic.CatERing;
import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.task.SummarySheet;

public class TestCatERingKitchen {

  public static void main(String[] args) {
    try {
      System.out.println("TEST CREAZIONE SUMMARY");

      System.out.println("\nLOGIN PHASE");
      CatERing.getInstance().getUserManager().fakeLogin("Lidia");
      System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

      System.out.println("\nSUMMARY CREATION PHASE");
      ServiceInfo service = CatERing.getInstance().getEventManager().getEventInfo().get(0).getServices().get(0);
      System.out.println(service);
      System.out.println(service.getMenu().getSections());
      SummarySheet s = CatERing.getInstance().getTaskManager().createSummarySheet(service);
      System.out.println(s);
    } catch (UseCaseLogicException e) {
      System.err.println("Use case logic exception: " + e.getMessage());
      e.printStackTrace();
    }
  }

}
