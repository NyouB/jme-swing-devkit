package fr.exratio.jme.devkit.action;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Quad;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;

public class CreateQuadAction extends CreateShapeAction {

  public CreateQuadAction(SceneGraphService sceneGraphService,
      AssetManager assetManager) {
    super(sceneGraphService, assetManager);
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
