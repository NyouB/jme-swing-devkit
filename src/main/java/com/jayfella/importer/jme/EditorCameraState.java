package com.jayfella.importer.jme;

import com.jme3.app.Application;
import com.jme3.app.state.BaseAppState;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

import java.util.logging.Logger;

public class EditorCameraState extends BaseAppState implements AnalogListener, ActionListener {

    private static final Logger log = Logger.getLogger(EditorCameraState.class.getName());

    private Camera cam;

    // Location and rotation are positioned away from 0,0,0 because if the user imports a model they will most likely
    // be inside it and won't see it. To avoid confusion we'll move away by default.
    // We move in the +Z direction (away/back) so the +X axis points to the right as one would expect.
    // We move slightly "up" so that the grid is visually apparent.

    private final float[] camAngles = new float[] { 0, 0, 0 };
    private final Quaternion camRotation = new Quaternion();

    // set when onDisable called, read when onEnable called.
    private final Vector3f cameraLocation = new Vector3f(0, 1, -15);

    private float panSpeed = 10.0F;
    private float rotateSpeed = 5.0F;
    private float zoomSpeed = 10.0F;

    private boolean lmb_pressed;
    private boolean mmb_pressed;
    private boolean rmb_pressed;
    private boolean key_forward;
    private boolean key_back;
    private boolean key_left;
    private boolean key_right;
    private boolean key_up;
    private boolean key_down;


    public EditorCameraState() {
    }

    public float getPanSpeed() {
        return this.panSpeed;
    }

    public void setPanSpeed(float panSpeed) {
        this.panSpeed = panSpeed;
    }

    public float getRotateSpeed() {
        return this.rotateSpeed;
    }

    public void setRotateSpeed(float rotateSpeed) {
        this.rotateSpeed = rotateSpeed;
    }

    public float getZoomSpeed() {
        return this.zoomSpeed;
    }

    public void setZoomSpeed(float zoomSpeed) {
        this.zoomSpeed = zoomSpeed;
    }

    protected void initialize(Application app) {
        this.cam = app.getCamera();
    }

    public Camera getCamera() {
        return this.cam;
    }

