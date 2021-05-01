package fr.exratio.jme.devkit.action;

import com.jme3.app.state.AppState;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class DisableAppStateAction extends AbstractAction {

  private final EditorJmeApplication editorJmeApplication;
  private final Class<? extends AppState> appstateClass;

  public DisableAppStateAction(
      EditorJmeApplication editorJmeApplication,
      Class<? extends AppState> appstateClass) {
    this.editorJmeApplication = editorJmeApplication;
    this.appstateClass = appstateClass;
  }

  @Override
  public void actionPerformed(ActionEvent event) {

    editorJmeApplication.enqueue(() -> {
      AppState appState = editorJmeApplication.getStateManager().getState(appstateClass);
      if (appState != null) {
        editorJmeApplication.getStateManager().detach(appState);
      }
    });
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
