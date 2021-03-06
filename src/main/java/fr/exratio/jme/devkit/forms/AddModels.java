package fr.exratio.jme.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.service.JmeEngineService;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.swing.ComponentUtilities;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class AddModels {

  private JList<String> modelsList;
  private JButton addModelsButton;
  private JPanel rootPanel;

  public AddModels(final NodeTreeNode nodeTreeNode) {

    List<Path> modelFiles = null;

    try {
      modelFiles = Files
          .walk(new File(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir()).toPath())
          .filter(p -> p.toString().endsWith(".j3o"))
          .collect(Collectors.toList());

    } catch (IOException e) {
      e.printStackTrace();
    }

    if (modelFiles != null) {

      DefaultListModel<String> listModel = new DefaultListModel<>();

      for (Path path : modelFiles) {

        String relativePath = path.toString()
            .replace(DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(), "");

        // remove any trailing slashes.
        if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
          relativePath = relativePath.substring(1);
        }

        relativePath = relativePath.replace("\\", "/");

        listModel.addElement(relativePath);
      }

      modelsList.setModel(listModel);
    }

    addModelsButton.addActionListener(e -> {

      int[] indices = modelsList.getSelectedIndices();

      if (indices.length > 0) {

        // disable the window
        ComponentUtilities.enableComponents(rootPanel, false);

        // then run this "later" so the GUI can display the "disabled" view now.
        SwingUtilities.invokeLater(() -> {

          JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);
          SceneTreeService treeService = ServiceManager.getService(SceneTreeService.class);

          for (int index : indices) {

            String assetPath = modelsList.getModel().getElementAt(index);

            Spatial model = engineService.getAssetManager().loadModel(assetPath);

            treeService.addSpatial(model, nodeTreeNode);

          }

          JButton button = (JButton) e.getSource();
          Window window = SwingUtilities.getWindowAncestor(button);
          window.dispose();

        });

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
    rootPanel.setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));
    addModelsButton = new JButton();
    addModelsButton.setText("Add Selected Model(s)");
    rootPanel.add(addModelsButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    rootPanel.add(spacer1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
    final JScrollPane scrollPane1 = new JScrollPane();
    rootPanel.add(scrollPane1,
        new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    modelsList = new JList();
    modelsList.putClientProperty("List.isFileList", Boolean.TRUE);
    scrollPane1.setViewportView(modelsList);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return rootPanel;
  }


}