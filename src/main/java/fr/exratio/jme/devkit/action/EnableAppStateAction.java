package fr.exratio.jme.devkit.action;

import com.jme3.app.state.AppState;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.event.ActionEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.AbstractAction;

public class EnableAppStateAction extends AbstractAction {

  private final EditorJmeApplication editorJmeApplication;
  private final Class<? extends AppState> appstateClass;

  public EnableAppStateAction(
      EditorJmeApplication editorJmeApplication,
      Class<? extends AppState> appstateClass) {
    this.editorJmeApplication = editorJmeApplication;
    this.appstateClass = appstateClass;
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    editorJmeApplication.enqueue(() -> {
      AppState appState = editorJmeApplication.getStateManager().getState(appstateClass);
      if (appState == null) {
        // editorJmeApplication.getStateManager().attach(new CameraRotationWidgetState());
        try {
          Constructor<? extends AppState> constructor = appstateClass.getConstructor();
          appState = constructor.newInstance();
          editorJmeApplication.getStateManager().attach(appState);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    });
  }

  @Override
  public boolean accept(Object sender) {

    return false;
  }
}
