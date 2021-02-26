package fr.exratio.devkit.jme;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.debug.Grid;
import fr.exratio.devkit.config.DevKitConfig;
import fr.exratio.devkit.config.SceneConfig;

public class DebugGridState extends BaseAppState {

  public static final String DEBUG_GRID_NAME = "Debug Grid";

  private Geometry gridGeometry;

  private void createGrid(AssetManager assetManager) {

    SceneConfig sceneConfig = DevKitConfig.getInstance().getSceneConfig();

    Grid grid = new Grid((int) sceneConfig.getGridSize().x, (int) sceneConfig.getGridSize().y,
        sceneConfig.getGridSize().z);
    gridGeometry = new Geometry(DEBUG_GRID_NAME, grid);

    gridGeometry.setMaterial(assetManager.loadMaterial("Materials/GridMaterial.j3m"));
    gridGeometry.getMaterial().setColor("Color", sceneConfig.getGridColor());

    gridGeometry.setLocalTranslation(sceneConfig.getGridLocation());

  }

  /**
   * Applies any changes made to DevKitConfig.
   *
   * @param refreshMesh whether or not to re-create the mesh.
   * @param refreshColor whether or not to change the color.
   */
  public void refreshMesh(boolean refreshMesh, boolean refreshColor) {

    SceneConfig sceneConfig = DevKitConfig.getInstance().getSceneConfig();

    if (refreshMesh) {
      Grid grid = new Grid((int) sceneConfig.getGridSize().x, (int) sceneConfig.getGridSize().y,
          sceneConfig.getGridSize().z);
      gridGeometry.setMesh(grid);
      gridGeometry.updateModelBound();
    }

    if (refreshColor) {
      gridGeometry.getMaterial().setColor("Color", sceneConfig.getGridColor());
    }

    gridGeometry.setLocalTranslation(sceneConfig.getGridLocation());

  }

  @Override
  protected void initialize(Application app) {
    createGrid(app.getAssetManager());
  }

  @Override
  protected void cleanup(Application app) {

  }

  @Override
  protected void onEnable() {
    ((SimpleApplication) getApplication()).getRootNode().attachChild(gridGeometry);
  }

  @Override
  protected void onDisable() {
    gridGeometry.removeFromParent();
  }


}
