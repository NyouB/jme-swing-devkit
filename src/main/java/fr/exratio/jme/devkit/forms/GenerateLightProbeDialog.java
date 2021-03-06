package fr.exratio.jme.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.environment.EnvironmentCamera;
import com.jme3.environment.LightProbeFactory;
import com.jme3.environment.generation.JobProgressListener;
import com.jme3.light.LightProbe;
import com.jme3.light.LightProbe.AreaType;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.service.JmeEngineService;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.swing.ComponentUtilities;
import fr.exratio.jme.devkit.swing.NumberFormatters;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.tree.spatial.SpatialTreeNode;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Window;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * Generates a LightProbe from a user-selected Node as an environment map. Note: LightProbeFactory
 * only allows selecting nodes. You can't select a geometry (possible bug?).
 */
public class GenerateLightProbeDialog {

  private JPanel rootPanel;
  private JTree sceneTree;
  private JComboBox<AreaType> areaTypeComboBox;
  private JFormattedTextField radiusTextField;
  private JButton generateButton;
  private JButton cancelButton;

  public GenerateLightProbeDialog(final SpatialTreeNode spatialTreeNode) {

    $$$setupUI$$$();

    sceneTree.setRootVisible(false);
    recurseSceneForNodes();

    DefaultComboBoxModel<AreaType> areaTypeDefaultComboBoxModel = new DefaultComboBoxModel<>();
    for (AreaType type : AreaType.values()) {
      areaTypeDefaultComboBoxModel.addElement(type);
    }
    areaTypeComboBox.setModel(areaTypeDefaultComboBoxModel);
    areaTypeComboBox.setSelectedItem(AreaType.OrientedBox);

    generateButton.addActionListener(e -> {

      SpatialTreeNode selectedNode = (SpatialTreeNode) sceneTree.getLastSelectedPathComponent();

      // things we need in the JME thread.
      final Node selectedSceneNode = (Node) selectedNode.getUserObject();
      final AreaType selectedAreaType = (AreaType) areaTypeComboBox.getSelectedItem();
      final float radius = (float) radiusTextField.getValue();

      // disable the dialog
      ComponentUtilities.enableComponents(rootPanel, false);

      JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

      engineService.enqueue(() -> {

        Vector3f cameraLocation = engineService.getCamera().getLocation();

        EnvironmentCamera environmentCamera = engineService.getStateManager()
            .getState(EnvironmentCamera.class);
        environmentCamera.setPosition(cameraLocation);

        LightProbeFactory
            .makeProbe(environmentCamera, selectedSceneNode, new JobProgressListener<LightProbe>() {
              @Override
              public void start() {

              }

              @Override
              public void step(String message) {

              }

              @Override
              public void progress(double value) {

              }

              @Override
              public void done(final LightProbe result) {

                result.setAreaType(selectedAreaType);
                result.getArea().setRadius(radius);
                result.setPosition(cameraLocation);

                SwingUtilities.invokeLater(() -> {

                  // add the light to the treenode we selected when we cliked "generate lightprobe".
                  ServiceManager.getService(SceneTreeService.class)
                      .addLight(result, spatialTreeNode);

                  JButton button = (JButton) e.getSource();
                  Window window = SwingUtilities.getWindowAncestor(button);
                  window.dispose();

                });

              }
            });

      });

    });

    cancelButton.addActionListener(e -> {
      JButton button = (JButton) e.getSource();
      Window window = SwingUtilities.getWindowAncestor(button);
      window.dispose();
    });

  }

  private void recurseSceneForNodes() {

    // create the tree on the JME thread, then pass it to swing.
    SceneTreeService sceneTreeService = ServiceManager.getService(SceneTreeService.class);

    // NodeTreeNode treeRoot = new NodeTreeNode(sceneTreeService.getRootNode());
    DefaultMutableTreeNode treeRoot = new DefaultMutableTreeNode("Root");
    sceneTree.setModel(new DefaultTreeModel(treeRoot));

    NodeTreeNode guiNode = new NodeTreeNode(sceneTreeService.getGuiNode());
    NodeTreeNode rootNode = new NodeTreeNode(sceneTreeService.getRootNode());

    treeRoot.add(guiNode);
    treeRoot.add(rootNode);

    reloadTree();

    recurse(guiNode);
    recurse(rootNode);

  }

  private void recurse(SpatialTreeNode spatialTreeNode) {

    JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);

    if (spatialTreeNode instanceof NodeTreeNode) {

      // query the node on the JME thread.
      engineService.enqueue(() -> {

        Node node = (Node) spatialTreeNode.getUserObject();
        List<Spatial> children = node.getChildren();

        for (Spatial child : children) {

          if (child instanceof Node) {
            // add nodes on the AWT thread.
            SwingUtilities.invokeLater(() -> {

              NodeTreeNode childNode = new NodeTreeNode((Node) child);
              spatialTreeNode.add(childNode);

              // its a bit annoying, but we don't really have a way of determining when this is finished
              // since we jump from thread to thread, so we'll just update the tree each time we
              // add something.
              reloadTree();

              // recurse on the AWT thread.
              recurse(childNode);
            });
          }

        }

      });

    }

  }

  /**
   * Reloads the scene JTree to reflect any changes made.
   */
  private void reloadTree() {
    DefaultTreeModel treeModel = (DefaultTreeModel) sceneTree.getModel();
    treeModel.reload();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    rootPanel = new JPanel();
    rootPanel.setLayout(new GridLayoutManager(5, 2, new Insets(10, 10, 10, 10), -1, -1));
    final JScrollPane scrollPane1 = new JScrollPane();
    rootPanel.add(scrollPane1,
        new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    sceneTree = new JTree();
    scrollPane1.setViewportView(sceneTree);
    final JLabel label1 = new JLabel();
    label1.setText("Area Type");
    rootPanel.add(label1,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    areaTypeComboBox = new JComboBox();
    rootPanel.add(areaTypeComboBox, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    label2.setText("Radius");
    label2.setToolTipText("The area of effect of this light probe in world units.");
    rootPanel.add(label2,
        new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    radiusTextField.setText("100.00");
    rootPanel.add(radiusTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    rootPanel.add(panel1,
        new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    generateButton = new JButton();
    generateButton.setText("Generate");
    panel1.add(generateButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    cancelButton = new JButton();
    cancelButton.setText("Cancel");
    panel1.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
    final JLabel label3 = new JLabel();
    label3.setText(
        "<html>\nSelect the Node that will be used to generate the environment.<br />\nThe probe will be positioned at the camera position.\n</html>");
    rootPanel.add(label3,
        new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return rootPanel;
  }


  private void createUIComponents() {
    // TODO: place custom component creation code here
    radiusTextField = new JFormattedTextField(
        NumberFormatters.createFloatFormatter(1.0f, Float.parseFloat(Integer.MAX_VALUE + ".00f")));
  }
}
