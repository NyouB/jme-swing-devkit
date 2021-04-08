package devkit.appstate.tool;

import com.google.common.eventbus.EventBus;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Line;
import fr.exratio.jme.devkit.event.SelectedItemEvent;
import fr.exratio.jme.devkit.service.EventService;
import fr.exratio.jme.devkit.service.ServiceManager;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SpatialSelectorState extends BaseAppState implements ActionListener {

  private final CollisionResults collisionResults = new CollisionResults();
  private final Ray ray = new Ray();
  private static final Logger LOGGER = LoggerFactory.getLogger(SpatialSelectorState.class);
  private final EventBus eventBus;

  @Autowired
  public SpatialSelectorState(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  @Override
  protected void initialize(Application app) {
  }

  @Override
  protected void cleanup(Application app) {
  }

  @Override
  protected void onEnable() {
    InputManager inputManager = getApplication().getInputManager();

    inputManager.addMapping("Select Spatial", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addListener(this, "Select Spatial");
  }

  @Override
  protected void onDisable() {

    InputManager inputManager = getApplication().getInputManager();

    inputManager.deleteMapping("Select Spatial");
    inputManager.removeListener(this);
  }

  @Override
  public void onAction(String binding, boolean isPressed, float tpf) {
/*
    if (getState(SpatialToolState.class).isBusy()) {
      return;
    }*/

    if (binding.equals("Select Spatial") && !isPressed) {

      InputManager inputManager = getApplication().getInputManager();
      // JmePanel jmePanel = ServiceManager.getService(JmeEngineService.class).getActivePanel();
      // Camera cam = jmePanel.getCamera();
      Camera cam = getApplication().getCamera();

      Vector2f click2d = inputManager.getCursorPosition();
      Vector3f click3d = cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
      Vector3f dir =
          cam.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f)
              .subtractLocal(click3d)
              .normalizeLocal();

      ray.setOrigin(click3d);
      ray.setDirection(dir);
      drawRay(cam.getLocation(), cam.getDirection(), ColorRGBA.Green);
      drawRay(click3d, cam.getDirection(), ColorRGBA.Red);
      drawRay(click3d, dir, ColorRGBA.Blue);
      ((SimpleApplication) getApplication()).getRootNode().collideWith(ray, collisionResults);
      if (LOGGER.isTraceEnabled()) {
        for (int i = 0; i < collisionResults.size(); i++) {
          // (For each hit, we know distance, impact point, geometry.)
          float dist = collisionResults.getCollision(i).getDistance();
          Vector3f pt = collisionResults.getCollision(i).getContactPoint();
          String target = collisionResults.getCollision(i).getGeometry().getName();
          System.out.println(
              "Selection #" + i + ": " + target + " at " + pt + ", " + dist + " WU away.");
        }
      }
      if (collisionResults.size() > 0) {
        Geometry geometry = collisionResults.getClosestCollision().getGeometry();
        LOGGER.debug(">> onAction > collision geometry found: {}", geometry.toString());

        SwingUtilities.invokeLater(() -> eventBus.post(new SelectedItemEvent(geometry)));

        collisionResults.clear();
      }
    }
  }

  public void drawRay(Vector3f source, Vector3f destination, ColorRGBA color) {

    Line line = new Line(source, destination);
    Geometry lineGeometry = new Geometry("line", line);
    Material lineMaterial =
        new Material(getApplication().getAssetManager(), "/MatDefs/FogUnshaded.j3md");
    lineMaterial.setColor("Color", color);
    lineGeometry.setMaterial(lineMaterial);
    ((SimpleApplication) getApplication()).getRootNode().attachChild(lineGeometry);
  }
}
