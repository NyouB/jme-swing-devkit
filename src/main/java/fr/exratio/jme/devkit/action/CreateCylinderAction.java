package fr.exratio.jme.devkit.action;

import com.jme3.scene.Node;
import com.jme3.scene.shape.Cylinder;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateCylinderAction extends CreateShapeAction {

  @Autowired
  public CreateCylinderAction(SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication) {
    super(sceneGraphService, editorJmeApplication.getAssetManager());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    sceneGraphService
        .add(createShape(new Cylinder(32, 32, 1.0f, 1.0f, true), "Cylinder"),
            (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
