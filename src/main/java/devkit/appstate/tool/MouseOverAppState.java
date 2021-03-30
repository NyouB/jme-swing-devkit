package devkit.appstate.tool;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.BaseAppState;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class MouseOverAppState extends BaseAppState {


  private CollisionResults collisionResults;
  private Camera camera;
  private InputManager inputManager;
  private Node rootNode;
  private Geometry currentMouseHoverGeometry;

  @Override
  protected void initialize(Application app) {
    camera = app.getCamera();
    inputManager = app.getInputManager();
    rootNode = ((SimpleApplication) app).getRootNode();
    collisionResults = new CollisionResults();
  }

  @Override
  protected void cleanup(Application app) {

  }

  @Override
  public void update(float tpf) {
    super.update(tpf);
    Vector3f origin = camera.getWorldCoordinates(inputManager.getCursorPosition(), 0.0f);
    Vector3f direction = camera.getWorldCoordinates(inputManager.getCursorPosition(), 0.3f);
    direction.subtractLocal(origin).normalizeLocal();
    Ray ray = new Ray(origin, direction);
    collisionResults.clear();
    rootNode.collideWith(ray, collisionResults);
    if (collisionResults.getClosestCollision() != null) {
      currentMouseHoverGeometry = collisionResults.getClosestCollision().getGeometry();
    } else {
      currentMouseHoverGeometry = null;
    }
  }

  @Override
  protected void onEnable() {

  }

  @Override
  protected void onDisable() {

  }

  public CollisionResults getCollisionResults() {
    return collisionResults;
  }

  public Geometry getCurrentMouseHoverGeometry() {
    return currentMouseHoverGeometry;
  }
}
