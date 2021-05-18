package fr.exratio.jme.devkit.forms;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.simsilica.jmec.Convert;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.swing.ComponentUtilities;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImportModel extends JPanel {

  private JPanel rootPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JTextField modelPathTextField;
  private JTextField assetPathTextField;
  private JButton browseModelButton;
  private JButton browseAssetPathButton;
  private JButton importModelButton;

  public ImportModel() {

    initComponents();
    browseModelButton.addActionListener(e -> {

      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
      chooser.setDialogTitle("Select GLTF Model");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setFileFilter(new FileNameExtensionFilter("GLTF Models", "gltf", "glb"));
      chooser.setAcceptAllFileFilterUsed(false);

      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
        modelPathTextField.setText(chooser.getSelectedFile().getAbsolutePath());
      }

    });

    browseAssetPathButton.addActionListener(e -> {

      String assetRoot = DevKitConfig.getInstance().getAssetRootDir();

      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new File(assetRoot));
      chooser.setDialogTitle("Select GLTF Model");
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);

      if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

        String chosenPath = chooser.getSelectedFile().getAbsolutePath();

        if (!chosenPath.contains(assetRoot)) {

          JOptionPane.showMessageDialog(null,
              "The chosen directory must exist inside the asset root directory.",
              "Invalid Directory",
              ERROR_MESSAGE);

        } else {
          assetPathTextField.setText(chooser.getSelectedFile().getAbsolutePath()
              .replace(DevKitConfig.getInstance().getAssetRootDir(), ""));
        }

      }

    });

    importModelButton.addActionListener(e -> {

      String modelPath = modelPathTextField.getText().trim();

      // Prerequisites. Ensure the paths are correct, etc.
      if (modelPath.isEmpty()) {
        return;
      }

      JButton button = (JButton) e.getSource();
      Window window = SwingUtilities.getWindowAncestor(button);

      // disable the window while we import...
      ComponentUtilities.enableComponents(rootPanel, false);

      // do the conversion in another thread.
      new Thread(() -> {

        Convert convert = new Convert();
        File sourceModel = new File(modelPath);
        convert.setSourceRoot(sourceModel.getParentFile());
        convert.setTargetRoot(
            new File(DevKitConfig.getInstance().getAssetRootDir()));
        convert.setTargetAssetPath(assetPathTextField.getText());

        try {
          convert.convert(new File(modelPath));
        } catch (IOException ex) {
          ex.printStackTrace();

          // notify the user on the AWT Thread.
          SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(window,
                "An error occured attempting to import the model:\n" + ex.getMessage(),
                "Import Failed",
                ERROR_MESSAGE);
          });

          return;
        }

        Path resultFile = Paths.get(
            DevKitConfig.getInstance().getAssetRootDir(),
            assetPathTextField.getText(),
            sourceModel.getName() + ".j3o");

        Path extRemoved = Paths.get(
            DevKitConfig.getInstance().getAssetRootDir(),
            assetPathTextField.getText(),
            sourceModel.getName()
                .replace(".glb", "").replace(".GLB", "")
                .replace(".gltf", "").replace(".GLTF", "")
                + ".j3o"
        );

        try {
          Files.move(resultFile, extRemoved);
        } catch (IOException ex) {
          ex.printStackTrace();

          JOptionPane.showMessageDialog(window,
              "An error occurred attempting to rename the model."
                  + System.lineSeparator()
                  + ex.getMessage()
                  + System.lineSeparator()
                  + System.lineSeparator()
                  + "The model appears to have still been imported."
              ,
              "Move error",
              ERROR_MESSAGE
          );

          return;
        }

        String result = extRemoved.toString()
            .replace(DevKitConfig.getInstance().getAssetRootDir(), "");

        // notify the user on the AWT Thread.
        SwingUtilities.invokeLater(() -> {

          JOptionPane.showMessageDialog(window,
              "Model imported successfully to: " + result,
              "Imported Successfully",
              JOptionPane.INFORMATION_MESSAGE);

          // close the window
          window.dispose();

        });

      }).start();

    });
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    var label1 = new JLabel();
    var label2 = new JLabel();
    modelPathTextField = new JTextField();
    assetPathTextField = new JTextField();
    browseModelButton = new JButton();
    browseAssetPathButton = new JButton();
    var vSpacer1 = new Spacer();
    var panel1 = new JPanel();
    importModelButton = new JButton();
    var hSpacer1 = new Spacer();

    //======== this ========
    setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, -1));

    //---- label1 ----
    label1.setText("Model");
    add(label1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- label2 ----
    label2.setText("Asset Path");
    add(label2, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(modelPathTextField, new GridConstraints(0, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(assetPathTextField, new GridConstraints(1, 1, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- browseModelButton ----
    browseModelButton.setText("Browse...");
    add(browseModelButton, new GridConstraints(0, 2, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- browseAssetPathButton ----
    browseAssetPathButton.setText("Browse...");
    add(browseAssetPathButton, new GridConstraints(1, 2, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(vSpacer1, new GridConstraints(3, 1, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));

    //======== panel1 ========
    {
      panel1.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 0, 0), -1, -1));

      //---- importModelButton ----
      importModelButton.setText("Import Model");
      panel1.add(importModelButton, new GridConstraints(0, 0, 1, 1,
          GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
          GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
          GridConstraints.SIZEPOLICY_FIXED,
          null, null, null));
      panel1.add(hSpacer1, new GridConstraints(0, 1, 1, 1,
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
