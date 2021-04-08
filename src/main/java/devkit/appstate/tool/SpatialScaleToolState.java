package devkit.appstate.tool;

import com.jme3.app.Application;
import com.jme3.scene.Node;

public class SpatialScaleToolState extends AbstractSpatialToolState {

  public SpatialScaleToolState() {
    super(eventBus);
  }

  @Override
  protected void initialize(Application app) {

    toolModel = new Node("Scale Tool");

  }

  @Override
  protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {

    }

    @Override
    protected void onDisable() {

    }
}
