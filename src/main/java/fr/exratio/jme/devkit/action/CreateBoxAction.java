package fr.exratio.jme.devkit.action;

import static devkit.appstate.tool.SpatialMoveToolState.COLOR;
import static devkit.appstate.tool.SpatialMoveToolState.MAT_DEF;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
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
        .addSpatial(createShape(new Box(1, 1, 1), "Cube"), (Node) sceneGraphService.getSelectedObject());
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
