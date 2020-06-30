package com.jayfella.importer.service;

import com.jayfella.importer.jme.SceneObjectHighlighterState;
import com.jayfella.importer.tree.MeshTreeNode;
import com.jayfella.importer.tree.SceneTreeMouseListener;
import com.jayfella.importer.tree.light.*;
import com.jayfella.importer.tree.spatial.GeometryTreeNode;
import com.jayfella.importer.tree.spatial.NodeTreeNode;
import com.jayfella.importer.tree.spatial.SpatialTreeNode;
import com.jme3.light.*;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.util.logging.Logger;

import static com.jayfella.importer.tree.TreeConstants.UNDELETABLE_FLAG;

/**
 * Provides a jMonkey Scene Tree visualised in a Swing JTree.
 * This implementation does not reflect any changes made by outside sources.
 */
public class SceneTreeService implements Service {

    private static final Logger log = Logger.getLogger(SceneTreeService.class.getName());

    public static final String WINDOW_ID = "scene tree";

    private final JTree tree;

    // These are "fake" nodes. They are added to their counterparts so we don't see things like "statsappstate".
    // These nodes are created, queried and modified on the JME thread ONLY.
    private Node guiNode;
    private Node rootNode;

    // Our "root" nodes. These nodes are not deletable.
    private NodeTreeNode guiNodeTreeNode;
    private NodeTreeNode rootNodeTreeNode;

    // private final SceneObjectHighlighter sceneObjectHighlighter = new SceneObjectHighlighter();

