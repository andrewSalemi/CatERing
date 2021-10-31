import businesslogic.CatERing;

public class TestCatERingKitchen2 {

  public static void main(String[] args) {
    System.out.println("TEST GET SERVICE MENU");


    System.out.println("\nLOGIN PHASE");
    CatERing.getInstance().getUserManager().fakeLogin("Lidia");
    System.out.println(CatERing.getInstance().getUserManager().getCurrentUser());

    System.out.println("\nSUMMARY RETRIVING PHASE");
    System.out.println(CatERing.getInstance().getEventManager().getServiceMenu(CatERing.getInstance().getEventManager().getEventInfo().get(0).getServices().get(0)));
  }

}
