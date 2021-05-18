package fr.exratio.jme.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.swing.ComponentUtilities;
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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AddModels extends JPanel {

  private JPanel rootPanel;
  private final SceneGraphService sceneGraphService;
  private final EditorJmeApplication editorJmeApplication;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JButton addModelsButton;
  private JList<String> modelsList;

  @Autowired
  public AddModels(SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication) {
    initComponents();
    this.sceneGraphService = sceneGraphService;
    this.editorJmeApplication = editorJmeApplication;

    List<Path> modelFiles = null;

    try {
      modelFiles = Files
          .walk(new File(DevKitConfig.getInstance().getAssetRootDir()).toPath())
          .filter(p -> p.toString().endsWith(".j3o"))
          .collect(Collectors.toList());

    } catch (IOException e) {
      e.printStackTrace();
    }

    //TODO : this relativisation may be simplify or move in utils. already done in apache commons?
    if (modelFiles != null) {

      DefaultListModel<String> listModel = new DefaultListModel<>();

      for (Path path : modelFiles) {

        String relativePath = path.toString()
            .replace(DevKitConfig.getInstance().getAssetRootDir(), "");

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

          for (int index : indices) {

            String assetPath = modelsList.getModel().getElementAt(index);

            Spatial model = editorJmeApplication.getAssetManager().loadModel(assetPath);

            sceneGraphService.add(model, (Node) sceneGraphService.getSelectedObject());

          }

          JButton button = (JButton) e.getSource();
          Window window = SwingUtilities.getWindowAncestor(button);
          window.dispose();

        });

      }

    });

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    addModelsButton = new JButton();
    var hSpacer1 = new Spacer();
    var scrollPane1 = new JScrollPane();
    modelsList = new JList();

    //======== this ========
    setLayout(new GridLayoutManager(2, 2, new Insets(10, 10, 10, 10), -1, -1));

    //---- addModelsButton ----
    addModelsButton.setText("Add Selected Model(s)");
    add(addModelsButton, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(hSpacer1, new GridConstraints(1, 1, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        null, null, null));

    //======== scrollPane1 ========
    {

      //---- modelsList ----
      modelsList.putClientProperty("List.isFileList", true);
      scrollPane1.setViewportView(modelsList);
    }
    add(scrollPane1, new GridConstraints(0, 0, 1, 2,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW
            | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
