package fr.exratio.jme.devkit.action;

import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateSphereAction extends CreateShapeAction {

  @Autowired
  public CreateSphereAction(SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication) {
    super(sceneGraphService, editorJmeApplication.getAssetManager());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    sceneGraphService
        .add(createShape(new Sphere(32, 32, 1.0f), "Sphere"),
            (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
