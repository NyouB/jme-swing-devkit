package devkit.appstate.tool;

import com.jme3.app.Application;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.appstate.annotations.DevKitAppState;

@DevKitAppState
public class SpatialToolState extends SpatialTool {

    public enum Tool { Move, Rotate, Scale }

    private SpatialTool moveTool = new SpatialMoveToolState();
    private SpatialTool rotateTool = new SpatialRotateToolState();
    private SpatialTool scaleTool = new SpatialScaleToolState();

    private Tool activeTool;
    private SpatialTool activeToolState;

    public SpatialToolState() {
    }

    public void setTool(Tool tool) {

        if (activeTool == tool) {
             return;
        }

        if (activeToolState != null) {
            activeToolState.setEnabled(false);
        }

        Spatial spatial = activeToolState.getSpatial();

        activeTool = tool;

        switch (tool) {
            case Move: activeToolState = moveTool; break;
            case Rotate: activeToolState = rotateTool; break;
            case Scale: activeToolState = scaleTool; break;
        }

        activeToolState.setEnabled(true);
        activeToolState.setSpatial(spatial);
    }

    @Override
    protected void initialize(Application app) {
        activeTool = Tool.Move;
        activeToolState = moveTool;
        getStateManager().attachAll(moveTool, rotateTool, scaleTool);
        rotateTool.setEnabled(false);
        scaleTool.setEnabled(false);
        activeToolState.setEnabled(true);

    }

    @Override
    protected void cleanup(Application app) {

    }

    @Override
    protected void onEnable() {
        getStateManager().attach(moveTool);
        getStateManager().attach(rotateTool);
        getStateManager().attach(scaleTool);

        if (activeToolState != null) {
            activeToolState.setEnabled(true);
            activeToolState.setSpatial(activeToolState.getSpatial());
        }

    }

    @Override
    protected void onDisable() {
        getStateManager().detach(moveTool);
        getStateManager().detach(rotateTool);
        getStateManager().detach(scaleTool);
    }

    public void setSpatial(Spatial spatial) {
        activeToolState.setSpatial(spatial);
    }

    @Override
    public Spatial getSpatial() {
        return activeToolState.getSpatial();
    }

    @Override
    public boolean isBusy() {
        return activeToolState.isBusy();
    }

    @Override
    public void update(float tpf) {
        //getState(EditorCameraState.class).setEnabled(!isBusy());
    }

}
