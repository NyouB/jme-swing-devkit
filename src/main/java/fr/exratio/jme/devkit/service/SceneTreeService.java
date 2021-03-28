package fr.exratio.jme.devkit.service;

import com.google.common.eventbus.Subscribe;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.LightProbe;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.event.ControlCreatedEvent;
import fr.exratio.jme.devkit.event.ControlRemovedEvent;
import fr.exratio.jme.devkit.event.LightCreatedEvent;
import fr.exratio.jme.devkit.event.LightRemovedEvent;
import fr.exratio.jme.devkit.event.SpatialCreatedEvent;
import fr.exratio.jme.devkit.event.SpatialNameChangedEvent;
import fr.exratio.jme.devkit.event.SpatialRemovedEvent;
import fr.exratio.jme.devkit.jme.SceneObjectHighlighterState;
import fr.exratio.jme.devkit.main.MainPage.Zone;
import fr.exratio.jme.devkit.registration.Registrar;
import fr.exratio.jme.devkit.registration.spatial.GeometryRegistrar;
import fr.exratio.jme.devkit.registration.spatial.NodeRegistrar;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorTool;
import fr.exratio.jme.devkit.tool.Tool;
import fr.exratio.jme.devkit.tool.ViewMode;
import fr.exratio.jme.devkit.tree.JmeTreeNode;
import fr.exratio.jme.devkit.tree.SceneTreeMouseListener;
import fr.exratio.jme.devkit.tree.TreeConstants;
import fr.exratio.jme.devkit.tree.control.ControlTreeNode;
import fr.exratio.jme.devkit.tree.event.SceneTreeItemChangedEvent;
import fr.exratio.jme.devkit.tree.light.LightTreeNode;
import fr.exratio.jme.devkit.tree.spatial.GeometryTreeNode;
import fr.exratio.jme.devkit.tree.spatial.MeshTreeNode;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.tree.spatial.SpatialTreeNode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides a jMonkey Scene Tree visualised in a Swing JTree. This implementation does not reflect
 * any changes made by outside sources.
 */
public class SceneTreeService extends Tool implements Service {

  public static final String WINDOW_ID = "scene-tree";
  public static final String TITLE = "Project";
  private static final Logger LOGGER = LoggerFactory.getLogger(SceneTreeService.class);
  private JTree tree;
  private final long threadId = Thread.currentThread().getId();
  // These are "fake" nodes. They are added to their counterparts so we don't see things like "statsappstate".
  // These nodes are created, queried and modified on the JME thread ONLY.
  private Node guiNode;
  private Node rootNode;
  // Our "root" nodes. These nodes are not deletable.
  private NodeTreeNode guiNodeTreeNode;
  // private final SceneObjectHighlighter sceneObjectHighlighter = new SceneObjectHighlighter();
  private NodeTreeNode rootNodeTreeNode;

  private final Map<Object, DefaultMutableTreeNode> objectToNodeMap = new HashMap<>();

  public SceneTreeService() {
    super(SceneTreeService.class.getName(), TITLE, null, Zone.LEFT_TOP, ViewMode.PIN, true);
    initialize();
  }

  @Builder(builderMethodName = "treeBuilder")
  public SceneTreeService(String id, String title, Icon icon,
      Zone zone, ViewMode viewMode,
      boolean isDisplayed) {
    super(id, title, icon, zone, viewMode, isDisplayed);
    initialize();
  }

