import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(MainTest.class);
/*
  public static void main(String[] args) {
    AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext(SpringConfiguration.class);
    Main bean = context.getBean(Main.class);
    bean.run(args);
  }

  public static void main(String[] args) {

    LOGGER.info("Engine Version: {}", JmeSystem.getFullName());
    LOGGER.info(
        "Operating System: {} {}", System.getProperty("os.name"), System.getProperty("os.arch"));
    // Enable font anti-aliasing. On my setup (manjaro linux) this is definitely required.
    // I'll have to do further research to see if this has adverse effects on other systems.
    // System.setProperty("awt.useSystemAAFontSettings","on");
    // cleartype - I think this one looks better.
    System.setProperty("awt.useSystemAAFontSettings", "lcd");

    // set the theme.
    SwingTheme.setTheme(DevKitConfig.getInstance().getTheme());

    EditorJmeApplication engineService = ServiceManager.registerService(EditorJmeApplicationImpl.class);
    engineService.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setCustomRenderer(AwtPanelsContext.class);
    settings.setWidth(DevKitConfig.getInstance().getCameraDimension().width);
    settings.setHeight(DevKitConfig.getInstance().getCameraDimension().height);
    engineService.setSettings(settings);
    engineService.start(true);
    if (engineService != null) {

      while (engineService.getViewPort() == null) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          LOGGER.error("-- Main() Unexpected error occured", e);
        }
      }

    } else {
      LOGGER.info("Unable to create instance of JmeEngineService. Exiting.");
      System.exit(-1);
    }

    // The first "pass" involves just setting up the environment. Do as little as possible just so we can get the
    // window visible.
    SwingUtilities.invokeLater(() -> {

      // register all of our services...
      // All of these services are created on the AWT thread.

      ServiceManager.registerService(EventService.class);

      // Why is this being set?
      JPopupMenu.setDefaultLightWeightPopupEnabled(false);

      String parentDirName = new File(System.getProperty("user.dir")).getName();
      // now we have the window visible we can display progress data and load anything else we need.

      // register all of our services...
      // All of these services are created on the AWT thread.
      MainPageController coreService = ServiceManager.registerService(MainPageController.class, parentDirName);
      //fix the node being display only on resizing
      coreService.getMainFrame().revalidate();

      ServiceManager.registerService(RegistrationService.class);
      ServiceManager.registerService(SceneGraphService.class);
      ServiceManager.registerService(ClipboardService.class);
      ServiceManager.registerService(PluginService.class);
      engineService.getStateManager().attach(new MouseOverAppState());
      SpatialMoveToolState spatialMoveToolState = new SpatialMoveToolState();
      spatialMoveToolState.setEnabled(true);
      engineService.getStateManager().attach(spatialMoveToolState);
      SpatialRotateToolState2 spatialRotateToolState2 = new SpatialRotateToolState2(even);
      spatialRotateToolState2.setEnabled(true);
      engineService.getStateManager().attach(spatialRotateToolState2);
      engineService.getStateManager().attach(new SpatialSelectorState(eventBus));
      // load any available plugins.
      // I'm not sure where we should put this.
      ServiceManager.getService(PluginService.class).loadPlugins();

    });

  }*/


}
