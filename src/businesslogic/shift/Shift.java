package businesslogic.shift;

import persistence.PersistenceManager;

import java.util.Date;

public class Shift {

  private int id;
  private Date date;
  private boolean completed;

  public Shift() {

  }

  public static Shift fakeShift() {
    Shift fakeShift = new Shift();
    fakeShift.id = 1;
    fakeShift.date = new Date();
    fakeShift.completed = false;
    return fakeShift;
  }

  public static Shift loadShiftById(Integer integer) {
    Shift shift = new Shift();

    String shiftQuery = "SELECT * FROM catering.Shifts";
    PersistenceManager.executeQuery(shiftQuery, rs -> {
      shift.id = rs.getInt("id");
      shift.date = rs.getDate("date");
      shift.completed = rs.getBoolean("completed");
    });

    return shift;
  }

  public boolean isComplete() {
    return this.completed;
  }

  public int getId() {
    return id;
  }

}
