package fr.exratio.jme.devkit.jme;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EditorCameraState extends BaseAppState implements AnalogListener, ActionListener {

  public static final String CAMERA_ACTIVATED = "cameraActivated";
  public static final String ROTATE_LEFT = "rotateLeft";
  public static final String ROTATE_RIGHT = "rotateRight";
  public static final String ROTATE_UP = "rotateUp";
  public static final String ROTATE_DOWN = "rotateDown";
  public static final String MOVE_LEFT = "moveLeft";
  public static final String MOVE_RIGHT = "moveRight";
  public static final String MOVE_FORWARD = "moveForward";
  public static final String MOVE_BACKWARD = "moveBackward";
  public static final String MOVE_UP = "moveUp";
  public static final String MOVE_DOWN = "moveDown";
  public static final String ZOOM_IN = "zoomIn";
  public static final String ZOOM_OUT = "zoomOut";

  private static final Logger LOGGER = LoggerFactory.getLogger(EditorCameraState.class);
  private final float[] camAngles = new float[]{0, FastMath.PI, 0};

  // Location and rotation are positioned away from 0,0,0 because if the user imports a model they will most likely
  // be inside it and won't see it. To avoid confusion we'll move away by default.
  // We move in the +Z direction (away/back) so the +X axis points to the right as one would expect.
  // We move slightly "up" so that the grid is visually apparent.
  private final Quaternion camRotation = new Quaternion();
  // set when onDisable called, read when onEnable called.
  // This is NOT the current location.
  private final Vector3f cameraLocation = new Vector3f(0, 1, 15);
  private Camera cam;
  private float panSpeed = 10.0F;
  private float rotateSpeed = 5.0F;
  private float zoomSpeed = 10.0F;

  private boolean cameraActivated;
  private boolean zoomIn;
  private boolean zoomOut;
  private boolean moveLeft;
  private boolean moveRight;
  private boolean moveUp;
  private boolean moveDown;
  private boolean moveForward;
  private boolean moveBackward;


  public EditorCameraState() {
  }

  public float getPanSpeed() {
    return panSpeed;
  }

  public void setPanSpeed(float panSpeed) {
    this.panSpeed = panSpeed;
  }

  public float getRotateSpeed() {
    return rotateSpeed;
  }

  public void setRotateSpeed(float rotateSpeed) {
    this.rotateSpeed = rotateSpeed;
  }

  public float getZoomSpeed() {
    return zoomSpeed;
  }

  public void setZoomSpeed(float zoomSpeed) {
    this.zoomSpeed = zoomSpeed;
  }

  protected void initialize(Application app) {
    cam = app.getCamera();
  }

  public Camera getCamera() {
    return cam;
  }

  private void addMappings() {

    InputManager inputManager = getApplication().getInputManager();
    azertyMapping(inputManager);

    inputManager.addListener(this, ROTATE_RIGHT, ROTATE_LEFT, ROTATE_UP, ROTATE_DOWN,
        CAMERA_ACTIVATED, MOVE_FORWARD, MOVE_BACKWARD, MOVE_LEFT, MOVE_RIGHT, MOVE_DOWN,
        MOVE_UP, ZOOM_IN, ZOOM_OUT);

    LOGGER.info("-- addMappings() Keyboard Mappings Registered ");
  }

  private void removeMappings() {

    InputManager inputManager = getApplication().getInputManager();

    inputManager.deleteMapping(ROTATE_RIGHT);
    inputManager.deleteMapping(ROTATE_LEFT);
    inputManager.deleteMapping(ROTATE_UP);
    inputManager.deleteMapping(ROTATE_DOWN);
    inputManager.deleteMapping(CAMERA_ACTIVATED);
    inputManager.deleteMapping(MOVE_FORWARD);
    inputManager.deleteMapping(MOVE_BACKWARD);
    inputManager.deleteMapping(MOVE_LEFT);
    inputManager.deleteMapping(MOVE_RIGHT);
    inputManager.deleteMapping(MOVE_DOWN);
    inputManager.deleteMapping(MOVE_UP);
    inputManager.deleteMapping(ZOOM_IN);
    inputManager.deleteMapping(ZOOM_OUT);
    inputManager.removeListener(this);

    LOGGER.info("-- removeMappings() Unregistered Keyboard Mappings");
  }

  @Override
  protected void cleanup(Application app) {
  }

  @Override
  protected void onEnable() {
    addMappings();

    // put the camera back to where it was if/when it was disabled.
    getApplication().getCamera().setLocation(cameraLocation);
    setRotation(camAngles[0], camAngles[1], camAngles[2]);
  }

  @Override
  protected void onDisable() {

    cameraActivated = zoomIn = zoomOut = moveLeft = moveRight = moveUp = moveDown = false;
    removeMappings();

    // store the existing location so we can re-set it.
    cameraLocation.set(getApplication().getCamera().getLocation());
  }

  @Override
  public void update(float tpf) {
    if (cam == null || !cameraActivated) {
      return;
    }

    if (moveForward) {
      zoomCamera(tpf * zoomSpeed);
    }

    if (moveBackward) {
      zoomCamera(-tpf * zoomSpeed);
    }

    if (moveLeft) {
      panCamera(tpf * panSpeed, 0.0F);
    }

    if (moveRight) {
      panCamera(-tpf * panSpeed, 0.0F);
    }

    if (moveUp) {
      panCamera(0.0F, tpf * panSpeed);
    }

    if (moveDown) {
      panCamera(0.0F, -tpf * panSpeed);
    }

  }

  @Override
  public void onAction(String name, boolean isPressed, float tpf) {
    if (cam == null || !isEnabled()) {
      return;
    }

    if (name.equals(CAMERA_ACTIVATED)) {
      cameraActivated = isPressed;
    }

    if (name.equals(MOVE_FORWARD)) {
      moveForward = isPressed;
    }

    if (name.equals(MOVE_BACKWARD)) {
      moveBackward = isPressed;
    }

    if (name.equals(MOVE_LEFT)) {
      moveLeft = isPressed;
    }

    if (name.equals(MOVE_RIGHT)) {
      moveRight = isPressed;
    }

    if (name.equals(MOVE_UP)) {
      moveUp = isPressed;
    }

    if (name.equals(MOVE_DOWN)) {
      moveDown = isPressed;
    }

  }

  private void panCamera(float left, float up) {
    Vector3f leftVec = cam.getLeft().mult(left);
    Vector3f upVec = cam.getUp().mult(up);
    Vector3f camLoc = cam.getLocation().add(leftVec).add(upVec);
    cam.setLocation(camLoc);
  }

  private void rotateCamera(float y, float x) {
    float[] var10000 = camAngles;
    var10000[0] += x;
    var10000 = camAngles;
    var10000[1] += y;
    float maxRotX = 1.553343F;
    if (camAngles[0] < -maxRotX) {
      camAngles[0] = -maxRotX;
    }

    if (camAngles[0] > maxRotX) {
      camAngles[0] = maxRotX;
    }

    if (camAngles[1] > 6.2831855F) {
      var10000 = camAngles;
      var10000[1] -= 6.2831855F;
    } else if (camAngles[1] < -6.2831855F) {
      var10000 = camAngles;
      var10000[1] += 6.2831855F;
    }

    camRotation.fromAngles(camAngles);
    cam.setRotation(camRotation);
  }

  public void setRotation(float x, float y, float z) {
    camAngles[0] = x;
    camAngles[1] = y;
    camAngles[2] = z;
    camRotation.fromAngles(camAngles);
    cam.setRotation(camRotation);
  }

  public void setRotation(Quaternion rotation) {
    camRotation.set(rotation);
    cam.setRotation(camRotation);
  }

  public void lookAt(Vector3f pos, Vector3f worldUpVector) {

    Vector3f newDirection = new Vector3f();
    Vector3f newUp = new Vector3f();
    Vector3f newLeft = new Vector3f();

    newDirection.set(pos).subtractLocal(cam.getLocation()).normalizeLocal();

    newUp.set(worldUpVector).normalizeLocal();
    if (newUp.equals(Vector3f.ZERO)) {
      newUp.set(Vector3f.UNIT_Y);
    }

    newLeft.set(newUp).crossLocal(newDirection).normalizeLocal();
    if (newLeft.equals(Vector3f.ZERO)) {
      if (newDirection.x != 0) {
        newLeft.set(newDirection.y, -newDirection.x, 0f);
      } else {
        newLeft.set(0f, newDirection.z, -newDirection.y);
      }
    }

    newUp.set(newDirection).crossLocal(newLeft).normalizeLocal();

    Quaternion rotation = new Quaternion().fromAxes(newLeft, newUp, newDirection);
    // rotation.normalizeLocal();
    //vars.release();

    // setRotation(rotation);
    float[] angles = rotation.toAngles(null);
    setRotation(angles[0], angles[1], angles[2]);

    // onFrameChange();

  }

  public void setLocation(Vector3f location) {
    cam.setLocation(location);
  }

  public void setLocation(float x, float y, float z) {
    setLocation(new Vector3f(x, y, z));
  }

  private void zoomCamera(float amount) {
    Vector3f camLoc = cam.getLocation();
    Vector3f movement = cam.getDirection().mult(amount);
    Vector3f newLoc = camLoc.add(movement);
    cam.setLocation(newLoc);
  }

  public void onAnalog(String name, float value, float tpf) {
    if (cam == null || !isEnabled() || !cameraActivated) {
      return;
    }

    switch (name) {
      case ROTATE_DOWN:
        rotateCamera(0.0F, value * rotateSpeed);
        break;
      case ROTATE_UP:
        rotateCamera(0.0F, -value * rotateSpeed);
        break;
      case ROTATE_LEFT:
        rotateCamera(value * rotateSpeed, 0.0F);
        break;
      case ROTATE_RIGHT:
        rotateCamera(-value * rotateSpeed, 0.0F);
        break;
      case ZOOM_IN:
        //zoomCamera(value * zoomSpeed * zoomSpeed);
        zoomCamera(value);
        break;
      case ZOOM_OUT:
        // zoomCamera(-value * zoomSpeed * zoomSpeed);
        zoomCamera(-value);
        break;
    }
  }

  public void qwertyMapping(InputManager inputManager) {
    inputManager.addMapping(ROTATE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(ROTATE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(ROTATE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(ROTATE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

    inputManager.addMapping(CAMERA_ACTIVATED, new MouseButtonTrigger(1));

    inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping(MOVE_DOWN, new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping(MOVE_UP, new KeyTrigger(KeyInput.KEY_E));
    inputManager.addMapping(ZOOM_IN, new KeyTrigger(KeyInput.KEY_E));
    inputManager.addMapping(ZOOM_OUT, new KeyTrigger(KeyInput.KEY_E));

  }

  public void azertyMapping(InputManager inputManager) {
    inputManager.addMapping(ROTATE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(ROTATE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(ROTATE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(ROTATE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

    inputManager.addMapping(CAMERA_ACTIVATED, new MouseButtonTrigger(1));

    inputManager.addMapping(MOVE_FORWARD, new KeyTrigger(KeyInput.KEY_Z));
    inputManager.addMapping(MOVE_BACKWARD, new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping(MOVE_LEFT, new KeyTrigger(KeyInput.KEY_Q));
    inputManager.addMapping(MOVE_RIGHT, new KeyTrigger(KeyInput.KEY_D));
    inputManager.addMapping(MOVE_DOWN, new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping(MOVE_UP, new KeyTrigger(KeyInput.KEY_E));
    inputManager.addMapping(ZOOM_IN, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
    inputManager.addMapping(ZOOM_OUT, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));

  }
}
