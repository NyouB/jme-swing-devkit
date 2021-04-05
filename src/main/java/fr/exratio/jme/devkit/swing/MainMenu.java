package fr.exratio.jme.devkit.swing;

import com.jme3.app.StatsAppState;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.forms.Configuration;
import fr.exratio.jme.devkit.forms.DebugLights;
import fr.exratio.jme.devkit.forms.ImportModel;
import fr.exratio.jme.devkit.forms.RunAppStateWindow;
import fr.exratio.jme.devkit.jme.AppStateUtils;
import fr.exratio.jme.devkit.jme.CameraRotationWidgetState;
import fr.exratio.jme.devkit.jme.DebugGridState;
import fr.exratio.jme.devkit.service.JmeEngineService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.WindowConstants;

public class MainMenu extends JMenuBar {

  JMenu fileMenu;
  JMenuItem importModelItem;
  JMenu editMenu;
  JMenuItem configItem;
  JMenu viewMenu;
  JMenu windowItem;

  public MainMenu(Frame frame) {
    // FILE menu
    fileMenu = add(new JMenu("File"));

    importModelItem = fileMenu.add(new JMenuItem("Import Model..."));
    importModelItem.addActionListener(e -> {

      ImportModel importModel = new ImportModel();
      JDialog importModelDialog = new JDialog(frame, "Import Model", true);
      importModelDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      importModelDialog.setContentPane(importModel.$$$getRootComponent$$$());
      importModelDialog.pack();
      importModelDialog.setLocationRelativeTo(frame);

      importModelDialog.setVisible(true);

    });

    fileMenu.add(new JSeparator());

    JMenuItem exitMenuItem = fileMenu.add(new JMenuItem("Exit"));
    exitMenuItem.addActionListener(e -> {
      ServiceManager.getService(JmeEngineService.class).stop();
      frame.dispose();
    });

    // EDIT menu
    editMenu = add(new JMenu("Edit"));

    configItem = editMenu.add(new JMenuItem("Configuration..."));
    configItem.addActionListener(e -> {

      Configuration configuration = new Configuration();

      JDialog configDialog = new JDialog(frame, Configuration.WINDOW_ID, true);
      configDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      configDialog.setContentPane(configuration.$$$getRootComponent$$$());
      configDialog.pack();
      configDialog.setLocationRelativeTo(frame);
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
        .setSelected(DevKitConfig.getInstance().isShowCamRotationWidget());
    camRotWidgetItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(CameraRotationWidgetState.class, isSelected);

      DevKitConfig.getInstance().setShowCamRotationWidget(isSelected);
      DevKitConfig.getInstance().save();
    });

    JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Grid"));
    debugGridItem.setSelected(DevKitConfig.getInstance().isShowGrid());
    debugGridItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(DebugGridState.class, isSelected);

      DevKitConfig.getInstance().setShowGrid(isSelected);
      DevKitConfig.getInstance().save();

    });

    // WINDOW menu
    windowItem = add(new JMenu("Window"));





  }

}
