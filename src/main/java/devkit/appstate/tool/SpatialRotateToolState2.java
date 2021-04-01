package devkit.appstate.tool;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.Spatial.CullHint;
import org.apache.commons.lang3.StringUtils;


public class SpatialRotateToolState2 extends AbstractSpatialToolState implements ActionListener,
    AnalogListener {

  public static final String ROTATE_X = "Rotate_X";
  public static final String ROTATE_Y = "Rotate_Y";
  public static final String ROTATE_Z = "Rotate_Z";
  public static final String MOVE = "Move";
  public static final String MOVE_ROTATE_X_PLUS = "MoveAxisX";
  public static final String MOVE_ROTATE_X_MINUS = "MoveAxisX-";
  public static final String MOVE_ROTATE_Y_PLUS = "MoveAxisY";
  public static final String MOVE_ROTATE_Y_MINUS = "MoveAxisY-";
  public static final String COLOR = "Color";
  public static final String WIDGET = "Models/SDK/Widget_Rotation.j3o";
  public static final String MAT_DEF = "/MatDefs/FogUnshaded.j3md";
  public static final ColorRGBA LIGHT_RED = new ColorRGBA(1f, 0.5f, 0.5f, 1f);
  public static final ColorRGBA LIGHT_GREEN = new ColorRGBA(0.5f, 1f, 0.5f, 1f);
  public static final ColorRGBA LIGHT_BLUE = new ColorRGBA(0.5f, 0.5f, 1f, 1f);
  private InputManager inputManager;
  private boolean move_x, move_y, move_z;

  private SimpleApplication application;
  private MouseOverAppState mouseHoverAppState;
  private Geometry currentMouseHoverTool;

  public SpatialRotateToolState2() {
    super.setEnabled(false);
  }

  @Override
  protected void initialize(Application app) {
    this.application = (SimpleApplication) app;
    this.inputManager = app.getInputManager();
    this.mouseHoverAppState = application.getStateManager().getState(MouseOverAppState.class);
    if (toolModel == null) {
      toolModel = (Node) app.getAssetManager().loadModel(WIDGET);
      MaterialDef materialDef = (MaterialDef) application.getAssetManager()
          .loadAsset(MAT_DEF);
      Material redMaterial = new Material(materialDef);
      redMaterial.setColor(COLOR, LIGHT_RED);
      toolModel.getChild(ROTATE_X).setMaterial(redMaterial);
      Material greenMaterial = new Material(materialDef);
      greenMaterial.setColor(COLOR, LIGHT_GREEN);
      toolModel.getChild(ROTATE_Y).setMaterial(greenMaterial);
      Material blueMaterial = new Material(materialDef);
      blueMaterial.setColor(COLOR, LIGHT_BLUE);
      toolModel.getChild(ROTATE_Z).setMaterial(blueMaterial);
      toolModel.setQueueBucket(RenderQueue.Bucket.Transparent);
      toolModel.setShadowMode(RenderQueue.ShadowMode.Off);
      toolModel.setCullHint(CullHint.Never);
      toolModel.setBatchHint(BatchHint.Never);
    }
  }

  @Override
  protected void onEnable() {

    inputManager.addMapping(MOVE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

    inputManager.addMapping(MOVE_ROTATE_X_PLUS, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(MOVE_ROTATE_X_MINUS, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(MOVE_ROTATE_Y_PLUS, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(MOVE_ROTATE_Y_MINUS, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

    inputManager
        .addListener(this, MOVE, MOVE_ROTATE_X_PLUS, MOVE_ROTATE_X_MINUS, MOVE_ROTATE_Y_PLUS,
            MOVE_ROTATE_Y_MINUS
        );

  }

  @Override
  protected void onDisable() {

    inputManager.deleteMapping(MOVE);

    inputManager.deleteMapping(MOVE_ROTATE_X_PLUS);
    inputManager.deleteMapping(MOVE_ROTATE_X_MINUS);

    inputManager.deleteMapping(MOVE_ROTATE_Y_PLUS);
    inputManager.deleteMapping(MOVE_ROTATE_Y_MINUS);

    inputManager.removeListener(this);

    if (super.toolModel.getParent() != null) {
      super.toolModel.removeFromParent();
    }
  }


  private boolean isMoving() {
    return move_x || move_y || move_z;
  }


  @Override
  public void update(float tpf) {
    super.update(tpf);

    handleGeometryColoring(mouseHoverAppState.getCurrentMouseHoverGeometry());

  }

  private void handleGeometryColoring(Geometry newMouseHoverGeometry) {
    // if the mouse exit the tool area
    if (currentMouseHoverTool != null && !currentMouseHoverTool.equals(newMouseHoverGeometry)) {
      onMouseExitToolArea();
    }

    //if the mouse is over nothing
    if (newMouseHoverGeometry == null || newMouseHoverGeometry.equals(currentMouseHoverTool)) {
      return;
    }

    //if the mouse is enter the tool area
    onMouseEnterToolArea(newMouseHoverGeometry);

  }

  private void onMouseEnterToolArea(Geometry newMouseHoverGeometry) {
    switch (newMouseHoverGeometry.getName()) {
      case ROTATE_X:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, ColorRGBA.Red);
        break;
      case ROTATE_Y:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, ColorRGBA.Green);
        break;
      case ROTATE_Z:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, ColorRGBA.Blue);
        break;
    }

  }

  private void onMouseExitToolArea() {
    switch (currentMouseHoverTool.getName()) {
      case ROTATE_X:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(LIGHT_RED);
        break;
      case ROTATE_Y:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(LIGHT_GREEN);
        break;
      case ROTATE_Z:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(LIGHT_BLUE);
        break;
    }
    currentMouseHoverTool = null;
  }

  @Override
  protected void cleanup(Application app) {
  }


  @Override
  public void onAction(String binding, boolean isPressed, float tpf) {
    if (currentMouseHoverTool == null) {
      disableMoving();
      return;
    }
    if (binding.equals(MOVE) && isPressed) {
      move_x = ROTATE_X.equals(currentMouseHoverTool.getName());
      move_y = ROTATE_Y.equals(currentMouseHoverTool.getName());
      move_z = ROTATE_Z.equals(currentMouseHoverTool.getName());
      if (!move_x && !move_y && !move_z) {
        // System.out.println(String.format("Move: [ %b | %b | %b ]", move_x, move_y, move_z));
        busy = true;
        getState(SpatialSelectorState.class).setEnabled(true);
      } else {
        busy = true;
        getState(SpatialSelectorState.class).setEnabled(false);
      }

    } else {
      disableMoving();
    }

  }

  private void disableMoving() {
    busy = false;
    move_x = move_y = move_z = false;
    getState(SpatialSelectorState.class).setEnabled(true);
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {
    if (selectedSpatial == null || !StringUtils
        .equalsAny(binding, MOVE_ROTATE_X_PLUS, MOVE_ROTATE_Y_PLUS, MOVE_ROTATE_X_MINUS,
            MOVE_ROTATE_Y_MINUS)) {
      return;
    }

    if (isMoving()) {

      // we're moving something.

      // Vector3f left = selectedSpatial.getLocalRotation().mult(Vector3f.UNIT_X);
      // Vector3f up = selectedSpatial.getLocalRotation().mult(Vector3f.UNIT_Y);
      // Vector3f forward = selectedSpatial.getLocalRotation().mult(Vector3f.UNIT_Z);

      float val = binding.endsWith("-")
          ? value * -1
          : value;

      val *= distance;

      if (move_x) {
        selectedSpatial.rotate(val, 0, 0);
      } else if (move_y) {
        selectedSpatial.rotate(0, val, 0);
      } else if (move_z) {
        selectedSpatial.rotate(0, 0, val);
      }


    }

  }


}
