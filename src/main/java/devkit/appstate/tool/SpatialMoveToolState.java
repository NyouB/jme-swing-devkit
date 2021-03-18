package devkit.appstate.tool;

import com.jme3.app.Application;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;


public class SpatialMoveToolState extends SpatialTool implements ActionListener, AnalogListener {

    private InputManager inputManager;
    private Camera camera;

    private final CollisionResults collisionResults = new CollisionResults();
    private final Ray ray = new Ray();

    private boolean move_x, move_y, move_z;

    public SpatialMoveToolState() {
        super.setEnabled(false);
    }

    @Override
    protected void initialize(Application app) {

        this.inputManager = app.getInputManager();
        this.camera = app.getCamera();

        if (toolModel == null) {
            super.toolModel = (Node) app.getAssetManager().loadModel("Models/SDK/Widget_Translation.j3o");
            super.toolModel.setQueueBucket(RenderQueue.Bucket.Transparent);
            super.toolModel.setShadowMode(RenderQueue.ShadowMode.Off);
        }
    }

    @Override
    protected void cleanup(Application app) { }

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


                Vector2f click2d = inputManager.getCursorPosition();
                Vector3f click3d = camera.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 0f).clone();
                Vector3f dir = camera.getWorldCoordinates(new Vector2f(click2d.x, click2d.y), 1f).subtractLocal(click3d).normalizeLocal();

                ray.setOrigin(click3d);
                ray.setDirection(dir);

                super.toolModel.collideWith(ray, collisionResults);

                if (collisionResults.size() > 0) {

                    Geometry geometry = collisionResults.getClosestCollision().getGeometry();

                    move_x = geometry.getName().equals("Axis_X");
                    move_y = geometry.getName().equals("Axis_Y");
                    move_z = geometry.getName().equals("Axis_Z");

                    // System.out.println(String.format("Move: [ %b | %b | %b ]", move_x, move_y, move_z));

                    collisionResults.clear();

                    super.busy = true;
                    getState(SpatialSelectorState.class).setEnabled(false);
                } else {
                    super.busy = false;
                    getState(SpatialSelectorState.class).setEnabled(true);
                }

            }
        } else {
            super.busy = false;
            move_x = move_y = move_z = false;
        }

    }

    private  boolean isMoving() {
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
                    super.selectedSpatial.move(val, 0 ,0);
                } else if (move_y) {
                    super.selectedSpatial.move(0, val, 0);
                } else if (move_z) {
                    super.selectedSpatial.move(0, 0, -val);
                }

            }
        }

    }



}
