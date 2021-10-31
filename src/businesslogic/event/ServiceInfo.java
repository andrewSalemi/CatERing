package businesslogic.event;

import businesslogic.menu.Menu;
import businesslogic.task.SummarySheet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.PersistenceManager;
import persistence.ResultHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class ServiceInfo implements EventItemInfo {


  private int id;
  private String name;
  private SummarySheet serviceSummary;
  private Date date;
  private Time timeStart;
  private Time timeEnd;
  private int participants;
  private Menu menu;

  public ServiceInfo(String name) {
    this.name = name;
  }

  public static ObservableList<ServiceInfo> loadServiceInfoForEvent(int event_id) {
    ObservableList<ServiceInfo> result = FXCollections.observableArrayList();
    ArrayList<Integer> menuids = new ArrayList<>();

    String query = "SELECT id, name, service_date, time_start, time_end, expected_participants, approved_menu_id " +
            "FROM catering.Services WHERE event_id = " + event_id;
    PersistenceManager.executeQuery(query, new ResultHandler() {
      @Override
      public void handle(ResultSet rs) throws SQLException {
        String s = rs.getString("name");
        ServiceInfo serv = new ServiceInfo(s);
        serv.id = rs.getInt("id");
        serv.date = rs.getDate("service_date");
        serv.timeStart = rs.getTime("time_start");
        serv.timeEnd = rs.getTime("time_end");
        serv.participants = rs.getInt("expected_participants");
        menuids.add(rs.getInt("approved_menu_id"));
        result.add(serv);
      }
    });

    for (int i = 0; i < menuids.size(); i++) {
      ServiceInfo service = result.get(i);
      service.menu = Menu.loadMenuById(menuids.get(i));
    }

    for (int i = 0; i < result.size(); i++) {
      ServiceInfo service = result.get(i);
      service.serviceSummary = SummarySheet.loadSummaryById(service.id);
    }

    return result;
  }

  public static Menu getServiceMenu(ServiceInfo service) {
    return service.menu;
  }

  public void setServiceSummary(SummarySheet summary) {
    this.serviceSummary = summary;
  }

  public boolean hasServiceSummary() {
    return this.serviceSummary != null;
  }

  public Menu getMenu() {
    return this.menu;
  }

  public int getId() {
    return id;
  }

  // STATIC METHODS FOR PERSISTENCE

  public String toString() {
    return name + ": " + date + " (" + timeStart + "-" + timeEnd + "), " + participants + " pp.";
  }


}
