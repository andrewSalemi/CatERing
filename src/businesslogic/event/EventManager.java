package businesslogic.event;

import businesslogic.menu.Menu;
import javafx.collections.ObservableList;

public class EventManager {

  public ObservableList<EventInfo> getEventInfo() {
    return EventInfo.loadAllEventInfo();
  }

  public Menu getServiceMenu(ServiceInfo service) {
    return ServiceInfo.getServiceMenu(service);
  }

}
