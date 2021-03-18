package fr.exratio.jme.devkit.service;

import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.SceneGraphVisitorAdapter;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import com.jme3.scene.instancing.InstancedNode;
import com.jme3.shader.VarType;
import fr.exratio.jme.devkit.event.ControlCreatedEvent;
import fr.exratio.jme.devkit.event.ControlRemovedEvent;
import fr.exratio.jme.devkit.event.LightCreatedEvent;
import fr.exratio.jme.devkit.event.LightRemovedEvent;
import fr.exratio.jme.devkit.event.SpatialCreatedEvent;
import fr.exratio.jme.devkit.event.SpatialRemovedEvent;

public class SceneGraphService implements Service {

  JmeEngineService engineService;


  public SceneGraphService() {
    engineService = ServiceManager.getService(JmeEngineService.class);
  }

  /**
   * Adds a spatial to the tree and scene. All spatials should be added to the scene using this
   * method. This should only be called from the AWT thread. The spatial should not be added to the
   * scene yet. Fire a @see SpatialCreatedEvent on creation.
   *
   * @param spatial the spatial to add.
   * @param parentNode the treeNode to add the spatial.
   */
  public void addSpatial(Spatial spatial, Node parentNode) {

    // traverse all the way back to beginning to check if this a child of an instancedNode.
    // if it is we need to set "UseInstancing" to "true" before we add it.
    // the parent will be in the scene already, so we need to touch it in the JME thread.

    engineService.enqueue(() -> {

      Node parent = parentNode == null ? engineService.getRootNode() : parentNode;

      if (isNodeInstanced(parent)) {

        SceneGraphVisitorAdapter adapter = new SceneGraphVisitorAdapter() {

          @Override
          public void visit(Geometry geom) {

            Material material = geom.getMaterial();
            material.getMaterialDef()
                .addMaterialParam(VarType.Boolean, "UseInstancing", true);
            material.setBoolean("UseInstancing", true);

          }

        };

        spatial.breadthFirstTraversal(adapter);
      }

      // attach the spatial on the JME thread. The spatial no longer belongs to AWT at this point.
      parent.attachChild(spatial);

    });
    ServiceManager.getService(EventService.class)
        .post(new SpatialCreatedEvent(spatial));
  }

  private boolean isNodeInstanced(Node parent) {
    Node parentLocal = parent.clone(true);
    while (parentLocal != null) {
      if (parentLocal instanceof InstancedNode) {
        return true;
      }
      parentLocal = parentLocal.getParent();
    }
    return false;
  }

  /**
   * Adds a light to the tree and scene. All lights should be added to the scene using this method.
   * This should only be called from the AWT thread. The light should not be added to the scene yet.
   * Fire a @see LightCreatedEvent on creation.
   *
   * @param light the light to add.
   * @param parentNode the treeNode to add the spatial.
   */
  public void addLight(Light light, Spatial parentNode) {

    // attach the light on the JME thread. The light no longer belongs to AWT at this point.
    engineService.enqueue(() -> {

      parentNode.addLight(light);

      // Set the various configurations on the JME thread.
      // For example a point light needs a position so we position it at the camera position.
      // The camera is on the JME thread, so we can only set its position now (which is as early as possible)
      // because this is the earliest time they are both on the same thread.

      if (light instanceof PointLight) {
        ((PointLight) light).setPosition(engineService.getCamera().getLocation());
      }

    });
    ServiceManager.getService(EventService.class)
        .post(new LightCreatedEvent(parentNode, light));
  }

  /**
   * Adds a control to the tree and scene. All controls should be added to the scene using this
   * method. This should only be called from the AWT thread. The control should not be added to the
   * scene yet. Fire a @see ControlCreatedEvent on creation.
   *
   * @param control the control to add.
   * @param parentNode the treeNode to add the control.
   */
  public void addControl(Control control, Spatial parentNode) {

    // attach the light on the JME thread. The light no longer belongs to AWT at this point.
    engineService.enqueue(() -> parentNode.addControl(control));
    ServiceManager.getService(EventService.class)
        .post(new ControlCreatedEvent(control));
  }

  /**
   * Removes the selected scene spatial from the tree and scene. This method **must** be called from
   * the AWT thread. Fire a @see SpatialRemovedEvent on creation.
   *
   * @param spatial the spatial to remove.
   */
  public void removeSpatial(Spatial spatial) {

    ServiceManager.getService(JmeEngineService.class).enqueue(() ->
        spatial.removeFromParent()
    );

    ServiceManager.getService(EventService.class)
        .post(new SpatialRemovedEvent(spatial));
  }

  /**
   * Removes the selected scene light from the graph scene. This method **must** be called from the
   * AWT thread. Fire a @see LightRemovedEvent on creation.
   *
   * @param light The light to remove
   * @param parent The spatial that holds the light.
   */
  public void removeLight(Light light, Spatial parent) {

    ServiceManager.getService(JmeEngineService.class).enqueue(() -> parent.removeLight(light));

    ServiceManager.getService(EventService.class)
        .post(new LightRemovedEvent(light));
  }

  /**
   * Removes the selected control from the tree and scene. This method **must** be called from the
   * AWT thread. Fire a @see ControlRemovedEvent on creation.
   *
   * @param control The Control to remove
   * @param parent The Spatial that holds the light.
   */
  public void removeTreeNode(Control control, Spatial parent) {
    engineService.enqueue(() -> {
      parent.removeControl(control);
    });

    ServiceManager.getService(EventService.class)
        .post(new ControlRemovedEvent(control));
  }

  @Override
  public long getThreadId() {
    return -1;
  }

  @Override
  public void stop() {

  }
}
