package com.jayfella.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jayfella.devkit.config.DevKitConfig;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.scene.Spatial;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class SaveSpatial {

  private JTextField assetPathTextField;
  private JTextField spatialNameTextField;
  private JButton browseAssetDirButton;
  private JButton saveButton;
  private JPanel rootPanel;

  private final Spatial spatial;

  public SaveSpatial(Spatial spatial) {

    this.spatial = spatial;

    browseAssetDirButton.addActionListener(e -> {

      // String projectRoot = System.getProperty("user.dir");
      String assetRoot = DevKitConfig.getInstance().getProjectConfig().getAssetRootDir();

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
            .get(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(), dir, name)
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
                  + file.toString()
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
            .replace(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(), "");

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

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    rootPanel = new JPanel();
    rootPanel.setLayout(new GridLayoutManager(4, 4, new Insets(10, 10, 10, 10), -1, -1));
    final JLabel label1 = new JLabel();
    label1.setText("Asset Directory");
    rootPanel.add(label1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    final Spacer spacer1 = new Spacer();
    rootPanel.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
    final Spacer spacer2 = new Spacer();
    rootPanel.add(spacer2, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0,
        false));
    final JLabel label2 = new JLabel();
    label2.setText("Name");
    rootPanel.add(label2,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    assetPathTextField = new JTextField();
    rootPanel.add(assetPathTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    spatialNameTextField = new JTextField();
    rootPanel.add(spatialNameTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
    browseAssetDirButton = new JButton();
    browseAssetDirButton.setText("Browse...");
    rootPanel.add(browseAssetDirButton,
        new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
            GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
    rootPanel.add(panel1,
        new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    saveButton = new JButton();
    saveButton.setText("Save");
    panel1.add(saveButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer3 = new Spacer();
    panel1.add(spacer3, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return rootPanel;
  }


}