  private void initialize() {
    this.tree = new JTree();
    add(new JScrollPane(tree), BorderLayout.CENTER);
    tree.setMinimumSize(new Dimension(100, 500));
    tree.setPreferredSize(new Dimension(200, 500));
    DefaultTreeCellRenderer renderer =
        new DefaultTreeCellRenderer();
    renderer.setLayout(new FlowLayout(FlowLayout.LEFT));
    tree.setCellRenderer(renderer);

    // The tree root is never visible.
    DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Root");

    tree.addMouseListener(new SceneTreeMouseListener());
    tree.setRootVisible(false);
    tree.setModel(new DefaultTreeModel(treeRoot));

    final JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

    engineService.enqueue(() -> {

      // create the nodes on the JME thread.
      // These are "fake" nodes. They are added to their counterparts so we don't see things like "StatsAppState".
      guiNode = new Node("Gui Node");
      rootNode = new Node("Root Node");

      // put the "not deletable" in the "root" nodes so we can reject deleting them.
      guiNode.setUserData(TreeConstants.UNDELETABLE_FLAG, TreeConstants.UNDELETABLE_FLAG);
      rootNode.setUserData(TreeConstants.UNDELETABLE_FLAG, TreeConstants.UNDELETABLE_FLAG);

      // put the "root node" flag in the "root" nodes so we know when we've hit our fake nodes.
      rootNode.setUserData(TreeConstants.TREE_ROOT, TreeConstants.TREE_ROOT);
      guiNode.setUserData(TreeConstants.TREE_ROOT, TreeConstants.TREE_ROOT);

      // Attach them in the JME thread.
      engineService.getGuiNode().attachChild(guiNode);
      engineService.getRootNode().attachChild(rootNode);

      SwingUtilities.invokeLater(() -> {

        // create the TreeItems on the Swing thread.
        guiNodeTreeNode = new NodeTreeNode(guiNode);
        rootNodeTreeNode = new NodeTreeNode(rootNode);

        objectToNodeMap.put(guiNode, guiNodeTreeNode);
        objectToNodeMap.put(rootNode, rootNodeTreeNode);

        treeRoot.add(guiNodeTreeNode);
        treeRoot.add(rootNodeTreeNode);

        // update the tree to reflect any changes made.
        reloadTree();
      });

    });

    tree.getSelectionModel().addTreeSelectionListener(e -> {

      TreePath[] paths = tree.getSelectionPaths();
      SceneObjectHighlighterState highlighterState = engineService.getStateManager()
          .getState(SceneObjectHighlighterState.class);

      // highlighting
      // remove and rebuild all highlights.
      // we could probably not be so aggressive and remove items that are no longer selected.
      // lets just get this working for now.
      engineService.enqueue(highlighterState::removeAllHighlights);

      if (paths != null) {

        // Property Inspector.
        // We can only inspect one thing at a time, so choose the last selected object.
        DefaultMutableTreeNode lastSelectedTreeNode = (DefaultMutableTreeNode) paths[
            paths.length
                - 1].getLastPathComponent();

        ServiceManager.getService(PropertyInspectorTool.class)
            .inspect(lastSelectedTreeNode.getUserObject());

        // highlighting
        for (TreePath path : paths) {
          DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path
              .getLastPathComponent();

          if (treeNode != null) {

            if (treeNode.getUserObject() instanceof Spatial) {

              engineService
                  .enqueue(() -> highlighterState
                      .highlight((Spatial) treeNode.getUserObject()));

            } else if (treeNode.getUserObject() instanceof Mesh) {

              GeometryTreeNode parent = (GeometryTreeNode) treeNode.getParent();
              engineService.enqueue(
                  () -> highlighterState.highlightMesh(parent.getUserObject()));


            } else if (treeNode.getUserObject() instanceof LightProbe) {

              engineService
                  .enqueue(() -> highlighterState
                      .highlight((Light) treeNode.getUserObject()));

            } else {
              highlighterState.removeAllHighlights();
            }
          } else {
            highlighterState.removeAllHighlights();
          }

        }

        // fire an event that the scene tree item has changed.
        if (lastSelectedTreeNode instanceof JmeTreeNode) {
          ServiceManager.getService(EventService.class)
              .post(
                  new SceneTreeItemChangedEvent((JmeTreeNode) lastSelectedTreeNode));
        }
      }

    });

    // register our listener
    ServiceManager.getService(EventService.class).register(this);


  }


  /**
   * Returns the "fake" RootNode as opposed to the "real" rootNode. Note that this node is attached
   * to the JME scene and should be inspected or modified on the JME thread.
   *
   * @return the Scene Tree RootNode
   */
  public Node getRootNode() {
    return rootNode;
  }

  /**
   * Returns the root node tree element.
   *
   * @return the root node tree element.
   */
  public NodeTreeNode getRootNodeTreeNode() {
    return rootNodeTreeNode;
  }

  /**
   * Returns the "fake" GuiNode as opposed to the "real" guiNode. Note that this node is attached to
   * the JME scene and should be inspected or modified on the JME thread.
   *
   * @return the Scene Tree GuiNode
   */
  public Node getGuiNode() {
    return guiNode;
  }

  /**
   * Returns the gui node tree element.
   *
   * @return the gui node tree element.
   */
  public NodeTreeNode getGuiNodeTreeNode() {
    return guiNodeTreeNode;
  }

  /**
   * Returns the currently selected TreeNode in the scene tree.
   *
   * @return the currently selected TreeNode in the scene tree.
   */
  public JmeTreeNode getSelectedTreeNode() {

    TreePath[] paths = tree.getSelectionPaths();
    if (paths != null) {

      // We can only inspect one thing at a time, so choose the last selected object.
      DefaultMutableTreeNode lastSelectedTreeNode = (DefaultMutableTreeNode) paths[
          paths.length - 1]
          .getLastPathComponent();

      if (lastSelectedTreeNode instanceof JmeTreeNode) {
        return (JmeTreeNode) lastSelectedTreeNode;
      }
    }

    return null;
  }

