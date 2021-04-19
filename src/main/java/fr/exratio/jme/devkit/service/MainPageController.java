package fr.exratio.jme.devkit.service;

import com.jme3.system.awt.AwtPanel;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.RunAppStateWindow;
import fr.exratio.jme.devkit.main.MainPage;
import fr.exratio.jme.devkit.main.MainPage.Zone;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorTool;
import fr.exratio.jme.devkit.swing.MainMenu;
import fr.exratio.jme.devkit.tool.Tool;
import fr.exratio.jme.devkit.tool.ViewMode;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MainPageController {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainPageController.class);
  private final MainPage mainPage;
  private final EditorJmeApplication jmeEngineService;
  private final DevKitConfig devKitConfig;
  private final MainMenu mainMenu;
  private final SceneTreeService sceneTreeService;
  private Map<Zone, List<Tool>> toolsZone;
  private final Map<String, Tool> toolSet = new HashMap<>();
  private final JMenu toolViewMenu = new JMenu("View");
  private final MenuController menuController;


  public MainPageController(@Autowired EditorJmeApplication jmeEngineService,
      @Autowired DevKitConfig devKitConfig, @Autowired MainMenu mainMenu,
      @Autowired SceneTreeService sceneTreeService,  @Autowired MenuController menuController) {
    this.jmeEngineService = jmeEngineService;
    this.devKitConfig = devKitConfig;
    this.mainMenu = mainMenu;
    this.sceneTreeService = sceneTreeService;
    this.menuController = menuController;
    mainPage = new MainPage();

    // position and size the jme panel

    AwtPanel jmePanel = jmeEngineService.getAWTPanel();
    jmePanel.setSize(devKitConfig.getCameraDimension());
    ImageIcon icon = new ImageIcon("images/middle.gif");
    mainPage.addTabCenterPanel("Canvas", jmePanel, icon);

    initializeMap();

  }

  public MainPage getMainPage() {
    return mainPage;
  }

  private void initializeMap() {
    toolsZone = new HashMap<>();
    for (Zone zone : Zone.values()) {
      toolsZone.put(zone, new ArrayList<>());
    }
  }

  public static void closeToolDialog(Tool tool) {
    if (ViewMode.WINDOW == tool.getViewMode()) {
      Window window = (Window) SwingUtilities.getRoot(tool);
      window.dispose();
    }
  }

  public void attachTool(Tool tool) {
    if (!isToolRegistered(tool)) {
      registerTool(tool);
    }
    tool.setViewMode(tool.getViewMode());
    toolsZone.get(tool.getZone()).add(tool);
    tool.getZone().add(tool);

  }

  public void addViewMenuEntry(Tool toolView) {
    JRadioButtonMenuItem jRadioButtonMenuItem = new JRadioButtonMenuItem(toolView.getTitle(),
        toolView.getIcon());
    jRadioButtonMenuItem.addItemListener(
        e -> toolView.setDisplayed(e.getStateChange() == ItemEvent.SELECTED));
    toolViewMenu.add(jRadioButtonMenuItem);
    jRadioButtonMenuItem.setSelected(toolView.isDisplayed());
  }

  /**
   * Returns an instance of the window matching the exact specified text, or null if none exists.
   *
   * @param title the exact string text title of the window.
   * @return an instance of the window matching the exact specified text, or null if none exists.
   */
  public Window getWindow(String title) {
    return Arrays.stream(SwingUtilities.getWindowAncestor(mainPage).getOwnedWindows())
        .filter(w -> w instanceof Dialog)
        .filter(w -> ((Dialog) w).getTitle().equals(title))
        .findFirst()
        .orElse(null);
  }

  public JDialog createDialog(Frame parent, JComponent content, String title, boolean saveLocation,
      boolean saveSize) {
    JDialog dialog = new JDialog(parent, title);
    dialog.setContentPane(content);
    dialog.pack();
    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    return dialog;

  }

  public void registerTool(Tool tool) {
    if (!isToolRegistered(tool)) {
      addViewMenuEntry(tool);
      toolSet.put(tool.getId(), tool);
      tool.setRegistered(true);
      tool.addPropertyChangeListener(evt -> {
        mainPage.revalidate();
        mainPage.repaint();
      });
    }
    tool.getZone().add(tool);
  }

  public Window wrapInWindow(Tool tool) {
    Window window = getWindow(tool.getId());
    if (window == null) {
      window = createDialog(null,
          tool,
          tool.getTitle(),
          true, true);
      window.setMinimumSize(new Dimension(400, 500));
      window.setVisible(true);
    }
//    DevKitConfig.getInstance().getSdkConfig().registerWindow(tool.getId(), tool);
    return window;
  }

  public void moveZone(Tool tool, Zone newZone) {
    if (tool.getViewMode() != ViewMode.PIN) {
      return;
    }
    removeTool(tool);
    newZone.add(tool);
  }

  public void removeTool(JComponent tool) {
    if (tool.getParent() != null) {
      tool.getParent().remove(tool);
    }
  }

  public void hide(Tool toolView) {
    toolView.setVisible(false);
  }

  public void show(Tool toolView) {
    toolView.setVisible(true);
  }

  public void remove(Component component) {
    if (component.getParent() != null) {
      component.getParent().remove(component);
    }
  }

  public void changeToolViewMode(Tool toolView, ViewMode newViewMode) {
    if (toolView.getViewMode().equals(newViewMode)) {
      return;
    }
    if (!isToolRegistered(toolView)) {
      registerTool(toolView);
    }
  }

  public boolean isToolRegistered(Tool toolView) {
    return toolSet.get(toolView.getId()) != null;
  }


}
