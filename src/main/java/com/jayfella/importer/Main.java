package com.jayfella.importer;

import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.core.LogUtil;
import com.jayfella.importer.service.EventService;
import com.jayfella.importer.forms.Configuration;
import com.jayfella.importer.forms.DebugLights;
import com.jayfella.importer.forms.ImportModel;
import com.jayfella.importer.forms.MainPage;
import com.jayfella.importer.jme.AppStateUtils;
import com.jayfella.importer.jme.CameraRotationWidgetState;
import com.jayfella.importer.jme.DebugGridState;
import com.jayfella.importer.service.*;
import com.jayfella.importer.service.impl.JmeEngineServiceImpl;
import com.jayfella.importer.swing.*;
import com.jme3.app.StatsAppState;
import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.system.JmeSystem;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

public class Main {

    private static final Logger log = Logger.getLogger(Main.class.getName());

    private JFrame frame;

    public static void main(String[] args){

        LogUtil.initializeLogger(Level.INFO, true);

        Arrays.stream(new String[] {
                "com.jme3.audio.openal.ALAudioRenderer",
                "com.jme3.asset.AssetConfig",
                "com.jme3.material.plugins.J3MLoader",

                // startup information
                "com.jme3.system.JmeSystem",
                "com.jme3.system.lwjgl.LwjglContext",
                "org.reflections"
                // "com.jme3.renderer.opengl.GLRenderer"
        }).forEach(p -> LogManager.getLogger(p).setLevel(Level.ERROR));

        log.info("Engine Version: " + JmeSystem.getFullName());
        log.info("Operating System: " + System.getProperty("os.name") + " " + System.getProperty("os.arch"));

        Main main = new Main();
        main.start();
    }

    public Main() {

        // Enable font anti-aliasing. On my setup (manjaro linux) this is definitely required.
        // I'll have to do further research to see if this has adverse effects on other systems.
        // System.setProperty("awt.useSystemAAFontSettings","on");
        // cleartype - I think this one looks better.
        System.setProperty("awt.useSystemAAFontSettings","lcd");

        // set the theme.
        SwingTheme.setTheme(DevKitConfig.getInstance().getSdkConfig().getTheme());

        JmeEngineService engineService = ServiceManager.registerService(JmeEngineServiceImpl.class);

        if (engineService != null) {

            while (!engineService.isStarted()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // start the canvas as early as possible.
            engineService.startCanvas();
        }
        else {
            log.severe("Unable to create instance of JmeEngineService. Exiting.");
            System.exit(-1);
        }
    }

    public void start() {

        // The first "pass" involves just setting up the environment. Do as little as possible just so we can get the
        // window visible.
        SwingUtilities.invokeLater(() -> {

            // register all of our services...
            // All of these services are created on the AWT thread.
            ServiceManager.registerService(WindowService.class);
            ServiceManager.registerService(EventService.class);

            // Why is this being set?
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);

            String parentDirName = new File(System.getProperty("user.dir")).getName();

            frame = new JFrame("JmeDevKit: " + parentDirName);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter(){
                @Override
                public void windowClosed(WindowEvent e) {
                    ServiceManager.stop();
                }
            });

            initializeTreeView();
            initializeInspectorService();

            JMenuBar menu = createMenu();
            frame.setJMenuBar(menu);
            ServiceManager.registerService(new MenuService(menu));

            // position and size the main window...
            Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(MainPage.WINDOW_ID);
            Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(MainPage.WINDOW_ID);

            ServiceManager.getService(JmeEngineService.class).getCanvas().setSize(dimension);

            if (location == null) {
                frame.setLocationRelativeTo(null);
            }
            else {
                frame.setLocation(location);
            }

            // add the canvas AFTER we set the window size because the camera.getWidth and .getHeight values will change.
            // whilst it's easy to understand once you know, it can be confusing to figure this out.
            frame.add(ServiceManager.getService(JmeEngineService.class).getCanvas(), BorderLayout.CENTER);
            frame.pack();

            // save any changes of movement and size to the configuration.
            // frame.addComponentListener(new WindowSizeAndLocationSaver(MainPage.WINDOW_ID));
            frame.addComponentListener(new WindowLocationSaver(MainPage.WINDOW_ID));
            ServiceManager.getService(JmeEngineService.class).getCanvas().addComponentListener(new JmeCanvasSizeSaver());

            // show the window.
            frame.setVisible(true);

            // verify that the asset root directory has been set properly.
            checkAssetRootDir();

        });


