package devkit.appstate.tool;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.event.SelectedItemEvent;
import fr.exratio.jme.devkit.service.EventService;
import fr.exratio.jme.devkit.service.ServiceManager;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractSpatialToolState extends BaseAppState {

  protected Spatial selectedSpatial;
  protected Node toolModel;

  protected boolean busy;
  protected float distance;
  protected final EventBus eventBus;

  @Autowired
  public AbstractSpatialToolState(EventBus eventBus) {
    this.eventBus = eventBus;
    eventBus.register(this);
  }

  @Subscribe
  private void onSelectedItem(SelectedItemEvent event) {
    if (event.getSelectedItem() instanceof Spatial) {
      setSpatial((Spatial) event.getSelectedItem());
    }
  }

  public void setSpatial(Spatial spatial) {

    this.selectedSpatial = spatial;

    setEnabled(this.selectedSpatial != null);

    if (selectedSpatial != null && selectedSpatial.getParent() != null) {
      // selectedSpatial.getParent().attachChild(toolModel);
      ((SimpleApplication) getApplication()).getRootNode().attachChild(toolModel);
    }
  }

  public Spatial getSpatial() {
    return selectedSpatial;
  }

  public boolean isBusy() {
    return busy;
  }

  private void calculateScale(Camera camera) {

    Vector3f relative = toolModel.getWorldTranslation().subtract(camera.getLocation());
    // Vector3f dir = relative.normalize();

    // axisColors[0].a = dirAlpha(dir, Vector3f.UNIT_X);
    // axisColors[1].a = dirAlpha(dir, Vector3f.UNIT_Y);
    // axisColors[2].a = dirAlpha(dir, Vector3f.UNIT_Z);

    // Need to figure out how much to scale the widget so that it stays
    // the same size on screen.  In our case, we want 1 unit to be
    // 100 pixels.
    Vector3f dir = camera.getDirection();
    float distance = dir.dot(toolModel.getWorldTranslation().subtract(camera.getLocation()));

    // m11 of the projection matrix defines the distance at which 1 pixel
    // is 1 unit.  Kind of.
    float m11 = getApplication().getCamera().getProjectionMatrix().m11;

    // Magic scaling... trust the math... don't question the math... magic math...
    float halfHeight = getApplication().getCamera().getHeight() * 0.5f;
    float scale = ((distance / halfHeight) * 100) / m11;
    toolModel.setLocalScale(scale);
  }

  @Override
  public void update(float tpf) {

    Camera camera = getApplication().getCamera();
    calculateScale(camera);
    distance = camera.getLocation().distance(toolModel.getWorldTranslation());

    /*
            // keep the size constant

            toolModel.setLocalScale(distance * .1f);
    */
    if (selectedSpatial != null) {
      toolModel.setLocalTranslation(selectedSpatial.getWorldTranslation());
      // toolModel.setLocalRotation(selectedSpatial.getWorldRotation());
    }
  }
}
