package businesslogic.task;

import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Task {

  private int id;
  private Recipe kitchenTask;
  private boolean isPreparation;
  private float time;
  private double portions;
  private boolean completed;
  private User cook;
  private Shift turn;


  public Task() {

  }

  public Task(Recipe kitchenTask, boolean isPreparation) {
    this.kitchenTask = kitchenTask;
    this.isPreparation = isPreparation;
    this.time = 0;
    this.portions = 0;
    this.completed = false;
  }

  public static void saveAllNewTask(int serviceId, List<Task> tasks) {
    String taskInsert = "INSERT INTO catering.Tasks (summary_id, recipe_id, position ,preparation) VALUES (?, ?, ?, ?)";
    PersistenceManager.executeBatchUpdate(taskInsert, tasks.size(), new BatchUpdateHandler() {
      @Override
      public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
        ps.setInt(1, serviceId);
        ps.setInt(2, tasks.get(batchCount).kitchenTask.getId());
        ps.setInt(3, batchCount);
        ps.setBoolean(4, tasks.get(batchCount).isPreparation);
      }

      @Override
      public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
        tasks.get(count).id = rs.getInt(1);

      }
    });
  }

  public static List<Task> loadSummaryTasks(int summaryId) {
    ArrayList<Task> tasks = new ArrayList<>();
    ArrayList<Integer> cooksId = new ArrayList<>();
    ArrayList<Integer> shiftsId = new ArrayList<>();

    String taskQuery = "SELECT * FROM catering.Tasks WHERE summary_id= " + summaryId;
    PersistenceManager.executeQuery(taskQuery, rs -> {
      Task t = new Task(Recipe.loadRecipeById(rs.getInt("recipe_id")), rs.getBoolean("preparation"));
      t.id = rs.getInt("id");
      t.time = rs.getFloat("time");
      t.portions = rs.getDouble("portions");
      t.completed = rs.getBoolean("completed");
      cooksId.add(rs.getInt("cook_id"));
      shiftsId.add(rs.getInt("shift_id"));
      tasks.add(t);
    });

    for (int i = 0; i < cooksId.size(); i++) {
      Task task = tasks.get(i);
      task.cook = User.loadUserById(cooksId.get(i));
    }

    for (int i = 0; i < shiftsId.size(); i++) {
      Task task = tasks.get(i);
      task.turn = Shift.loadShiftById(shiftsId.get(i));
    }

    return tasks;
  }

  public static void saveTaskAdded(int summaryId, List<Task> tasks, Task addedTask) {

    String taskInsert = "INSERT INTO catering.Tasks (summary_id, recipe_id, position,time, portions, preparation, completed) VALUES " +
            "(?, ?, ?, ?, ?, ?, ?)";
    int[] result = PersistenceManager.executeBatchUpdate(taskInsert, 1, new BatchUpdateHandler() {
      @Override
      public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
        ps.setInt(1, summaryId);
        ps.setInt(2, addedTask.kitchenTask.getId());
        ps.setInt(3, tasks.size() - 1);
        ps.setFloat(4, addedTask.time);
        ps.setDouble(5, addedTask.portions);
        ps.setBoolean(6, addedTask.isPreparation);
        ps.setBoolean(7, addedTask.completed);
      }

      @Override
      public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
        tasks.get(tasks.size() - 1).id = rs.getInt(1);
      }
    });
  }

  public static void saveAssignedTask(int summaryId, Task assignedTask) {
    String taskUpdate = "UPDATE catering.Tasks" +
            " SET time = " + assignedTask.time +
            " ,portions = " + assignedTask.portions +
            " ,cook_id = " + assignedTask.cook.getId() +
            " ,shift_id = " + assignedTask.turn.getId() +
            " WHERE summary_id = " + summaryId +
            " AND id = " + assignedTask.id;

    PersistenceManager.executeUpdate(taskUpdate);
  }

  public static void saveModifiedTask(int summaryId, Task modifiedTask) {
    String taskUpdate = "UPDATE catering.Tasks " +
            " SET time = " + modifiedTask.time +
            " ,portions = " + modifiedTask.portions +
            " ,completed = " + modifiedTask.completed +
            " ,cook_id = " + (modifiedTask.cook != null ? modifiedTask.cook.getId() : null) +
            " ,shift_id = " + (modifiedTask.turn != null ? modifiedTask.turn.getId() : null) +
            " WHERE summary_id = " + summaryId +
            " AND id = " + modifiedTask.id;

    PersistenceManager.executeUpdate(taskUpdate);
  }

  public static void saveDeletedTask(int summaryId, Task deletedTask) {
    String taskDelete = "DELETE FROM catering.Tasks WHERE id = " + deletedTask.id + " AND summary_id = " + summaryId;
    PersistenceManager.executeUpdate(taskDelete);
  }


  public void resetTask() {
    this.time = 0;
    this.portions = 0;
    this.cook = null;
    this.turn = null;
    this.completed = false;
  }

  public static void updateTasksReset(int summaryId, Task task) {
    String updateStatement = "UPDATE Tasks" +
            " SET time = " + task.time +
            " ,portions = " + task.portions +
            " ,completed = " + task.completed +
            " ,cook_id = " + null +
            " ,shift_id = " + null +
            " WHERE summary_id = " + summaryId;
    PersistenceManager.executeUpdate(updateStatement);
  }

  public Recipe getKitchenTask() {
    return kitchenTask;
  }

  public void setKitchenTask(Recipe kitchenTask) {
    this.kitchenTask = kitchenTask;
  }

  public boolean isPreparation() {
    return isPreparation;
  }

  public void setPreparation(boolean preparation) {
    isPreparation = preparation;
  }

  public float getTime() {
    return time;
  }

  public void setTime(float time) {
    this.time = time;
  }

  public double getPortions() {
    return portions;
  }

  public void setPortions(double portions) {
    this.portions = portions;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public User getCook() {
    return cook;
  }

  public void setCook(User cook) {
    this.cook = cook;
  }

  public Shift getTurn() {
    return turn;
  }

  public void setTurn(Shift turn) {
    this.turn = turn;
  }

  @Override
  public String toString() {
    return "Task: id: " + id + " - recipe: " + kitchenTask + " - time: " + time +
            " - portions: " + portions + " - completed: " + completed +
            " - cook: " + (cook != null ? cook.getUserName() : "");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return id == task.id;
  }

  public int getId() {
    return id;
  }

}
