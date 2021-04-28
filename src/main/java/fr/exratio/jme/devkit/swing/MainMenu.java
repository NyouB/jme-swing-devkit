package fr.exratio.jme.devkit.swing;

import com.jme3.app.StatsAppState;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.Configuration;
import fr.exratio.jme.devkit.forms.ImportModel;
import fr.exratio.jme.devkit.jme.AppStateUtils;
import fr.exratio.jme.devkit.jme.CameraRotationWidgetState;
import fr.exratio.jme.devkit.jme.DebugGridState;
import fr.exratio.jme.devkit.lifecycle.ExitAction;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Frame;
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
  private final ExitAction exitAction;
  private final DevKitConfig devKitConfig;
  private final EditorJmeApplication editorJmeApplication;

  @Autowired
  public MainMenu(ExitAction exitAction, DevKitConfig devKitConfig,
      EditorJmeApplication editorJmeApplication) {
    this.exitAction = exitAction;
    this.devKitConfig = devKitConfig;
    this.editorJmeApplication = editorJmeApplication;
    // FILE menu
    fileMenu = add(new JMenu("File"));

    importModelItem = fileMenu.add(new JMenuItem("Import Model..."));
    importModelItem.addActionListener(e -> {
      ImportModel importModel = new ImportModel();
      JDialog importModelDialog = new JDialog((Frame) null, "Import Model", true);
      importModelDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      importModelDialog.setContentPane(importModel.$$$getRootComponent$$$());
      importModelDialog.pack();
      importModelDialog.setVisible(true);
    });

    fileMenu.add(new JSeparator());

    JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
    exitMenuItem.setAction(exitAction);

    // EDIT menu
    editMenu = add(new JMenu("Edit"));

    configItem = editMenu.add(new JMenuItem("Configuration..."));
    configItem.addActionListener(e -> {
      Configuration configuration = new Configuration(this.editorJmeApplication);
      JDialog configDialog = new JDialog((Frame) null, Configuration.WINDOW_ID, true);
      configDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      configDialog.setContentPane(configuration.$$$getRootComponent$$$());
      configDialog.pack();
      configDialog.setVisible(true);

    });

    // VIEW menu
    viewMenu = add(new JMenu("View"));
    JCheckBoxMenuItem statsMenuItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Statistics"));
    statsMenuItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      AppStateUtils.toggleAppState(StatsAppState.class, checkBoxMenuItem.isSelected());
    });

    JCheckBoxMenuItem camRotWidgetItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Camera Rotation Widget"));
    camRotWidgetItem
        .setSelected(devKitConfig.isShowCamRotationWidget());
    camRotWidgetItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(CameraRotationWidgetState.class, isSelected);

      devKitConfig.setShowCamRotationWidget(isSelected);
      devKitConfig.save();
    });

    JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Grid"));
    debugGridItem.setSelected(devKitConfig.isShowGrid());
    debugGridItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(DebugGridState.class, isSelected);

      devKitConfig.setShowGrid(isSelected);
      devKitConfig.save();

    });

    // WINDOW menu
    windowItem = add(new JMenu("Window"));
  }

}