    private void addMappings() {

        InputManager inputManager = this.getApplication().getInputManager();
        inputManager.addMapping("MouseAxisX", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("MouseAxisX-", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("MouseAxisY", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping("MouseAxisY-", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("MouseWheel", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping("MouseWheel-", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));

        inputManager.addMapping("MouseButtonLeft", new MouseButtonTrigger(0));
        inputManager.addMapping("MouseButtonMiddle", new MouseButtonTrigger(2));
        inputManager.addMapping("MouseButtonRight", new MouseButtonTrigger(1));

        inputManager.addMapping("Key_Forward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("Key_Backward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("Key_Left", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("Key_Right", new KeyTrigger(KeyInput.KEY_D));

        inputManager.addMapping("Key_Down", new KeyTrigger(KeyInput.KEY_Q));
        inputManager.addMapping("Key_Up", new KeyTrigger(KeyInput.KEY_E));

        inputManager.addListener(this,
                "MouseAxisX", "MouseAxisY",
                "MouseAxisX-", "MouseAxisY-",
                "MouseWheel", "MouseWheel-",
                "MouseButtonLeft", "MouseButtonMiddle", "MouseButtonRight",
                "Key_Forward", "Key_Backward", "Key_Left", "Key_Right",
                "Key_Down", "Key_Up");

        log.info("Registered Keyboard Mappings");
    }

    private void removeMappings() {

        InputManager inputManager = this.getApplication().getInputManager();

        inputManager.deleteMapping("MouseAxisX");
        inputManager.deleteMapping("MouseAxisX-");

        inputManager.deleteMapping("MouseAxisY");
        inputManager.deleteMapping("MouseAxisY-");

        inputManager.deleteMapping("MouseWheel");
        inputManager.deleteMapping("MouseWheel-");

        inputManager.deleteMapping("MouseButtonLeft");
        inputManager.deleteMapping("MouseButtonMiddle");
        inputManager.deleteMapping("MouseButtonRight");

        inputManager.deleteMapping("Key_Forward");
        inputManager.deleteMapping("Key_Backward");
        inputManager.deleteMapping("Key_Left");
        inputManager.deleteMapping("Key_Right");

        inputManager.deleteMapping("Key_Down");
        inputManager.deleteMapping("Key_Up");

        inputManager.removeListener(this);

        log.info("Unregistered Keyboard Mappings");
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
        this.lmb_pressed = this.rmb_pressed = this.mmb_pressed = false;
        this.key_forward = this.key_back = this.key_left = this.key_right = false;
        this.key_up = this.key_down = false;
        this.removeMappings();

        // store the existing location so we can re-set it.
        this.cameraLocation.set(getApplication().getCamera().getLocation());
    }

    @Override
    public void update(float tpf) {
        if (this.cam != null) {
            if (this.lmb_pressed || this.mmb_pressed || this.rmb_pressed) {
                if (this.key_forward) {
                    this.zoomCamera(tpf * this.zoomSpeed);
                }

                if (this.key_back) {
                    this.zoomCamera(-tpf * this.zoomSpeed);
                }

                if (this.key_left) {
                    this.panCamera(tpf * this.panSpeed, 0.0F);
                }

                if (this.key_right) {
                    this.panCamera(-tpf * this.panSpeed, 0.0F);
                }

                if (this.key_up) {
                    this.panCamera(0.0F, tpf * this.panSpeed);
                }

                if (this.key_down) {
                    this.panCamera(0.0F, -tpf * this.panSpeed);
                }
            }

        }
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {
        if (this.cam != null) {
            if (this.isEnabled()) {
                if (name.equals("MouseButtonLeft")) {
                    this.lmb_pressed = isPressed;
                }

                if (name.equals("MouseButtonMiddle")) {
                    this.mmb_pressed = isPressed;
                }

                if (name.equals("MouseButtonRight")) {
                    this.rmb_pressed = isPressed;
                }

                if (name.equals("Key_Forward")) {
                    this.key_forward = isPressed;
                }

                if (name.equals("Key_Backward")) {
                    this.key_back = isPressed;
                }

                if (name.equals("Key_Left")) {
                    this.key_left = isPressed;
                }

                if (name.equals("Key_Right")) {
                    this.key_right = isPressed;
                }

                if (name.equals("Key_Up")) {
                    this.key_up = isPressed;
                }

                if (name.equals("Key_Down")) {
                    this.key_down = isPressed;
                }

            }
        }
    }

    private void panCamera(float left, float up) {
        Vector3f leftVec = this.cam.getLeft().mult(left);
        Vector3f upVec = this.cam.getUp().mult(up);
        Vector3f camLoc = this.cam.getLocation().add(leftVec).add(upVec);
        this.cam.setLocation(camLoc);
    }

    private void rotateCamera(float x, float y) {
        float[] var10000 = this.camAngles;
        var10000[0] += x;
        var10000 = this.camAngles;
        var10000[1] += y;
        float maxRotX = 1.553343F;
        if (this.camAngles[0] < -maxRotX) {
            this.camAngles[0] = -maxRotX;
        }

        if (this.camAngles[0] > maxRotX) {
            this.camAngles[0] = maxRotX;
        }

        if (this.camAngles[1] > 6.2831855F) {
            var10000 = this.camAngles;
            var10000[1] -= 6.2831855F;
        } else if (this.camAngles[1] < -6.2831855F) {
            var10000 = this.camAngles;
            var10000[1] += 6.2831855F;
        }

        this.camRotation.fromAngles(this.camAngles);
        this.cam.setRotation(this.camRotation);
    }

    public void setRotation(float x, float y, float z) {
        this.camAngles[0] = x;
        this.camAngles[1] = y;
        this.camAngles[2] = z;
        this.camRotation.fromAngles(this.camAngles);
        this.cam.setRotation(this.camRotation);
    }

    public void setRotation(Quaternion rotation) {
        this.camRotation.set(rotation);
        this.cam.setRotation(this.camRotation);
    }

    public void setLocation(Vector3f location) {
        this.cam.setLocation(location);
    }

    public void setLocation(float x, float y, float z) {
        this.setLocation(new Vector3f(x, y, z));
    }

    private void zoomCamera(float amount) {
        Vector3f camLoc = this.cam.getLocation();
        Vector3f movement = this.cam.getDirection().mult(amount);
        Vector3f newLoc = camLoc.add(movement);
        this.cam.setLocation(newLoc);
    }

    public void onAnalog(String name, float value, float tpf) {
        if (this.cam != null) {
            if (this.isEnabled()) {
                byte var5 = -1;
                switch(name.hashCode()) {
                    case -1690062569:
                        if (name.equals("MouseWheel-")) {
                            var5 = 5;
                        }
                        break;
                    case -1321280686:
                        if (name.equals("MouseAxisX")) {
                            var5 = 0;
                        }
                        break;
                    case -1321280685:
                        if (name.equals("MouseAxisY")) {
                            var5 = 2;
                        }
                        break;
                    case -1301444138:
                        if (name.equals("MouseWheel")) {
                            var5 = 4;
                        }
                        break;
                    case 1989971739:
                        if (name.equals("MouseAxisX-")) {
                            var5 = 1;
                        }
                        break;
                    case 1989971770:
                        if (name.equals("MouseAxisY-")) {
                            var5 = 3;
                        }
                }

                switch(var5) {
                    case 0:
                        if (this.lmb_pressed || this.rmb_pressed) {
                            this.rotateCamera(0.0F, -value * this.rotateSpeed);
                        }

                        if (this.mmb_pressed) {
                            this.panCamera(value * this.panSpeed, 0.0F);
                        }
                        break;
                    case 1:
                        if (this.lmb_pressed || this.rmb_pressed) {
                            this.rotateCamera(0.0F, value * this.rotateSpeed);
                        }

                        if (this.mmb_pressed) {
                            this.panCamera(-value * this.panSpeed, 0.0F);
                        }
                        break;
                    case 2:
                        if (this.lmb_pressed) {
                            this.zoomCamera(value * this.zoomSpeed * this.zoomSpeed);
                        }

                        if (this.rmb_pressed) {
                            this.rotateCamera(-value * this.rotateSpeed, 0.0F);
                        }

                        if (this.mmb_pressed) {
                            this.panCamera(0.0F, -value * this.panSpeed);
                        }
                        break;
                    case 3:
                        if (this.lmb_pressed) {
                            this.zoomCamera(-value * this.zoomSpeed * this.zoomSpeed);
                        }

                        if (this.rmb_pressed) {
                            this.rotateCamera(value * this.rotateSpeed, 0.0F);
                        }

                        if (this.mmb_pressed) {
                            this.panCamera(0.0F, value * this.panSpeed);
                        }
                        break;
                    case 4:
                        this.zoomCamera(value);
                        break;
                    case 5:
                        this.zoomCamera(-value);
                }

            }
        }
    }
}
