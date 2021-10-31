package businesslogic.shift;

import java.util.ArrayList;

public class ShiftManager {

  private ShiftTable shiftTable;
  private ArrayList<ShiftEventReceiver> eventReceivers;

  public ShiftManager() {
    eventReceivers = new ArrayList<>();
    shiftTable = ShiftTable.getInstance();
  }

  public ArrayList<Shift> getShift() {
    return shiftTable.getAllShifts();
  }

}
