package fr.exratio.devkit.jme;

import com.jme3.app.state.AppState;
import fr.exratio.devkit.service.JmeEngineService;
import fr.exratio.devkit.service.ServiceManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AppStateUtils {

  public static void toggleAppState(final Class<? extends AppState> appstateClass,
      final boolean enabled) {

    JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

    engineService.enqueue(() -> {

      AppState appState = engineService.getStateManager().getState(appstateClass);

      if (enabled) {

        if (appState == null) {
          // engineService.getStateManager().attach(new CameraRotationWidgetState());
          try {
            Constructor<? extends AppState> constructor = appstateClass.getConstructor();
            appState = constructor.newInstance();

            engineService.getStateManager().attach(appState);

          } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
          }
        }

      } else {

        if (appState != null) {
          engineService.getStateManager().detach(appState);
        }

      }

    });

  }

  public static boolean isBulletPhysicsOnClassPath() {

    try {
      Class<?> bulletAppStateClass = Class.forName("com.jme3.bullet.BulletAppState");
      return true;
    } catch (ClassNotFoundException e) {
      // e.printStackTrace();
      // do nothing.
    }

    return false;
  }

}
