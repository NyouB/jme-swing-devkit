package devkit.appstate.tool;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.MaterialDef;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;


public class SpatialMoveToolState extends AbstractSpatialToolState implements ActionListener,
    AnalogListener {

  public static final String AXIS_X = "Axis_X";
  public static final String AXIS_Y = "Axis_Y";
  public static final String AXIS_Z = "Axis_Z";
  public static final String COLOR = "Color";
  public static final ColorRGBA LIGHT_RED = new ColorRGBA(1f, 0.5f, 0.5f, 1f);
  public static final ColorRGBA LIGHT_GREEN = new ColorRGBA(0.5f, 1f, 0.5f, 1f);
  public static final ColorRGBA LIGHT_BLUE = new ColorRGBA(0.5f, 0.5f, 1f, 1f);
  private final CollisionResults collisionResults = new CollisionResults();
  private final Ray ray = new Ray();
  private InputManager inputManager;
  private Camera camera;
  private boolean move_x, move_y, move_z;

  private SimpleApplication application;
  private MouseOverAppState mouseHoverAppState;
  private Geometry currentMouseHoverTool;

  public SpatialMoveToolState() {
    super.setEnabled(false);
  }

  @Override
  protected void initialize(Application app) {
    this.application = (SimpleApplication) app;
    this.inputManager = app.getInputManager();
    this.camera = app.getCamera();
    this.mouseHoverAppState = application.getStateManager().getState(MouseOverAppState.class);
    if (toolModel == null) {
      toolModel = (Node) app.getAssetManager().loadModel("Models/SDK/Widget_Translation.j3o");
      MaterialDef materialDef = (MaterialDef) application.getAssetManager()
          .loadAsset("/MatDefs/FogUnshaded.j3md");
      Material redMaterial = new Material(materialDef);
      redMaterial.setColor(COLOR, ColorRGBA.Red);
      toolModel.getChild(AXIS_X).setMaterial(redMaterial);
      Material greenMaterial = new Material(materialDef);
      greenMaterial.setColor(COLOR, ColorRGBA.Green);
      toolModel.getChild(AXIS_Y).setMaterial(greenMaterial);
      Material blueMaterial = new Material(materialDef);
      blueMaterial.setColor(COLOR, ColorRGBA.Blue);
      toolModel.getChild(AXIS_Z).setMaterial(blueMaterial);
      toolModel.setQueueBucket(RenderQueue.Bucket.Transparent);
      toolModel.setShadowMode(RenderQueue.ShadowMode.Off);
    }
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
      case AXIS_X:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, LIGHT_RED);
        break;
      case AXIS_Y:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, LIGHT_GREEN);
        break;
      case AXIS_Z:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, LIGHT_BLUE);
        break;
    }

  }

  private void onMouseExitToolArea() {
    switch (currentMouseHoverTool.getName()) {
      case AXIS_X:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(ColorRGBA.Red);
        break;
      case AXIS_Y:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(ColorRGBA.Green);
        break;
      case AXIS_Z:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(ColorRGBA.Blue);
        break;
    }
    currentMouseHoverTool = null;
  }

  @Override
  protected void cleanup(Application app) {
  }

  @Override
  protected void onEnable() {

    inputManager.addMapping("Move", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

    inputManager.addMapping("MoveAxisX", new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping("MoveAxisX-", new MouseAxisTrigger(MouseInput.AXIS_X, true));

    inputManager.addMapping("MoveAxisY", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping("MoveAxisY-", new MouseAxisTrigger(MouseInput.AXIS_Y, true));

    inputManager.addListener(this,
        "Move",
        "MoveAxisX", "MoveAxisX-",
        "MoveAxisY", "MoveAxisY-"
    );

  }


  @Override
  protected void onDisable() {

    inputManager.deleteMapping("Move");

    inputManager.deleteMapping("MoveAxisX");
    inputManager.deleteMapping("MoveAxisX-");

    inputManager.deleteMapping("MoveAxisY");
    inputManager.deleteMapping("MoveAxisY-");

    inputManager.removeListener(this);

    if (super.toolModel.getParent() != null) {
      super.toolModel.removeFromParent();
    }
  }


  @Override
  public void onAction(String binding, boolean isPressed, float tpf) {

    if (binding.equals("Move") && isPressed) {

      if (!move_x && !move_y && !move_z) {

        move_x = currentMouseHoverTool.getName().equals("Axis_X");
        move_y = currentMouseHoverTool.getName().equals("Axis_Y");
        move_z = currentMouseHoverTool.getName().equals("Axis_Z");

        // System.out.println(String.format("Move: [ %b | %b | %b ]", move_x, move_y, move_z));

        super.busy = true;
        getState(SpatialSelectorState.class).setEnabled(false);
      } else {
        super.busy = false;
        getState(SpatialSelectorState.class).setEnabled(true);
      }

    } else {
      super.busy = false;
      move_x = move_y = move_z = false;
    }

  }

  private boolean isMoving() {
    return move_x || move_y || move_z;
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {

    if (binding.startsWith("MoveAxisX") || binding.startsWith("MoveAxisY")) {

      if (isMoving() && super.selectedSpatial != null) {

        // we're moving something.

        // Vector3f left = selectedSpatial.getLocalRotation().mult(Vector3f.UNIT_X);
        // Vector3f up = selectedSpatial.getLocalRotation().mult(Vector3f.UNIT_Y);
        // Vector3f forward = selectedSpatial.getLocalRotation().mult(Vector3f.UNIT_Z);

        float val = binding.endsWith("-")
            ? value * -1
            : value;

        val *= super.distance;

        if (move_x) {
          super.selectedSpatial.move(val, 0, 0);
        } else if (move_y) {
          super.selectedSpatial.move(0, val, 0);
        } else if (move_z) {
          super.selectedSpatial.move(0, 0, -val);
        }

      }
    }

  }


}