  @Subscribe
  public void onSpatialCreatedEvent(SpatialCreatedEvent event) {
    Spatial spatial = event.getSpatial();
    Spatial parent = spatial.getParent() == null ? rootNode : spatial.getParent();
    if (spatial == null) {
      LOGGER.warn(
          "-- onSpatialCreatedEvent() the spatial contained in the event is null. Doing nothing");
      return;
    }
    SpatialTreeNode newNode = createSpatialTreeNodeFrom(spatial);
    objectToNodeMap.put(spatial, newNode);
    objectToNodeMap.get(parent).add(newNode);
  }

  @Subscribe
  public void onControlCreatedEvent(ControlCreatedEvent event) {
    Control control = event.getControl();
    Spatial parent = event.getParent() == null ? rootNode : event.getParent();
    if (control == null) {
      LOGGER.warn(
          "-- onControlCreatedEvent() the control contained in the event is null. Doing nothing");
      return;
    }
    ControlTreeNode newNode = new ControlTreeNode(control);
    objectToNodeMap.put(control, newNode);
    objectToNodeMap.get(parent).add(newNode);
  }

  @Subscribe
  public void onLightCreatedEvent(LightCreatedEvent event) {
    Light light = event.getLight();
    Spatial parent = event.getParent() == null ? rootNode : event.getParent();
    if (light == null) {
      LOGGER.warn(
          "-- onLightCreatedEvent() the light contained in the event is null. Doing nothing");
      return;
    }
    LightTreeNode newNode = LightTreeNode.fromLight(light);
    objectToNodeMap.put(light, newNode);
    objectToNodeMap.get(parent).add(newNode);
  }

  @Subscribe
  public void onSpatialRemovedEvent(SpatialRemovedEvent event) {
    Spatial spatial = event.getSpatial();
    if (spatial == null) {
      LOGGER.warn(
          "-- onSpatialRemovedEvent() the spatial contained in the event is null. Doing nothing");
      return;
    }
    objectToNodeMap.get(spatial).removeFromParent();
  }

  @Subscribe
  public void onControlRemovedEvent(ControlRemovedEvent event) {
    Control control = event.getControl();
    if (control == null) {
      LOGGER.warn(
          "-- onControlRemovedEvent() the control contained in the event is null. Doing nothing");
      return;
    }
    objectToNodeMap.get(control).removeFromParent();
  }

  @Subscribe
  public void onLightRemovedEvent(LightRemovedEvent event) {
    Light light = event.getLight();
    if (light == null) {
      LOGGER.warn(
          "-- onLightRemovedEvent() the light contained in the event is null. Doing nothing");
      return;
    }
    objectToNodeMap.get(light).removeFromParent();
  }


  /**
   * Removes the selected scene spatial from the tree and scene. This method **must** be called from
   * the AWT thread. Fire a @see SpatialRemovedEvent on creation.
   *
   * @param spatialTreeNode the treeNode to remove.
   */
  public void removeTreeNode(SpatialTreeNode spatialTreeNode) {
    ServiceManager.getService(SceneGraphService.class)
        .removeSpatial(spatialTreeNode.getUserObject());

  }

