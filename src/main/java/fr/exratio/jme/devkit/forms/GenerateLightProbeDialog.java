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
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.swing.ComponentUtilities;
import fr.exratio.jme.devkit.swing.NumberFormatters;
import java.awt.Insets;
import java.awt.Window;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Generates a LightProbe from a user-selected Node as an environment map. Note: LightProbeFactory
 * only allows selecting nodes. You can't select a geometry (possible bug?).
 */
public class GenerateLightProbeDialog extends JPanel {

  private JPanel rootPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JComboBox areaTypeComboBox;
  private JFormattedTextField radiusTextField;
  private JButton generateButton;
  private JButton cancelButton;

  public GenerateLightProbeDialog(
      EditorJmeApplication editorJmeApplication,
      SceneGraphService sceneGraphService) {
    initComponents();

    DefaultComboBoxModel<AreaType> areaTypeDefaultComboBoxModel = new DefaultComboBoxModel<>();
    for (AreaType type : AreaType.values()) {
      areaTypeDefaultComboBoxModel.addElement(type);
    }
    areaTypeComboBox.setModel(areaTypeDefaultComboBoxModel);
    areaTypeComboBox.setSelectedItem(AreaType.OrientedBox);

    generateButton.addActionListener(e -> {

      // things we need in the JME thread.
      final Node selectedSceneNode = (Node) sceneGraphService.getSelectedObject();
      final AreaType selectedAreaType = (AreaType) areaTypeComboBox.getSelectedItem();
      final float radius = (float) radiusTextField.getValue();

      // disable the dialog
      ComponentUtilities.enableComponents(rootPanel, false);

      editorJmeApplication.enqueue(() -> {

        Vector3f cameraLocation = editorJmeApplication.getCamera().getLocation();

        EnvironmentCamera environmentCamera = editorJmeApplication.getStateManager()
            .getState(EnvironmentCamera.class);
        environmentCamera.setPosition(cameraLocation);

        LightProbeFactory
            .makeProbe(environmentCamera, selectedSceneNode, new JobProgressListener<>() {
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
                  sceneGraphService.add(result, (Spatial) sceneGraphService.getSelectedObject());

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

  private void createUIComponents() {
    // TODO: place custom component creation code here
    radiusTextField = new JFormattedTextField(
        NumberFormatters.createFloatFormatter(1.0f, Float.parseFloat(Integer.MAX_VALUE + ".00f")));
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    var label1 = new JLabel();
    areaTypeComboBox = new JComboBox();
    var label2 = new JLabel();
    var panel1 = new JPanel();
    generateButton = new JButton();
    cancelButton = new JButton();
    var hSpacer1 = new Spacer();
    var label3 = new JLabel();

    //======== this ========
    setLayout(new GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), -1, -1));

    //---- label1 ----
    label1.setText("Area Type");
    add(label1, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(areaTypeComboBox, new GridConstraints(1, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label2 ----
    label2.setText("Radius");
    label2.setToolTipText("The area of effect of this light probe in world units.");
    add(label2, new GridConstraints(2, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- radiusTextField ----
    radiusTextField.setText("100.00");
    add(radiusTextField, new GridConstraints(2, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //======== panel1 ========
    {
      panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));

      //---- generateButton ----
      generateButton.setText("Generate");
      panel1.add(generateButton, new GridConstraints(0, 1, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));

      //---- cancelButton ----
      cancelButton.setText("Cancel");
      panel1.add(cancelButton, new GridConstraints(0, 2, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(hSpacer1, new GridConstraints(0, 0, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
          GridConstraints.SIZEPOLICY_CAN_SHRINK,
          null, null, null));
    }
    add(panel1, new GridConstraints(3, 0, 1, 2,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        null, null, null));

    //---- label3 ----
    label3.setText(
        "<html>\nSelect the Node that will be used to generate the environment.<br />\nThe probe will be positioned at the camera position.\n</html>");
    add(label3, new GridConstraints(0, 0, 1, 2,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
