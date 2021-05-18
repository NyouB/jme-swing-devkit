package fr.exratio.jme.devkit.forms;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.config.DevKitConfig;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SaveSpatial extends JPanel {

  private final Spatial spatial;
  private JPanel rootPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JTextField assetPathTextField;
  private JTextField spatialNameTextField;
  private JButton browseAssetDirButton;
  private JButton saveButton;

  public SaveSpatial(Spatial spatial) {

    initComponents();
    this.spatial = spatial;

    browseAssetDirButton.addActionListener(e -> {

      // String projectRoot = System.getProperty("user.dir");
      String assetRoot = DevKitConfig.getInstance().getAssetRootDir();

      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File(assetRoot));
      chooser.setDialogTitle("Select Asset Directory");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);

      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

        String chosenPath = chooser.getSelectedFile().getAbsolutePath();

        if (!chosenPath.contains(assetRoot)) {

          JOptionPane.showMessageDialog(null,
              "The chosen directory must exist inside the projects Asset Root directory.",
              "Invalid Directory",
              ERROR_MESSAGE);

        } else {
          assetPathTextField
              .setText(chooser.getSelectedFile().getAbsolutePath().replace(assetRoot, ""));
        }

      }

    });

    saveButton.addActionListener(e -> {

      String name = spatialNameTextField.getText().trim();

      if (name.isEmpty()) {

        JOptionPane.showMessageDialog(null,
            "You must specify a name.",
            "No Name Specified",
            ERROR_MESSAGE);

      } else {

        String dir = assetPathTextField.getText().trim();

        if (!name.endsWith(".j3o")) {
          name += ".j3o";
        }

        File file = Paths
            .get(DevKitConfig.getInstance().getAssetRootDir(), dir, name)
            .toFile();

        if (file.exists()) {

          int overwriteResponse = JOptionPane.showConfirmDialog(null,
              "This file already exists. Overwrite?",
              "Overwrite File",
              JOptionPane.YES_NO_OPTION);

          // if we didn't explicitly get the YES response, exit now.
          if (overwriteResponse != JOptionPane.YES_OPTION) {
            return;
          }
        }

        try {
          BinaryExporter.getInstance().save(spatial, file);
        } catch (IOException ex) {

          String spatialName = spatial.getName() == null ? "No Name" : spatial.getName();

          String errorString =
              "An error occurred whilst attempting to save the spatial:"
                  + System.lineSeparator()
                  + spatialName
                  + System.lineSeparator()
                  + "to file"
                  + System.lineSeparator()
                  + file
                  + System.lineSeparator()
                  + ex.getMessage();

          JOptionPane.showMessageDialog(null,
              errorString,
              "Save Failed",
              ERROR_MESSAGE);

          return;
        }

        String responseString = "Saved successfully to:"
            + System.lineSeparator()
            + file.toString()
            .replace(DevKitConfig.getInstance().getAssetRootDir(), "");

        JOptionPane.showMessageDialog(null,
            responseString,
            "Spatial Saved",
            JOptionPane.INFORMATION_MESSAGE);

        // close the window.
        JButton button = (JButton) e.getSource();
        Window window = SwingUtilities.getWindowAncestor(button);
        window.dispose();

      }

    });

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    var label1 = new JLabel();
    var hSpacer1 = new Spacer();
    var vSpacer1 = new Spacer();
    var label2 = new JLabel();
    assetPathTextField = new JTextField();
    spatialNameTextField = new JTextField();
    browseAssetDirButton = new JButton();
    var panel1 = new JPanel();
    saveButton = new JButton();
    var hSpacer2 = new Spacer();

    //======== this ========
    setLayout(new GridLayoutManager(4, 4, new Insets(10, 10, 10, 10), -1, -1));

    //---- label1 ----
    label1.setText("Asset Directory");
    add(label1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(hSpacer1, new GridConstraints(0, 3, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        null, null, null));
    add(vSpacer1, new GridConstraints(3, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));

    //---- label2 ----
    label2.setText("Name");
    add(label2, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(assetPathTextField, new GridConstraints(0, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(spatialNameTextField, new GridConstraints(1, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- browseAssetDirButton ----
    browseAssetDirButton.setText("Browse...");
    add(browseAssetDirButton, new GridConstraints(0, 2, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //======== panel1 ========
    {
      panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));

      //---- saveButton ----
      saveButton.setText("Save");
      panel1.add(saveButton, new GridConstraints(0, 0, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(hSpacer2, new GridConstraints(0, 1, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
          GridConstraints.SIZEPOLICY_CAN_SHRINK,
          null, null, null));
    }
    add(panel1, new GridConstraints(2, 1, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
