package fr.exratio.jme.devkit.properties.component.material;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.util.MaterialDebugAppState;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaterialChooserEditor extends AbstractPropertyEditor<Material> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MaterialChooserEditor.class);

  private JPanel contentPanel;
  private final EditorJmeApplication editorJmeApplication;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JComboBox materialsComboBox;

  @Override
  public void setTypedValue(Material newValue) {
    if (newValue == null) {
      return;
    }
    materialsComboBox.setSelectedItem(newValue.getMaterialDef().getAssetName());
    super.setTypedValue(newValue);
  }

  @Override
  protected Material computeValue() {
    String selectedMaterial = (String) materialsComboBox.getSelectedItem();

    AssetManager assetManager = editorJmeApplication.getAssetManager();
    Material material = null;

    if (selectedMaterial != null) {

      if (selectedMaterial.endsWith(".j3md")) {
        material = new Material(assetManager, selectedMaterial);
      } else if (selectedMaterial.endsWith(".j3m")) {
        material = assetManager.loadMaterial(selectedMaterial);
      }

    } else {
      LOGGER.warn("The specified material is NULL. This is probably not intended!");
    }
    return material;
  }


  private void createUIComponents() {
    contentPanel = this;
    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

    try {
      model.addAll(
          findFiles(Paths.get(DevKitConfig.getInstance().getAssetRootDir()),
              "j3md", "j3m"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    materialsComboBox = new JComboBox();
    materialsComboBox.setModel(model);
    materialsComboBox.addItemListener(evt -> {
      if (evt.getStateChange() == ItemEvent.SELECTED) {
        setTypedValue(computeValue());
        firePropertyChange();
      }
    });

    reloadMaterialButton = new JButton();
    reloadMaterialButton.addActionListener(e -> {

      if (value != null) {
        editorJmeApplication.enqueue(() -> editorJmeApplication
            .getStateManager()
            .getState(MaterialDebugAppState.class)
            .reloadMaterial(value));
      }

    });

  }

  public static List<String> findFiles(Path path, String... fileExtension)
      throws IOException {

    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Path must be a directory!");
    }

    List<String> result;

    try (Stream<Path> walk = Files.walk(path)) {
      result = walk
          .filter(p -> !Files.isDirectory(p))
          // this is a path, not string,
          // this only test if path end with a certain path
          //.filter(p -> p.endsWith(fileExtension))
          // convert path to string first
          .map(p -> p.toString().toLowerCase())
          .filter(f -> Arrays.stream(fileExtension).anyMatch((f::endsWith)))
          .collect(Collectors.toList());
    }

    return result;
  }

  private JButton reloadMaterialButton;

  public MaterialChooserEditor(Material value,
      EditorJmeApplication editorJmeApplication) {
    super(value);
    initComponents();
    this.editorJmeApplication = editorJmeApplication;
    setTypedValue(value);
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    createUIComponents();

    var label1 = new JLabel();
    var hSpacer1 = new Spacer();
    var vSpacer1 = new Spacer();

    //======== this ========
    setLayout(new GridLayoutManager(4, 2, new Insets(0, 0, 0, 0), -1, -1));

    //---- label1 ----
    label1.setText("Material");
    add(label1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
        GridConstraints.SIZEPOLICY_FIXED,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    add(hSpacer1, new GridConstraints(0, 1, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        null, null, null));
    add(vSpacer1, new GridConstraints(3, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));
    add(materialsComboBox, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));

    //---- reloadMaterialButton ----
    reloadMaterialButton.setText("Reload Material");
    add(reloadMaterialButton, new GridConstraints(2, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_FIXED,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
