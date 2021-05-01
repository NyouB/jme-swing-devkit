package fr.exratio.jme.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.asset.AssetManager;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import com.jme3.util.SkyFactory;
import com.jme3.util.SkyFactory.EnvMapType;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.jme.TextureImage;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import org.springframework.stereotype.Component;

/**
 * Generates a SkyBox and adds it to the given node.
 */

@Component
public class CreateSkyBoxDialog {

  private JPanel rootPanel;
  private JComboBox<EnvMapType> comboBox1;
  private JButton createButton;
  private JButton cancelButton;
  private JList<String> assetsList;
  private final AssetManager assetManager;
  private final SceneGraphService sceneGraphService;

  public CreateSkyBoxDialog(SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication) {
    this.assetManager = editorJmeApplication.getAssetManager();
    this.sceneGraphService = sceneGraphService;

    populateListWithResources();
    populateComboBoxWithEnvMapTypes();

    createButton.addActionListener(e -> {

      String texturePath = assetsList.getSelectedValue();
      EnvMapType envMapType = (EnvMapType) comboBox1.getSelectedItem();

      if (texturePath == null || envMapType == null) {
        // @TODO: display a dialog explaining why it's not going to happen.
        return;
      }

      // disable the dialog
      ComponentUtilities.enableComponents(rootPanel, false);

      // run this "later" so the disabled effect is visible.
      SwingUtilities.invokeLater(() -> {

        Texture texture = assetManager.loadTexture(texturePath);

        // we're safe calling this from the AWT thread.
        Geometry sky = (Geometry) SkyFactory.createSky(assetManager, texture, envMapType);
        sky.setName("SkyBox");
        sky.setShadowMode(
            ShadowMode.Off); // !! important for shadows to work on the rest of the scene !!.
        sky.setQueueBucket(Bucket.Sky);

        // add it to the scene tree. This will also safely add it to the scene.
        sceneGraphService.add(sky, (Node) sceneGraphService.getSelectedObject());

        JButton button = (JButton) e.getSource();
        Window window = SwingUtilities.getWindowAncestor(button);
        window.dispose();

      });

    });

    cancelButton.addActionListener(e -> {

      JButton button = (JButton) e.getSource();
      Window window = SwingUtilities.getWindowAncestor(button);
      window.dispose();

    });

  }

  private void populateListWithResources() {

    List<Path> modelFiles = null;

    try {
      modelFiles = Files
          .walk(new File(DevKitConfig.getInstance().getAssetRootDir()).toPath())
          .filter(p -> {
            for (String ext : TextureImage.imageExtensions) {
              if (p.toString().endsWith(ext)) {
                return true;
              }
            }
            return false;
          })
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
        if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
          relativePath = relativePath.substring(1);
        }

        relativePath = relativePath.replace("\\", "/");

        listModel.addElement(relativePath);
      }

      assetsList.setModel(listModel);
    }

  }

  private void populateComboBoxWithEnvMapTypes() {

    DefaultComboBoxModel<EnvMapType> comboBoxModel = new DefaultComboBoxModel<>();
    for (EnvMapType type : EnvMapType.values()) {
      comboBoxModel.addElement(type);
    }

    comboBox1.setModel(comboBoxModel);

    // pretty much every case wants this value, so we'll default it.
    comboBox1.setSelectedIndex(EnvMapType.EquirectMap.ordinal());
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
    rootPanel.setLayout(new GridLayoutManager(3, 2, new Insets(0, 0, 0, 0), -1, -1));
    final JScrollPane scrollPane1 = new JScrollPane();
    rootPanel.add(scrollPane1,
        new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    assetsList = new JList();
    scrollPane1.setViewportView(assetsList);
    final JLabel label1 = new JLabel();
    label1.setText("Environment Map Type");
    rootPanel.add(label1,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    comboBox1 = new JComboBox();
    rootPanel.add(comboBox1, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
    rootPanel.add(panel1,
        new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null,
            null, 0, false));
    createButton = new JButton();
    createButton.setText("Create");
    panel1.add(createButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    panel1.add(spacer1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null,
        0, false));
    cancelButton = new JButton();
    cancelButton.setText("Cancel");
    panel1.add(cancelButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER,
        GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return rootPanel;
  }


}
