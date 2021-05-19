package fr.exratio.jme.devkit.action;

import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateQuadAction extends CreateShapeAction {

  @Autowired
  public CreateQuadAction(SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication) {
    super(sceneGraphService, editorJmeApplication);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    sceneGraphService
        .add(createShape(new Quad(1.0f, 1.0f), "Quad"),
            (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
