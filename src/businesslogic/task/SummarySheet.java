package businesslogic.task;

import businesslogic.UseCaseLogicException;
import businesslogic.event.ServiceInfo;
import businesslogic.menu.Menu;
import businesslogic.menu.MenuItem;
import businesslogic.menu.Section;
import businesslogic.recipe.Recipe;
import businesslogic.shift.Shift;
import businesslogic.user.User;
import persistence.BatchUpdateHandler;
import persistence.PersistenceManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SummarySheet {

  private static Map<Integer, SummarySheet> loadedSummaries = new HashMap<>();

  private int id;
  private int serviceId;
  private User owner;
  private List<Task> tasks;

  public SummarySheet() {

  }

  public SummarySheet(User owner, ServiceInfo service) throws UseCaseLogicException {

    this.owner = owner;
    this.serviceId = service.getId();
    this.tasks = new ArrayList<>();
    Menu serviceMenu = service.getMenu();

    if (serviceMenu == null) {
      throw new UseCaseLogicException();
    }

    for (Section section : serviceMenu.getSections()) {
      for (MenuItem menuItem : section.getItems()) {
        Task task = new Task(menuItem.getItemRecipe(), false);
        tasks.add(task);

        if (menuItem.getItemRecipe().getRecipePreparations().size() > 0) {
          for (Recipe preparation : menuItem.getItemRecipe().getRecipePreparations()) {
            Task preparationTask = new Task(preparation, true);
            tasks.add(preparationTask);
          }
        }
      }

      for (MenuItem menuItem : serviceMenu.getFreeItems()) {
        Task task = new Task(menuItem.getItemRecipe(), false);
        tasks.add(task);

        if (menuItem.getItemRecipe().getRecipePreparations().size() > 0) {
          for (Recipe preparation : menuItem.getItemRecipe().getRecipePreparations()) {
            Task preparationTask = new Task(preparation, true);
            tasks.add(preparationTask);
          }
        }
      }
    }
  }

  public static SummarySheet loadSummaryById(int serviceId) {
    if (loadedSummaries.containsKey(serviceId)) return loadedSummaries.get(serviceId);

    SummarySheet load = new SummarySheet();
    final int[] summaryOwner = new int[1];

    String summariesQuery = "SELECT * FROM catering.Summaries WHERE id= '" + serviceId + "'";
    PersistenceManager.executeQuery(summariesQuery, rs -> {
      load.id = rs.getInt("id");
      summaryOwner[0] = rs.getInt("owner");
    });
    load.owner = User.loadUserById(summaryOwner[0]);
    load.serviceId = serviceId;
    load.tasks = Task.loadSummaryTasks(load.id);

    return load;
  }


  public static void saveNewSummary(SummarySheet summary) {
    String summaryInsert = "INSERT INTO catering.Summaries (service_id, owner) VALUES (?, ?)";
    PersistenceManager.executeBatchUpdate(summaryInsert, 1, new BatchUpdateHandler() {
      @Override
      public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
        ps.setInt(1, summary.serviceId);
        ps.setInt(2, summary.owner.getId());
      }

      @Override
      public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
        summary.id = rs.getInt(1);
      }
    });

    Task.saveAllNewTask(summary.id, summary.tasks);

    loadedSummaries.put(summary.id, summary);
  }


  public static void saveSummaryReset(SummarySheet summary) {
    for (Task task : summary.tasks) {
      Task.updateTasksReset(summary.id, task);
    }
  }

  public static List<SummarySheet> loadAllSummaries() {
    ArrayList<SummarySheet> all = new ArrayList<>();
    String summariesQuery = "SELECT * FROM Summaries";
    PersistenceManager.executeQuery(summariesQuery, rs -> {
      SummarySheet s = new SummarySheet();
      s.id = rs.getInt("id");
      int ownerId = rs.getInt("owner");
      s.owner = User.loadUserById(ownerId);
      all.add(s);
    });

    for (SummarySheet s : all) {
      s.tasks = Task.loadSummaryTasks(s.id);
    }

    return all;
  }

  public static void saveSummaryOrder(SummarySheet summary) {
    String taskUpdate = "UPDATTE catering .Tasks " +
            " SET position = ? " +
            " WHERE id = ?";
    PersistenceManager.executeBatchUpdate(taskUpdate, summary.tasks.size(), new BatchUpdateHandler() {
      @Override
      public void handleBatchItem(PreparedStatement ps, int batchCount) throws SQLException {
        ps.setInt(1, batchCount);
        ps.setInt(2, summary.tasks.get(batchCount).getId());
      }

      @Override
      public void handleGeneratedIds(ResultSet rs, int count) throws SQLException {
        // no id generated
      }
    });
  }


  public void resetAllTask() {
    for (Task task : tasks) {
      task.resetTask();
    }
  }

  public User getOwner() {
    return owner;
  }

  @Override
  public String toString() {
    StringBuilder prettyPrint = new StringBuilder("SummarySheet: owner " + owner.getUserName() + ", service " + serviceId + "\n");
    if (tasks.size() > 0) {
      prettyPrint.append("\tList of Tasks");
    }
    for (Task task : tasks) {
      prettyPrint.append("\n").append(task.toString());
    }
    return prettyPrint.toString();
  }

  public boolean isOwner(User user) {
    return this.owner.getId() == user.getId();
  }


  public Task addTask(Recipe kitchenTask, boolean isPreparation) {
    Task task = new Task(kitchenTask, isPreparation);
    this.tasks.add(task);
    return task;
  }

  public boolean hasTask(Task task) {
    return tasks.contains(task);
  }


  public int getId() {
    return id;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public Task assignTask(Task task, Shift turn, User cook, float time, double portions) {
    for (Task t : tasks) {
      if (t.getId() == task.getId()) {
        t.setTurn(turn);
        t.setCook(cook);
        t.setTime(time);
        t.setPortions(portions);
        return t;
      }
    }
    // Unreachable statement
    return null;
  }

  public Task modifyTaskPortions(Task task, double portions) {
    for (Task t : tasks) {
      if (t.getId() == task.getId()) {
        t.setPortions(portions);
        return t;
      }
    }
    // Unreachable statement
    return null;
  }

  public Task modifyTimePortions(Task task, float time) {
    for (Task t : tasks) {
      if (t.getId() == task.getId()) {
        t.setTime(time);
        return t;
      }
    }
    // Unreachable statement
    return null;
  }

  public Task removeTask(Task task) {
    Task deleted = new Task();
    for (Task t : tasks) {
      if (t.getId() == task.getId()) {
        deleted = t;
        break;
      }
    }
    tasks.remove(deleted);
    return deleted;
  }

  public Task removeTaskCook(Task task) {
    for (Task t : tasks) {
      if (t.getId() == task.getId()) {
        t.setCook(null);
        return t;
      }
    }
    // Unreachable statement
    return null;
  }

  public Task completeTask(Task task, boolean completed) {
    for (Task t : tasks) {
      if (t.getId() == task.getId()) {
        t.setCompleted(completed);
        return t;
      }
    }
    // Unreachable statement
    return null;
  }

  public void moveTask(Task task, int position) {
    tasks.remove(task);
    tasks.add(position, task);
  }

  public int getTaskPosition(Task task) {
    return this.tasks.indexOf(task);
  }

}
