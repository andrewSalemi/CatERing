package businesslogic.shift;

import java.util.ArrayList;

public class ShiftTable {

  private static ShiftTable singleInstance;

  private ArrayList<ShiftGroup> shiftGroups;
  private ArrayList<Shift> shifts;

  private ShiftTable() {
    shiftGroups = new ArrayList<>();
    shifts = new ArrayList<>();
  }

  public static ShiftTable getInstance() {
    if (singleInstance != null) {
      singleInstance = new ShiftTable();
    }
    return singleInstance;
  }


  public ArrayList<Shift> getAllShifts() {
    ArrayList<Shift> result = new ArrayList<>(shifts);

    for (ShiftGroup shiftGroup : shiftGroups) {
      ArrayList<Shift> groupItems = shiftGroup.getGroupItems();
      result.addAll(groupItems);
    }

    return result;
  }

}
