package businesslogic;

import businesslogic.event.EventManager;
import businesslogic.menu.MenuManager;
import businesslogic.recipe.RecipeManager;
import businesslogic.shift.ShiftManager;
import businesslogic.task.KitchenTaskManager;
import businesslogic.user.UserManager;
import persistence.MenuPersistence;
import persistence.TaskPersistance;

public class CatERing {

  private static CatERing singleInstance;
  private MenuManager menuMgr;
  private RecipeManager recipeMgr;
  private UserManager userMgr;
  private EventManager eventMgr;
  private KitchenTaskManager taskMgr;
  private ShiftManager shiftMgr;
  private MenuPersistence menuPersistence;
  private TaskPersistance taskPersistance;

  private CatERing() {
    menuMgr = new MenuManager();
    recipeMgr = new RecipeManager();
    userMgr = new UserManager();
    eventMgr = new EventManager();
    taskMgr = new KitchenTaskManager();
    shiftMgr = new ShiftManager();
    menuPersistence = new MenuPersistence();
    taskPersistance = new TaskPersistance();
    menuMgr.addEventReceiver(menuPersistence);
    taskMgr.addEventReceiver(taskPersistance);
  }

  public static CatERing getInstance() {
    if (singleInstance == null) {
      singleInstance = new CatERing();
    }
    return singleInstance;
  }

  public MenuManager getMenuManager() {
    return menuMgr;
  }

  public RecipeManager getRecipeManager() {
    return recipeMgr;
  }

  public UserManager getUserManager() {
    return userMgr;
  }

  public EventManager getEventManager() {
    return eventMgr;
  }

  public KitchenTaskManager getTaskManager() {
    return taskMgr;
  }

  public ShiftManager getShiftManager() {
    return shiftMgr;
  }

}
