package fr.exratio.jme.devkit.action;

import com.jme3.scene.Node;
import com.jme3.scene.shape.Dome;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateDomeAction extends CreateShapeAction {

  @Autowired
  public CreateDomeAction(SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication) {
    super(sceneGraphService, editorJmeApplication);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    sceneGraphService
        .add(createShape(new Dome(32, 32, 1.0f), "Dome"),
            (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
