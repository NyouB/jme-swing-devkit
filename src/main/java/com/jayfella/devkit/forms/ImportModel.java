package com.jayfella.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.swing.ComponentUtilities;
import com.simsilica.jmec.Convert;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class ImportModel {
    private JTextField modelPathTextField;
    private JTextField assetPathTextField;
    private JButton browseModelButton;
    private JButton browseAssetPathButton;
    private JButton importModelButton;
    private JPanel rootPanel;

    public ImportModel() {

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

            String assetRoot = DevKitConfig.getInstance().getProjectConfig().getAssetRootDir();

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
                            .replace(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(), ""));
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
                convert.setTargetRoot(new File(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir()));
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
                        DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(),
                        assetPathTextField.getText(),
                        sourceModel.getName() + ".j3o");

                Path extRemoved = Paths.get(
                        DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(),
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

                String result = extRemoved.toString().replace(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(), "");

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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        rootPanel = new JPanel();
        rootPanel.setLayout(new GridLayoutManager(4, 3, new Insets(10, 10, 10, 10), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("Model");
        rootPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Asset Path");
        rootPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        modelPathTextField = new JTextField();
        rootPanel.add(modelPathTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, -1), new Dimension(150, -1), null, 0, false));
        assetPathTextField = new JTextField();
        rootPanel.add(assetPathTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(250, -1), new Dimension(150, -1), null, 0, false));
        browseModelButton = new JButton();
        browseModelButton.setText("Browse...");
        rootPanel.add(browseModelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        browseAssetPathButton = new JButton();
        browseAssetPathButton.setText("Browse...");
        rootPanel.add(browseAssetPathButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        rootPanel.add(spacer1, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 2, new Insets(10, 0, 0, 0), -1, -1));
        rootPanel.add(panel1, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        importModelButton = new JButton();
        importModelButton.setText("Import Model");
        panel1.add(importModelButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel1.add(spacer2, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
    }

    /**
     */
    public JComponent $$$getRootComponent$$$() {
        return rootPanel;
    }

}
