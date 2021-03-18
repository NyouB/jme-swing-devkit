package fr.exratio.jme.devkit.service;

import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.MainPage;
import fr.exratio.jme.devkit.forms.MainPage.Zone;
import fr.exratio.jme.devkit.tool.ToolView;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

;

/**
 * A simple service to set window positions and sizes from saved settings.
 */
public class ToolLocationService implements Service {

  private final JFrame mainFrame;
  private MainPage mainPage;
  private final long threadId;
  private Map<String, ToolView> tools = new HashMap<>();
  private JMenu toolViewMenu = new JMenu("View");

  public ToolLocationService(JFrame mainFrame, MainPage mainPage) {
    this.mainFrame = mainFrame;
    threadId = Thread.currentThread().getId();
    this.mainPage = mainPage;
    ServiceManager.getService(MenuService.class).addPrimaryMenu(toolViewMenu);
  }

  public void registerTool(ToolView toolView) {
    if (tools.get(toolView.getId()) == null) {
      tools.put(toolView.getId(), toolView);
      toolView.changeViewMode(toolView.getViewMode());
      addViewMenuEntry(toolView);
    }
  }

  public void addViewMenuEntry(ToolView toolView) {
    JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(toolView.getTitle(),
        toolView.getIcon());
    jRadioButtonMenuItem.addItemListener(
        e -> toolView.setDisplayed(e.getStateChange() == ItemEvent.SELECTED));
    toolViewMenu.add(jRadioButtonMenuItem);
    jRadioButtonMenuItem.setSelected(toolView.isDisplayed());
  }

  /**
   * Returns the primary JFrame of the devkit (the window with the scene in it).
   *
   * @return the primary JFrame of the devkit.
   */
  public JFrame getMainFrame() {
    return mainFrame;
  }

  /**
   * Returns an instance of the window matching the exact specified text, or null if none exists.
   *
   * @param title the exact string text title of the window.
   * @return an instance of the window matching the exact specified text, or null if none exists.
   */
  public Window getWindow(String title) {
    return Arrays.stream(mainFrame.getOwnedWindows())
        .filter(w -> w instanceof Dialog)
        .filter(w -> ((Dialog) w).getTitle().equals(title))
        .findFirst()
        .orElse(null);
  }

  /**
   * Sets the position of a window from saved data.
   *
   * @param window the window to position.
   * @param name the name of the window.
   */
  public void positionWindowFromSavedPosition(Window window, String name) {
    Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(name);
    if (location != null) {
      window.setLocation(location);
    }
  }

  /**
   * Resizes the window from saved data.
   *
   * @param window the window to resize.
   * @param name the name of the window.
   */
  public void sizeWindowFromSavedSize(Window window, String name) {
    Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(name);
    if (dimension != null) {
      window.setSize(dimension);
    }
  }

  public JDialog createDialog(Frame parent, JComponent content, String title, boolean saveLocation,
      boolean saveSize) {

    JDialog dialog = new JDialog(parent, title);

    dialog.setContentPane(content);
    dialog.pack();

    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);


  /*  if (saveLocation) {
      dialog.addComponentListener(new WindowLocationSaver(title));
    }

    if (saveSize) {
      dialog.addComponentListener(new WindowSizeSaver(title));
    }

    if (saveLocation) {
      ServiceManager.getService(ToolLocationService.class).positionWindowFromSavedPosition(dialog, title);
    }

    if (saveSize) {
      ServiceManager.getService(ToolLocationService.class).sizeWindowFromSavedSize(dialog, title);
    }*/

    return dialog;

  }

  public MainPage getMainPage() {
    return mainPage;
  }

  public void closeToolDialog(ToolView tool) {
    if (fr.exratio.jme.devkit.tool.Window.class.isInstance(tool.getParent())) {
      JDialog dialog = (JDialog) SwingUtilities.getRoot(tool);
      dialog.dispose();
    }
  }

  public Window wrapInWindow(ToolView tool) {
    Window window = getWindow(tool.getId());
    if (window == null) {
      window = createDialog(mainFrame,
          tool,
          tool.getTitle(),
          true, true);
      window.setMinimumSize(new Dimension(400, 500));
      window.setVisible(true);
    }
//    DevKitConfig.getInstance().getSdkConfig().registerWindow(tool.getId(), tool);
    return window;
  }

  public void moveZone(ToolView toolView, Zone newZone) {
    mainPage.removeTab(toolView);
    mainPage.addTab(toolView.getTitle(), toolView, toolView.getIcon(), newZone);
  }

  public void hide(ToolView toolView){
    closeToolDialog(toolView);
    mainPage.removeTab(toolView);
  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {

  }

}
