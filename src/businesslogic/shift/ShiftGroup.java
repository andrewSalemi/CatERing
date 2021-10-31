package businesslogic.shift;

import java.util.ArrayList;
import java.util.Date;

public class ShiftGroup {

  private int id;
  private Date date;
  private ArrayList<Shift> groupItems;

  public ShiftGroup() {
    this.groupItems = new ArrayList<>();
  }

  public ShiftGroup(ArrayList<Shift> groupItems) {
    this.groupItems = new ArrayList<>(groupItems);
  }

  public ArrayList<Shift> getGroupItems() {
    return this.groupItems;
  }

}
