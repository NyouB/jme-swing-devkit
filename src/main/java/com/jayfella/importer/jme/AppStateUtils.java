package com.jayfella.importer.jme;

import com.jayfella.importer.service.JmeEngineService;
import com.jayfella.importer.service.ServiceManager;
import com.jme3.app.state.AppState;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class AppStateUtils {

    public static void toggleAppState(final Class<? extends AppState> appstateClass, final boolean enabled) {

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

            }
            else {

                if (appState != null) {
                    engineService.getStateManager().detach(appState);
                }

            }

        });

    }

}
