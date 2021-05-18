package fr.exratio.jme.devkit.forms;

import com.google.common.collect.ImmutableList;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.asset.ModelKey;
import fr.exratio.jme.devkit.action.RemoveLinkedAssetAction;
import fr.exratio.jme.devkit.registration.spatial.AssetLinkNodeRegistrar.AssetLinkNodeTreeNode;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.Insets;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveLinkedAsset extends JPanel {

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private JList modelKeysList;
  private JButton removeLinkedAssetsButton;

  @Autowired
  public RemoveLinkedAsset(
      RemoveLinkedAssetAction removeLinkedAssetAction,
      EditorJmeApplication editorJmeApplication,
      SceneGraphService sceneGraphService) {
    initComponents();

    editorJmeApplication.enqueue(() -> {
      final ImmutableList<ModelKey> modelKeys = ImmutableList
          .copyOf(((AssetLinkNodeTreeNode) sceneGraphService
              .getSelectedObject()).getUserObject().getAssetLoaderKeys());
      SwingUtilities.invokeLater(() -> {
        DefaultListModel<ModelKey> listModel = new DefaultListModel<>();

        for (ModelKey key : modelKeys) {
          listModel.addElement(key);
        }

        modelKeysList.setModel(listModel);
      });

    });

    removeLinkedAssetsButton.setAction(removeLinkedAssetAction);

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    var scrollPane1 = new JScrollPane();
    modelKeysList = new JList();
    removeLinkedAssetsButton = new JButton();

    //======== this ========

    setLayout(new GridLayoutManager(2, 1, new Insets(10, 10, 10, 10), -1, -1));

    //======== scrollPane1 ========
    {
      scrollPane1.setViewportView(modelKeysList);
    }
    add(scrollPane1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));

    //---- removeLinkedAssetsButton ----
    removeLinkedAssetsButton.setText("Remove Selected Linked Assets");
    add(removeLinkedAssetsButton, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
