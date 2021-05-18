package fr.exratio.jme.devkit.swing;

import com.jme3.app.StatsAppState;
import fr.exratio.jme.devkit.action.DisableAppStateAction;
import fr.exratio.jme.devkit.action.EnableAppStateAction;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.Configuration;
import fr.exratio.jme.devkit.forms.ImportModel;
import fr.exratio.jme.devkit.jme.CameraRotationWidgetState;
import fr.exratio.jme.devkit.jme.DebugGridState;
import fr.exratio.jme.devkit.lifecycle.ExitAction;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Frame;
import java.awt.event.ItemEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenu extends JMenuBar {

  JMenu fileMenu;
  JMenuItem importModelItem;
  JMenu editMenu;
  JMenuItem configItem;
  JMenu viewMenu;
  JMenu windowItem;
  private final EditorJmeApplication editorJmeApplication;

  @Autowired
  public MainMenu(ExitAction exitAction, DevKitConfig devKitConfig,
      EditorJmeApplication editorJmeApplication) {
    this.editorJmeApplication = editorJmeApplication;
    // FILE menu
    fileMenu = add(new JMenu("File"));

    importModelItem = fileMenu.add(new JMenuItem("Import Model..."));
    ImportModel importModel = new ImportModel();
    importModelItem.addActionListener(e -> {
      JDialog importModelDialog = new JDialog((Frame) null, "Import Model", true);
      importModelDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      importModelDialog.setContentPane(importModel);
      importModelDialog.pack();
      importModelDialog.setVisible(true);
    });

    fileMenu.add(new JSeparator());

    JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
    exitMenuItem.setAction(exitAction);

    // EDIT menu
    editMenu = add(new JMenu("Edit"));

    configItem = editMenu.add(new JMenuItem("Configuration..."));
    Configuration configuration = new Configuration(this.editorJmeApplication);
    configItem.addActionListener(e -> {
      JDialog configDialog = new JDialog((Frame) null, Configuration.WINDOW_ID, true);
      configDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      configDialog.setContentPane(configuration);
      configDialog.pack();
      configDialog.setVisible(true);

    });

    // VIEW menu
    viewMenu = add(new JMenu("View"));
    JCheckBoxMenuItem statsMenuItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Statistics"));
    EnableAppStateAction enableStatsAppState = new EnableAppStateAction(
        editorJmeApplication, StatsAppState.class);
    DisableAppStateAction disableStatsAppState = new DisableAppStateAction(
        editorJmeApplication, StatsAppState.class);
    statsMenuItem.addItemListener(e -> {
      boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
      if (isSelected) {
        enableStatsAppState.actionPerformed(null);
      } else {
        disableStatsAppState.actionPerformed(null);
      }
    });

    JCheckBoxMenuItem camRotWidgetItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Camera Rotation Widget"));
    camRotWidgetItem
        .setSelected(devKitConfig.isShowCamRotationWidget());
    EnableAppStateAction enableCameraRotationWidgetState = new EnableAppStateAction(
        editorJmeApplication, CameraRotationWidgetState.class);
    DisableAppStateAction disableCameraRotationWidgetState = new DisableAppStateAction(
        editorJmeApplication, CameraRotationWidgetState.class);
    camRotWidgetItem.addItemListener(e -> {
      boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
      if (isSelected) {
        enableCameraRotationWidgetState.actionPerformed(null);
      } else {
        disableCameraRotationWidgetState.actionPerformed(null);
      }
      devKitConfig.setShowCamRotationWidget(isSelected);
      devKitConfig.save();
    });

    EnableAppStateAction enableDebugGridState = new EnableAppStateAction(
        editorJmeApplication, DebugGridState.class);
    DisableAppStateAction disableDebugGridState = new DisableAppStateAction(
        editorJmeApplication, DebugGridState.class);

    JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Grid"));
    debugGridItem.setSelected(devKitConfig.isShowGrid());
    debugGridItem.addItemListener(e -> {
      boolean isSelected = e.getStateChange() == ItemEvent.SELECTED;
      if (isSelected) {
        enableDebugGridState.actionPerformed(null);
      } else {
        disableDebugGridState.actionPerformed(null);
      }
      devKitConfig.setShowCamRotationWidget(isSelected);
      devKitConfig.save();
    });

    // WINDOW menu
    windowItem = add(new JMenu("Window"));
  }

}
