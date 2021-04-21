package fr.exratio.jme.devkit.action;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;

public class CreateBoxAction extends CreateShapeAction {

  public CreateBoxAction(SceneGraphService sceneGraphService,
      AssetManager assetManager) {
    super(sceneGraphService, assetManager);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    sceneGraphService
        .add(createShape(new Box(1, 1, 1), "Cube"), (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
