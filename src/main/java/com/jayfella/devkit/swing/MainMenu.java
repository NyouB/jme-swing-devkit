package com.jayfella.devkit.swing;

import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.forms.Configuration;
import com.jayfella.devkit.forms.DebugLights;
import com.jayfella.devkit.forms.ImportModel;
import com.jayfella.devkit.forms.RunAppStateWindow;
import com.jayfella.devkit.jme.AppStateUtils;
import com.jayfella.devkit.jme.CameraRotationWidgetState;
import com.jayfella.devkit.jme.DebugGridState;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import com.jayfella.devkit.service.WindowService;
import com.jme3.app.StatsAppState;
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
        .setSelected(DevKitConfig.getInstance().getSdkConfig().isShowCamRotationWidget());
    camRotWidgetItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(CameraRotationWidgetState.class, isSelected);

      DevKitConfig.getInstance().getSdkConfig().setShowCamRotationWidget(isSelected);
      DevKitConfig.getInstance().save();
    });

    JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu
        .add(new JCheckBoxMenuItem("Grid"));
    debugGridItem.setSelected(DevKitConfig.getInstance().getSceneConfig().isShowGrid());
    debugGridItem.addActionListener(e -> {
      JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
      final boolean isSelected = checkBoxMenuItem.isSelected();

      AppStateUtils.toggleAppState(DebugGridState.class, isSelected);

      DevKitConfig.getInstance().getSceneConfig().setShowGrid(isSelected);
      DevKitConfig.getInstance().save();

    });

    // WINDOW menu
    windowItem = add(new JMenu("Window"));
    JCheckBoxMenuItem debugLightsItem = (JCheckBoxMenuItem) windowItem
        .add(new JCheckBoxMenuItem("Debug Lights"));
    debugLightsItem
        .setSelected(DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow());
    debugLightsItem.addActionListener(e -> {

      // A Dialog is still owned even if it is disposed. dispose() only affects a Window's displayability, not its ownership.
      // A window can be re-created after disposal by calling .setVisible(true) or .pack().

      // so we check if the main window still owns the dialog, and if it does, call .setVisible(true), else create it.

      Window window = ServiceManager.getService(WindowService.class)
          .getWindow(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE);

      if (debugLightsItem.isSelected()) {

        if (window == null) {
          DebugLights debugLights = new DebugLights();

          JDialog dialog = ServiceManager.getService(WindowService.class)
              .createDialog(frame,
                  debugLights.$$$getRootComponent$$$(),
                  DebugLights.DEBUG_LIGHTS_WINDOW_TITLE,
                  true, true);

          dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
              debugLightsItem.setSelected(false);
            }
          });

          dialog.setVisible(true);

        } else {
          window.setVisible(true);
        }

      } else {

        if (window != null) {
          window.dispose();
        }

      }

      DevKitConfig.getInstance().getSdkConfig()
          .setShowDebugLightsWindow(debugLightsItem.isSelected());
      DevKitConfig.getInstance().save();

    });

    // bit of a strange place to put it, but we need to menu item to toggle if the window is closed.
    if (DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow()) {
      DebugLights debugLights = new DebugLights();

      JDialog dialog = ServiceManager.getService(WindowService.class)
          .createDialog(frame,
              debugLights.$$$getRootComponent$$$(),
              DebugLights.DEBUG_LIGHTS_WINDOW_TITLE,
              true, true);

      dialog.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          debugLightsItem.setSelected(false);
        }
      });

      dialog.setVisible(true);
    }

    // AppState Window
    JCheckBoxMenuItem runAppStateItem = (JCheckBoxMenuItem) windowItem
        .add(new JCheckBoxMenuItem("Run AppState"));
    runAppStateItem
        .setSelected(DevKitConfig.getInstance().getSdkConfig().isShowRunAppStateWindow());
    runAppStateItem.addActionListener(e -> {

      Window window = ServiceManager.getService(WindowService.class)
          .getWindow(RunAppStateWindow.RUN_APPSTATE_WINDOW_TITLE);

      if (runAppStateItem.isSelected()) {

        if (window == null) {
          RunAppStateWindow runAppStateWindow = new RunAppStateWindow();

          JDialog dialog = ServiceManager.getService(WindowService.class)
              .createDialog(frame,
                  runAppStateWindow.$$$getRootComponent$$$(),
                  RunAppStateWindow.RUN_APPSTATE_WINDOW_TITLE,
                  true, true);

          dialog.setMinimumSize(new Dimension(400, 500));

          dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
              runAppStateItem.setSelected(false);
            }
          });

          dialog.setVisible(true);

        } else {
          window.setVisible(true);
        }

      } else {

        if (window != null) {
          window.dispose();
        }

      }

      DevKitConfig.getInstance().getSdkConfig()
          .setShowRunAppStateWindow(runAppStateItem.isSelected());
      DevKitConfig.getInstance().save();

    });

    // bit of a strange place to put it, but we need to menu item to toggle if the window is closed.
    if (DevKitConfig.getInstance().getSdkConfig().isShowRunAppStateWindow()) {
      RunAppStateWindow runAppStateWindow = new RunAppStateWindow();

      JDialog dialog = ServiceManager.getService(WindowService.class)
          .createDialog(frame,
              runAppStateWindow.$$$getRootComponent$$$(),
              RunAppStateWindow.RUN_APPSTATE_WINDOW_TITLE,
              true, true);

      dialog.setMinimumSize(new Dimension(400, 500));

      dialog.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosed(WindowEvent e) {
          runAppStateItem.setSelected(false);
        }
      });

      dialog.setVisible(true);
    }

  }

}