  /**
   * Removes the selected scene light from the tree and scene. This method **must** be called from
   * the AWT thread. Fire a @see LightRemovedEvent on creation.
   *
   * @param lightTreeNode The lightTreeNode to remove
   * @param parent The SpatialTreeNode that holds the light.
   */
  public void removeTreeNode(LightTreeNode lightTreeNode, SpatialTreeNode parent) {

    lightTreeNode.removeFromParent();

    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

      Spatial parentNode = parent.getUserObject();
      Light light = lightTreeNode.getUserObject();

      parentNode.removeLight(light);

      // reload the tree to reflect the changes made.
      SwingUtilities.invokeLater(() -> reloadTreeNode(parent));

    });

    ServiceManager.getService(EventService.class)
        .post(new LightRemovedEvent(lightTreeNode.getUserObject()));
  }

  /**
   * Removes the selected control from the tree and scene. This method **must** be called from the
   * AWT thread. Fire a @see ControlRemovedEvent on creation.
   *
   * @param controlTreeNode The ControlTreeNode to remove
   * @param parent The SpatialTreeNode that holds the light.
   */
  public void removeTreeNode(ControlTreeNode controlTreeNode, SpatialTreeNode parent) {

    controlTreeNode.removeFromParent();

    ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

      Spatial parentNode = parent.getUserObject();
      Control control = controlTreeNode.getUserObject();

      parentNode.removeControl(control);

      // reload the tree to reflect the changes made.
      SwingUtilities.invokeLater(() -> reloadTreeNode(parent));

    });

    ServiceManager.getService(EventService.class)
        .post(new ControlRemovedEvent(controlTreeNode.getUserObject()));
  }

  /**
   * Walks through the given treeNode and queries the UserObject (a SceneNode) to add any children
   * recursively.
   *
   * @param treeNode the parent TreeNode to query.
   */
  private void ensureTreeEqualGraph(SpatialTreeNode treeNode) {

    if (treeNode.getUserObject() instanceof Node) {

      Node node = (Node) treeNode.getUserObject();

      for (Spatial nodeChild : node.getChildren()) {

        SpatialTreeNode childTreeNode = createSpatialTreeNodeFrom(nodeChild);

        if (childTreeNode != null) {

          treeNode.add(childTreeNode);
          ensureTreeEqualGraph(childTreeNode);

        }

      }
    } else if (treeNode.getUserObject() instanceof Geometry) {

      MeshTreeNode childTreeNode = new MeshTreeNode(
          ((Geometry) treeNode.getUserObject()).getMesh());
      treeNode.add(childTreeNode);

    }

    // lights...
    LightList lights = treeNode.getUserObject().getLocalLightList();
    for (Light light : lights) {
      treeNode.add(LightTreeNode.fromLight(light));
    }

    // controls...
    int controlCount = treeNode.getUserObject().getNumControls();
    for (int i = 0; i < controlCount; i++) {
      Control control = treeNode.getUserObject().getControl(i);
      treeNode.add(new ControlTreeNode(control));
    }

  }

  /**
   * Creates a treeNode from the given JME scene object.
   *
   * @param spatial a JME Spatial.
   * @return a TreeNode with the given object as the userObject.
   */
  private SpatialTreeNode createSpatialTreeNodeFrom(Spatial spatial) {

    RegistrationService registrationService = ServiceManager
        .getService(RegistrationService.class);
    JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

    SpatialTreeNode treeNode = null;

    if (spatial instanceof Node) {

      Node node = (Node) spatial;
      Registrar<NodeRegistrar> nodeRegistrar = registrationService.getNodeRegistration();

      for (NodeRegistrar registrar : nodeRegistrar.getRegistrations()) {
        if (registrar.getRegisteredClass().equals(node.getClass())) {
          treeNode = (SpatialTreeNode) registrar.createSceneTreeNode(node, engineService);
          break;
        }
      }

      if (treeNode == null) {
        LOGGER.trace("No TreeNode associated with object: {}, using default NodeTreeNode.",
            spatial.getClass());
        treeNode = new NodeTreeNode(node);
      }
    } else if (spatial instanceof Geometry) {

      Geometry geometry = (Geometry) spatial;
      Registrar<GeometryRegistrar> geometryRegistrar = registrationService
          .getGeometryRegistration();

      for (GeometryRegistrar registrar : geometryRegistrar.getRegistrations()) {
        if (registrar.getRegisteredClass().equals(geometry.getClass())) {
          treeNode = (SpatialTreeNode) registrar
              .createSceneTreeNode(geometry, engineService);
          break;
        }
      }

      if (treeNode == null) {
        LOGGER.trace("No TreeNode associated with object: {}, using default GeometryTreeNode.",
            spatial.getClass());
        treeNode = new GeometryTreeNode(geometry);
      }

    }

    if (treeNode == null) {
      LOGGER.warn("Unable to create SpatialTreeNode from object: {}", spatial.getClass());
    }

    return treeNode;
  }

  /**
   * Reloads the scene JTree to reflect any changes made.
   */
  public void reloadTree() {
    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
    treeModel.reload();
  }

  public void reloadTreeNode(TreeNode treeNode) {
    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
    treeModel.reload(treeNode);
  }

  public void updateTreeNodeRepresentation(TreeNode treeNode) {
    DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
    treeModel.nodeChanged(treeNode);
  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {
    ServiceManager.getService(EventService.class).unregister(this);
  }

  /**
   * Called whenever a spatial name changed, so the visual text can be updated.
   *
   * @param event the event fired.
   */
  public void onSpatialNameChangedEvent(SpatialNameChangedEvent event) {

    // if the spatial name changed, it must be the selected treeNode.
    SpatialTreeNode spatialTreeNode = (SpatialTreeNode) tree.getLastSelectedPathComponent();
    updateTreeNodeRepresentation(spatialTreeNode);

  }

  public JTree getTree() {
    return tree;
  }

  public DefaultMutableTreeNode jmeObjectToNode(Object object) {
    return objectToNodeMap.get(object);
  }


}
