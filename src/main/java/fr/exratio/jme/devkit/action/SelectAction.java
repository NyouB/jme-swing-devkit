package fr.exratio.jme.devkit.action;

import com.jme3.app.Application;
import fr.exratio.jme.devkit.jme.SceneObjectHighlighterState;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.springframework.stereotype.Component;

@Component
public class SelectAction extends AbstractAction {

  public static final String HIGHLIGHT_ACTION = "HighLightAction";
  private final Application editorJmeApplication;

  public SelectAction(Application editorJmeApplication) {
    super(HIGHLIGHT_ACTION);
    this.editorJmeApplication = editorJmeApplication;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    SceneObjectHighlighterState highlighterState = editorJmeApplication.getStateManager()
        .getState(SceneObjectHighlighterState.class);

    // highlighting
    // remove and rebuild all highlights.
    // we could probably not be so aggressive and remove items that are no longer selected.
    // lets just get this working for now.
    editorJmeApplication.enqueue(highlighterState::removeAllHighlights);
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
