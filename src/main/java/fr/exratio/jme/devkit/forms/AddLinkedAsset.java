package fr.exratio.jme.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.asset.ModelKey;
import com.jme3.scene.AssetLinkNode;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.registration.spatial.AssetLinkNodeRegistrar.AssetLinkNodeTreeNode;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.swing.ComponentUtilities;
import java.awt.Insets;
import java.awt.Window;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class AddLinkedAsset extends JPanel {

  private final EditorJmeApplication editorJmeApplication;
  private final SceneTreeService sceneTreeService;

  private JPanel rootPanel;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JList<String> modelsList;
  private JButton addLinkedAssetButton;

  public AddLinkedAsset(final AssetLinkNodeTreeNode assetLinkNodeTreeNode,
      String assetRootDirectory,
      EditorJmeApplication editorJmeApplication,
      SceneTreeService sceneTreeService) {
    initComponents();
    this.editorJmeApplication = editorJmeApplication;
    this.sceneTreeService = sceneTreeService;

    List<Path> modelFiles = null;

    try {
      modelFiles = Files
          .walk(new File(assetRootDirectory).toPath())
          .filter(p -> p.toString().endsWith(".j3o"))
          .collect(Collectors.toList());

    } catch (IOException e) {
      e.printStackTrace();
    }

    if (modelFiles != null) {

      DefaultListModel<String> listModel = new DefaultListModel<>();

      for (Path path : modelFiles) {

        String relativePath = path.toString()
            .replace(DevKitConfig.getInstance().getAssetRootDir(), "");

        // remove any trailing slashes.
        if (relativePath.startsWith("/")) {
          relativePath = relativePath.substring(1);
        }

        listModel.addElement(relativePath);
      }

      modelsList.setModel(listModel);
    }

    addLinkedAssetButton.addActionListener(e -> {

      int[] indices = modelsList.getSelectedIndices();

      if (indices != null && indices.length > 0) {

        // disable the window
        ComponentUtilities.enableComponents(rootPanel, false);

        // then run this "later" so the GUI can display the "disabled" view now.
        SwingUtilities.invokeLater(() -> {

          // get the list of models in the AWT thread.
          final List<String> modelPaths = new ArrayList<>();
          for (int index : indices) {
            modelPaths.add(modelsList.getModel().getElementAt(index));
          }

          editorJmeApplication.enqueue(() -> {

            AssetLinkNode assetLinkNode = assetLinkNodeTreeNode.getUserObject();

            // add all the assets on the JME thread.
            for (String assetPath : modelPaths) {
              assetLinkNode.addLinkedChild(new ModelKey(assetPath));
            }

            assetLinkNode.attachLinkedChildren(editorJmeApplication.getAssetManager());

            // close the window on the AWT thread.
            SwingUtilities.invokeLater(() -> {

              // ServiceManager.getService(SceneTreeService.class).reloadTree();
              this.sceneTreeService.reloadTreeNode(assetLinkNodeTreeNode);

              JButton button = (JButton) e.getSource();
              Window window = SwingUtilities.getWindowAncestor(button);
              window.dispose();
            });

          });

          // we need to create a context menu that allows the user to add more linked children.
          // assetLinkNode.addLinkedChild(new ModelKey(String path));
          // assetLinkNode.attachLinkedChildren(engineService.getAssetManager());

          // we need to override the delete menuItem to remove the linked item.
          // assetLinkNode.removeLinkedChild();

        });

      }

    });

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    var scrollPane1 = new JScrollPane();
    modelsList = new JList();
    addLinkedAssetButton = new JButton();

    //======== this ========
    setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView(modelsList);
    }
    add(scrollPane1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));

    //---- addLinkedAssetButton ----
    addLinkedAssetButton.setText("Add Linked Asset");
    add(addLinkedAssetButton, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
