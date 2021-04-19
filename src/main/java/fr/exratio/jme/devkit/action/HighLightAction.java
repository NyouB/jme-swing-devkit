package fr.exratio.jme.devkit.action;

import com.jme3.app.Application;
import fr.exratio.jme.devkit.jme.SceneObjectHighlighterState;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.springframework.stereotype.Component;

@Component
public class HighLightAction extends AbstractAction {

  public static final String HIGHLIGHT_ACTION = "HighLightAction";
  private final Application editorJmeApplication;
  private final SceneObjectHighlighterState highlighterState;
  private final SceneGraphService sceneGraphService;

  public HighLightAction(Application editorJmeApplication,
      SceneObjectHighlighterState highlighterState,
      SceneGraphService sceneGraphService) {
    super(HIGHLIGHT_ACTION);
    this.editorJmeApplication = editorJmeApplication;
    this.highlighterState = highlighterState;
    this.sceneGraphService = sceneGraphService;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // highlighting
    // remove and rebuild all highlights.
    // we could probably not be so aggressive and remove items that are no longer selected.
    // lets just get this working for now.
    Object object = sceneGraphService.getSelectedObject();

    editorJmeApplication.enqueue(() -> highlighterState.highlight(object));
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
