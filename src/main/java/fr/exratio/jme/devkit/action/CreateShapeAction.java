package fr.exratio.jme.devkit.action;

import static devkit.appstate.tool.SpatialMoveToolState.COLOR;
import static devkit.appstate.tool.SpatialMoveToolState.MAT_DEF;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public abstract class CreateShapeAction extends AbstractAction {

  protected final SceneGraphService sceneGraphService;
  protected final AssetManager assetManager;

  public CreateShapeAction(SceneGraphService sceneGraphService,
      AssetManager assetManager) {
    this.sceneGraphService = sceneGraphService;
    this.assetManager = assetManager;
  }

  protected Geometry createShape(Mesh mesh, String name) {
    Geometry geometry = new Geometry(name, mesh);
    Material material = new Material(assetManager, MAT_DEF);
    geometry.setMaterial(material);
    material.setColor(COLOR, ColorRGBA.Blue);
    return geometry;
  }

}