        // now we have the window visible we can display progress data and load anything else we need.
        SwingUtilities.invokeLater(() -> {

            // register all of our services...
            // All of these services are created on the AWT thread.
            ServiceManager.registerService(RegistrationService.class);
            ServiceManager.registerService(ClipboardService.class);
            ServiceManager.registerService(PluginService.class);

            // load any available plugins.
            // I'm not sure where we should put this.
            ServiceManager.getService(PluginService.class).loadPlugins();

        });

    }

    private JMenuBar createMenu() {
        JMenuBar menuBar = new JMenuBar();

        // FILE menu
        JMenu fileMenu = menuBar.add(new JMenu("File"));

        JMenuItem importModelItem = fileMenu.add(new JMenuItem("Import Model..."));
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
        JMenu editMenu = menuBar.add(new JMenu("Edit"));

        JMenuItem configItem = editMenu.add(new JMenuItem("Configuration..."));
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
        JMenu viewMenu = menuBar.add(new JMenu("View"));
        JCheckBoxMenuItem statsMenuItem = (JCheckBoxMenuItem) viewMenu.add(new JCheckBoxMenuItem("Statistics"));
        statsMenuItem.addActionListener(e -> {
            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            AppStateUtils.toggleAppState(StatsAppState.class, checkBoxMenuItem.isSelected());
        });

        JCheckBoxMenuItem camRotWidgetItem = (JCheckBoxMenuItem) viewMenu.add(new JCheckBoxMenuItem("Camera Rotation Widget"));
        camRotWidgetItem.setSelected(DevKitConfig.getInstance().getSdkConfig().isShowCamRotationWidget());
        camRotWidgetItem.addActionListener(e -> {
            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            final boolean isSelected = checkBoxMenuItem.isSelected();

            AppStateUtils.toggleAppState(CameraRotationWidgetState.class, isSelected);

            DevKitConfig.getInstance().getSdkConfig().setShowCamRotationWidget(isSelected);
            DevKitConfig.getInstance().save();
        });

        JCheckBoxMenuItem debugGridItem = (JCheckBoxMenuItem) viewMenu.add(new JCheckBoxMenuItem("Grid"));
        debugGridItem.setSelected(DevKitConfig.getInstance().getSceneConfig().isShowGrid());
        debugGridItem.addActionListener(e -> {
            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            final boolean isSelected = checkBoxMenuItem.isSelected();

            AppStateUtils.toggleAppState(DebugGridState.class, isSelected);

            DevKitConfig.getInstance().getSceneConfig().setShowGrid(isSelected);
            DevKitConfig.getInstance().save();

        });

        // WINDOW menu
        JMenu windowItem = menuBar.add(new JMenu("Window"));
        JCheckBoxMenuItem debugLightsItem = (JCheckBoxMenuItem) windowItem.add(new JCheckBoxMenuItem("Debug Lights"));
        debugLightsItem.setSelected(DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow());
        debugLightsItem.addActionListener(e -> {

            JCheckBoxMenuItem checkBoxMenuItem = (JCheckBoxMenuItem) e.getSource();
            final boolean isSelected = checkBoxMenuItem.isSelected();

            // According to the documentation:
            // A Dialog is still owned even if it is disposed. dispose() only affects a Window's displayability, not its ownership.
            // A window can be re-created after disposal by calling .setVisible(true) or .pack().

            // so we check if the main window still owns the dialog, and if it does, call .setVisible(true), else create it.

            Window window = Arrays.stream(frame.getOwnedWindows())
                    .filter(w -> w instanceof Dialog)
                    .filter(w -> ((Dialog)w).getTitle().equals(DebugLights.DEBUG_LIGHTS_WINDOW_TITLE))
                    .findFirst()
                    .orElse(null);

            if (isSelected) {

                if (window == null) {
                    JDialog dialog = Windows.createDebugLightsWindow(frame, checkBoxMenuItem);
                    dialog.setVisible(true);

                }
                else {
                    window.setVisible(true);
                }

            }
            else {

                if (window != null) {
                    window.dispose();
                }

            }

            DevKitConfig.getInstance().getSdkConfig().setShowDebugLightsWindow(isSelected);
            DevKitConfig.getInstance().save();

        });

        // bit of a strange place to put it, but we need to menu item to toggle if the window is closed.
        if (DevKitConfig.getInstance().getSdkConfig().isShowDebugLightsWindow()) {
            JDialog dialog = Windows.createDebugLightsWindow(frame, debugLightsItem);
            dialog.setVisible(true);
        }

        return menuBar;
    }

    private void initializeTreeView() {

        JTree tree = new JTree();

        JDialog treeViewFrame = new JDialog(frame, "Scene Tree");

        treeViewFrame.setContentPane(new JScrollPane(tree));
        treeViewFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        treeViewFrame.pack();

        ServiceManager.getService(WindowService.class).positionWindowFromSavedPosition(treeViewFrame, SceneTreeService.WINDOW_ID);
        ServiceManager.getService(WindowService.class).sizeWindowFromSavedSize(treeViewFrame, SceneTreeService.WINDOW_ID);

        treeViewFrame.addComponentListener(new WindowLocationSaver(SceneTreeService.WINDOW_ID));
        treeViewFrame.addComponentListener(new WindowSizeSaver(SceneTreeService.WINDOW_ID));

        treeViewFrame.setVisible(true);

        ServiceManager.registerService(SceneTreeService.class, tree);

    }

    private void initializeInspectorService() {

        JPanel panelRoot = new JPanel(new VerticalLayout());
        panelRoot.setBorder(new EmptyBorder(10, 10, 10, 10));

        JDialog inspectorFrame = new JDialog(frame, PropertyInspectorService.WINDOW_ID);
        inspectorFrame.setContentPane(new JScrollPane(panelRoot));
        inspectorFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        ServiceManager.getService(WindowService.class).positionWindowFromSavedPosition(inspectorFrame, PropertyInspectorService.WINDOW_ID);
        ServiceManager.getService(WindowService.class).sizeWindowFromSavedSize(inspectorFrame, PropertyInspectorService.WINDOW_ID);

        inspectorFrame.addComponentListener(new WindowLocationSaver(PropertyInspectorService.WINDOW_ID));
        inspectorFrame.addComponentListener(new WindowSizeSaver(PropertyInspectorService.WINDOW_ID));

        inspectorFrame.setVisible(true);

        ServiceManager.registerService(PropertyInspectorService.class, panelRoot);

    }

    /**
     * Verifies the Asset Root directory. If an asset root has not been specified, it is defaulted to "src/main/resources".
     * A warning is displayed if the asset root directory does not exist.
     */
    private void checkAssetRootDir() {

        // if the asset root dir is null, specify the default dir and notify the user.
        if (DevKitConfig.getInstance().getProjectConfig().getAssetRootDir() == null) {

            Path assetRoot = Paths.get("src", "main", "resources");
            DevKitConfig.getInstance().getProjectConfig().setAssetRootDir(assetRoot.toAbsolutePath().toString());

            // @TODO: ask the user if they want to change it and navigate directly to it for them if true.

            JOptionPane.showMessageDialog(frame,
                    "Your Asset Root directory has not been set and has been assigned a default value " +
                            "of ./src/main/resources/"
                            + System.lineSeparator()
                            + "To change this value navigate to Edit -> Configuration.",
                    "No Asset Root Specified",
                    JOptionPane.INFORMATION_MESSAGE);

            DevKitConfig.getInstance().save();
        }

        // notify the user that the asset root dir doesn't exist.
        if (!new File(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir()).exists()) {

            JOptionPane.showMessageDialog(frame,
                    "The specified Asset Root directory does not exist. Please specify a valid Asset Root " +
                            "directory by navigating to Edit - Configuration",
                    "Invalid Asset Root",
                    JOptionPane.WARNING_MESSAGE);

        }

        else {

            // create a file locator for the asset root dir.

            AssetManager assetManager = ServiceManager.getService(JmeEngineService.class).getAssetManager();
            String assetRoot = DevKitConfig.getInstance().getProjectConfig().getAssetRootDir();

            assetManager.registerLocator(assetRoot, FileLocator.class);
            log.info("Registered Asset Root Directory: " + assetRoot);

        }

    }

}