    public SceneTreeService(JTree tree) {

        this.tree = tree;

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

            // put some arbitrary data in the "root" nodes so we can reject deleting them.
            guiNode.setUserData(UNDELETABLE_FLAG, UNDELETABLE_FLAG);
            rootNode.setUserData(UNDELETABLE_FLAG, UNDELETABLE_FLAG);

            // Attach them in the JME thread.
            engineService.getGuiNode().attachChild(guiNode);
            engineService.getRootNode().attachChild(rootNode);

            SwingUtilities.invokeLater(() -> {

                // create the TreeItems on the Swing thread.
                guiNodeTreeNode = new NodeTreeNode(guiNode);
                rootNodeTreeNode = new NodeTreeNode(rootNode);

                treeRoot.add(guiNodeTreeNode);
                treeRoot.add(rootNodeTreeNode);

                // update the tree to reflect any changes made.
                reloadTree();
            });

        });


        tree.getSelectionModel().addTreeSelectionListener(e -> {

            TreePath[] paths = tree.getSelectionPaths();
            SceneObjectHighlighterState highlighterState = engineService.getStateManager().getState(SceneObjectHighlighterState.class);

            // highlighting
            // remove and rebuild all highlights.
            // we could probably not be so aggressive and remove items that are no longer selected.
            // lets just get this working for now.
            engineService.enqueue(highlighterState::removeAllHighlights);

            if (paths != null) {

                // Property Inspector.
                // We can only inspect one thing at a time, so choose the last selected object.
                DefaultMutableTreeNode lastSelectedTreeNode = (DefaultMutableTreeNode) paths[paths.length - 1].getLastPathComponent();

                ServiceManager.getService(PropertyInspectorService.class).inspect(lastSelectedTreeNode.getUserObject());

                // highlighting
                for (TreePath path : paths) {
                    DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) path.getLastPathComponent();

                    if (treeNode != null) {

                        if (treeNode.getUserObject() instanceof Spatial) {

                            engineService.enqueue(() -> highlighterState.highlight((Spatial) treeNode.getUserObject()));

                        } else if (treeNode.getUserObject() instanceof Mesh) {

                            GeometryTreeNode parent = (GeometryTreeNode) treeNode.getParent();
                            engineService.enqueue(() -> highlighterState.highlightMesh(parent.getUserObject()));


                        }
                        else if (treeNode.getUserObject() instanceof LightProbe) {

                            engineService.enqueue(() -> highlighterState.highlight((Light) treeNode.getUserObject()));

                        }

                        else {
                            highlighterState.removeAllHighlights();
                        }
                    } else {
                        highlighterState.removeAllHighlights();
                    }

                }

            }

        });
    }

    /**
     * Returns the "fake" RootNode as opposed to the "real" rootNode.
     * Note that this node is attached to the JME scene and should be inspected or modified on the JME thread.
     *
     * @return the Scene Tree RootNode
     */
    public Node getRootNode() {
        return rootNode;
    }

    /**
     * Returns the "fake" GuiNode as opposed to the "real" guiNode.
     * Note that this node is attached to the JME scene and should be inspected or modified on the JME thread.
     *
     * @return the Scene Tree GuiNode
     */
    public Node getGuiNode() {
        return guiNode;
    }

    /**
     * Adds a spatial to the tree and scene. All scene objects should be added to the scene using this method.
     * This should only be called from the AWT thread. The spatial should not be added to the scene yet.
     *
     * @param spatial    the spatial to add.
     * @param parentNode the treeNode to add the spatial.
     */
    public void addSpatial(Spatial spatial, NodeTreeNode parentNode) {

        SpatialTreeNode newNode = createSpatialTreeNodeFrom(spatial);

        if (newNode != null) {

            parentNode.add(newNode);

            // the spatial is still on the AWT thread at this point, so we are safe to traverse it.
            traverseSceneGraph(newNode);

            // update the tree to reflect any changes made.
            reloadTree();

            // attach the spatial on the JME thread. The spatial no longer belongs to AWT at this point.
            ServiceManager.getService(JmeEngineService.class).enqueue(() -> {
                parentNode.getUserObject().attachChild(spatial);
            });

        }
    }

    /**
     * Adds a light to the tree and scene. All scene objects should be added to the scene using this method.
     * This should only be called from the AWT thread. The light should not be added to the scene yet.
     *
     * @param light      the light to add.
     * @param parentNode the treeNode to add the spatial.
     */
    public void addLight(Light light, SpatialTreeNode parentNode) {

        LightTreeNode newNode = createLightTreeNodeFrom(light);

        if (newNode != null) {
            parentNode.add(newNode);

            // update the tree to reflect any changes made.
            reloadTree();

            JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

            // attach the light on the JME thread. The light no longer belongs to AWT at this point.
            engineService.enqueue(() -> {

                parentNode.getUserObject().addLight(light);

                // Set the various configurations on the JME thread.
                // For example a point light needs a position so we position it at the camera position.
                // The camera is on the JME thread, so we can only set its position now (which is as early as possible)
                // because this is the earliest time they are both on the same thread.

                if (light instanceof PointLight) {
                    ((PointLight) light).setPosition(engineService.getCamera().getLocation());
                }

            });
        }

    }

    /**
     * Removes the selected scene spatial from the tree and scene.
     * This method **must** be called from the AWT thread.
     *
     * @param spatialTreeNode the treeNode to remove.
     */
    public void removeTreeNode(SpatialTreeNode spatialTreeNode) {

        spatialTreeNode.removeFromParent();

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

            Spatial spatial = spatialTreeNode.getUserObject();
            spatial.removeFromParent();

            // reload the tree to reflect the changes made.
            SwingUtilities.invokeLater(this::reloadTree);

        });

    }

    /**
     * Removes the selected scene light from the tree and scene.
     * This method **must** be called from the AWT thread.
     *
     * @param lightTreeNode The lightTreeNode to remove
     * @param parent        The SpatialTreeNode that holds the light.
     */
    public void removeTreeNode(LightTreeNode lightTreeNode, SpatialTreeNode parent) {

        lightTreeNode.removeFromParent();

        ServiceManager.getService(JmeEngineService.class).enqueue(() -> {

            Spatial parentNode = parent.getUserObject();
            Light light = lightTreeNode.getUserObject();

            parentNode.removeLight(light);

            // reload the tree to reflect the changes made.
            SwingUtilities.invokeLater(this::reloadTree);

        });

    }

    /**
     * Walks through the given treeNode and queries the UserObject (a SceneNode) to add any children recursively.
     *
     * @param treeNode the parent TreeNode to query.
     */
    private void traverseSceneGraph(SpatialTreeNode treeNode) {

        if (treeNode.getUserObject() instanceof Node) {

            Node node = (Node) treeNode.getUserObject();

            for (Spatial nodeChild : node.getChildren()) {

                SpatialTreeNode childTreeNode = createSpatialTreeNodeFrom(nodeChild);

                if (childTreeNode != null) {

                    treeNode.add(childTreeNode);
                    traverseSceneGraph(childTreeNode);

                }

            }
        }
        else if (treeNode.getUserObject() instanceof Geometry) {
            MeshTreeNode childTreeNode = new MeshTreeNode(((Geometry) treeNode.getUserObject()).getMesh());
            treeNode.add(childTreeNode);
        }

    }

    /**
     * Creates a treeNode from the given JME scene object.
     *
     * @param spatial a JME Spatial.
     * @return a TreeNode with the given object as the userObject.
     */
    private SpatialTreeNode createSpatialTreeNodeFrom(Spatial spatial) {

        if (spatial instanceof Node) {
            return new NodeTreeNode( (Node) spatial );
        }
        else if (spatial instanceof Geometry) {
            return new GeometryTreeNode( (Geometry) spatial );
        }

        log.warning("Unable to create SpatialTreeNode from object: " + spatial.getClass());

        return null;
    }

    private LightTreeNode createLightTreeNodeFrom(Light light) {

        if (light instanceof AmbientLight) {
            return new AmbientLightTreeNode((AmbientLight) light);
        }
        else if (light instanceof DirectionalLight) {
            return new DirectionalLightTreeNode((DirectionalLight) light);
        }
        else if (light instanceof LightProbe) {
            return new LightProbeTreeNode((LightProbe) light);
        }
        else if (light instanceof PointLight) {
            return new PointLightTreeNode((PointLight) light);
        }

        log.warning("Unable to create LightTreeNode from object: " + light.getClass());

        return null;
    }

    /**
     * Reloads the scene JTree to reflect any changes made.
     */
    private void reloadTree() {
        DefaultTreeModel treeModel = (DefaultTreeModel) tree.getModel();
        treeModel.reload();
    }

    @Override
    public void stop() {

    }

}
