package fr.exratio.jme.devkit.action;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Dome;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;

public class CreateDomeAction extends CreateShapeAction {

  public CreateDomeAction(SceneGraphService sceneGraphService,
      AssetManager assetManager) {
    super(sceneGraphService, assetManager);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    sceneGraphService
        .addSpatial(createShape(new Dome(32, 32, 1.0f), "Dome"),
            (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
