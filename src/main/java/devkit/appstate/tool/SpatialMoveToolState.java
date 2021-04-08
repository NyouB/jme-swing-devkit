package devkit.appstate.tool;

import com.google.common.eventbus.EventBus;
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
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import fr.exratio.jme.devkit.service.EventService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.inspector.SpatialMoveEvent;
import javax.swing.SwingUtilities;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class SpatialMoveToolState extends AbstractSpatialToolState implements ActionListener,
    AnalogListener {

  public static final String AXIS_X = "Axis_X";
  public static final String AXIS_Y = "Axis_Y";
  public static final String AXIS_Z = "Axis_Z";
  public static final String MOVE = "Move";
  public static final String MOVE_AXIS_X_PLUS = "MoveAxisX";
  public static final String MOVE_AXIS_X_MINUS = "MoveAxisX-";
  public static final String MOVE_AXIS_Y_PLUS = "MoveAxisY";
  public static final String MOVE_AXIS_Y_MINUS = "MoveAxisY-";
  public static final String COLOR = "Color";
  public static final String WIDGET = "Models/SDK/Widget_Translation.j3o";
  public static final String MAT_DEF = "/MatDefs/FogUnshaded.j3md";
  public static final ColorRGBA LIGHT_RED = new ColorRGBA(1f, 0.5f, 0.5f, 1f);
  public static final ColorRGBA LIGHT_GREEN = new ColorRGBA(0.5f, 1f, 0.5f, 1f);
  public static final ColorRGBA LIGHT_BLUE = new ColorRGBA(0.5f, 0.5f, 1f, 1f);
  private InputManager inputManager;
  private boolean move_x, move_y, move_z;

  private SimpleApplication application;
  private MouseOverAppState mouseHoverAppState;
  private Geometry currentMouseHoverTool;

  @Autowired
  public SpatialMoveToolState(EventBus eventBus) {
    super(eventBus);
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
      toolModel.getChild(AXIS_X).setMaterial(redMaterial);
      Material greenMaterial = new Material(materialDef);
      greenMaterial.setColor(COLOR, LIGHT_GREEN);
      toolModel.getChild(AXIS_Y).setMaterial(greenMaterial);
      Material blueMaterial = new Material(materialDef);
      blueMaterial.setColor(COLOR, LIGHT_BLUE);
      toolModel.getChild(AXIS_Z).setMaterial(blueMaterial);
      toolModel.setQueueBucket(Bucket.Translucent);
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
        currentMouseHoverTool.getMaterial().setColor(COLOR, ColorRGBA.Red);
        break;
      case AXIS_Y:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, ColorRGBA.Green);
        break;
      case AXIS_Z:
        currentMouseHoverTool = newMouseHoverGeometry;
        currentMouseHoverTool.getMaterial().setColor(COLOR, ColorRGBA.Blue);
        break;
    }

  }

  private void onMouseExitToolArea() {
    switch (currentMouseHoverTool.getName()) {
      case AXIS_X:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(LIGHT_RED);
        break;
      case AXIS_Y:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(LIGHT_GREEN);
        break;
      case AXIS_Z:
        currentMouseHoverTool.getMaterial().getParam(COLOR).setValue(LIGHT_BLUE);
        break;
    }
    currentMouseHoverTool = null;
  }

  @Override
  protected void cleanup(Application app) {
  }

  @Override
  protected void onEnable() {

    inputManager.addMapping(MOVE, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

    inputManager.addMapping(MOVE_AXIS_X_PLUS, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(MOVE_AXIS_X_MINUS, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(MOVE_AXIS_Y_PLUS, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(MOVE_AXIS_Y_MINUS, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

    inputManager.addListener(this, MOVE, MOVE_AXIS_X_PLUS, MOVE_AXIS_X_MINUS, MOVE_AXIS_Y_PLUS,
        MOVE_AXIS_Y_MINUS
    );

  }


  @Override
  protected void onDisable() {

    inputManager.deleteMapping(MOVE);

    inputManager.deleteMapping(MOVE_AXIS_X_PLUS);
    inputManager.deleteMapping(MOVE_AXIS_X_MINUS);

    inputManager.deleteMapping(MOVE_AXIS_Y_PLUS);
    inputManager.deleteMapping(MOVE_AXIS_Y_MINUS);

    inputManager.removeListener(this);

    if (super.toolModel.getParent() != null) {
      super.toolModel.removeFromParent();
    }
  }


  @Override
  public void onAction(String binding, boolean isPressed, float tpf) {
    if (currentMouseHoverTool == null) {
      disableMoving();
      return;
    }
    if (binding.equals(MOVE) && isPressed) {
      move_x = AXIS_X.equals(currentMouseHoverTool.getName());
      move_y = AXIS_Y.equals(currentMouseHoverTool.getName());
      move_z = AXIS_Z.equals(currentMouseHoverTool.getName());
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

  private boolean isMoving() {
    return move_x || move_y || move_z;
  }

  @Override
  public void onAnalog(String binding, float value, float tpf) {
    if (selectedSpatial == null || !StringUtils
        .equalsAny(binding, MOVE_AXIS_X_PLUS, MOVE_AXIS_Y_PLUS, MOVE_AXIS_X_MINUS,
            MOVE_AXIS_Y_MINUS)) {
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
        super.selectedSpatial.move(val, 0, 0);
      } else if (move_y) {
        super.selectedSpatial.move(0, val, 0);
      } else if (move_z) {
        super.selectedSpatial.move(0, 0, -val);
      }

      SwingUtilities.invokeLater(() -> eventBus.post(new SpatialMoveEvent(selectedSpatial)));


    }

  }


}
