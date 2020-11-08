package com.jayfella.devkit.properties.component.texture2d;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.jme.TextureImage;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.material.MaterialChooserComponent;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.texture.Texture2D;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.io.FilenameUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.helpers.MessageFormatter;

public class Texture2DComponent extends AbstractSDKComponent<Texture2D> {

  private static final Logger LOGGER = LoggerFactory.getLogger(MaterialChooserComponent.class);

  private JPanel contentPanel;
  private JLabel propertyNameLabel;
  private JButton clearTextureButton;
  private Texture2DPanel imagePanel;
  private JList<String> texturesList;
  private String assetRootDirectory;
  private AssetManager assetManager;

  public Texture2DComponent() {
    this(new Texture2D(), null);
  }

  public Texture2DComponent(Texture2D texture2D) {
    this(texture2D, null);
  }

  public Texture2DComponent(String propertyName) {
    this(new Texture2D(), propertyName);
  }

  public Texture2DComponent(Texture2D texture2D, String propertyName) {
    this(texture2D, propertyName, null, null);
  }

  public Texture2DComponent(Texture2D texture2D, String propertyName, AssetManager assetManager,
      String assetRootDirectory) {
    super(texture2D);
    this.assetRootDirectory = assetRootDirectory;
    this.assetManager = assetManager;
    $$$setupUI$$$();
    updateTextureChooser();
    setComponent(texture2D);
    setPropertyName(propertyName);
  }

  public static List<Path> findTextureFiles(String rootDirectory) throws IOException {
    // get a list of all textures in the asset root.
    List<Path> textureFiles = new ArrayList<>();
    if (rootDirectory == null) {
      return textureFiles;
    }
    textureFiles = Files
        .walk(new File(rootDirectory).toPath())
        .filter(
            p -> TextureImage.imageExtensions.contains(FilenameUtils.getExtension(p.toString())))
        .collect(Collectors.toList());
    return textureFiles;
  }

  private String toAssetManagerFormat(Path filePath) {
    String relativePath = filePath.toString()
        .replaceFirst(assetRootDirectory, "");

    // remove any trailing slashes.
    if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
      relativePath = relativePath.substring(1);
    }
    return relativePath.replace("\\", "/");
  }

  private void updateTextureChooser() {
    List<Path> texturePathList = null;
    try {
      texturePathList = findTextureFiles(assetRootDirectory);
    } catch (IOException e) {
      LOGGER.warn(
          "<< findTextureFiles() an IO exception occured, returning empty list, you might change the asset directory in the preferences");
      JXErrorPane pane = new JXErrorPane();
      MessageFormatter.format(
          "Error while listing textures in the folder {}, you might change the asset directory in the preferences",
          DevKitConfig.getInstance()
              .getProjectConfig().getAssetRootDir());
      pane.setErrorInfo(new ErrorInfo("DevKitError", MessageFormatter.format(
          "Error while listing textures in the folder {}, you might change the asset directory in the preferences",
          DevKitConfig.getInstance()
              .getProjectConfig().getAssetRootDir()).getMessage(), "IOException", e.getMessage(), e,
          Level.SEVERE, null));
      JXErrorPane.showDialog(contentPanel, pane);
    }
    DefaultListModel<String> model = new DefaultListModel<>();

    for (Path path : texturePathList) {
      model.addElement(toAssetManagerFormat(path));
    }

    texturesList.setModel(model);
  }

  @Override
  public void setComponent(Texture2D value) {
    if (value == null) {
      component = null;
      texturesList.setSelectedIndex(-1);
      return;
    }

    component = value;
    // if the texture is embedded it won't have a key.
    SwingUtilities.invokeLater(() -> {
      System.out.println("getting key");
          String keyString =
              value.getKey() != null ? component.getKey().getName() : component.getName();
          if (0 < texturesList.getModel().getSize()) {
            texturesList.setSelectedValue(keyString, true);
          }
      System.out.println("setting texture ");
          this.imagePanel.setTexture(component);
          contentPanel.revalidate();
          contentPanel.repaint();
      System.out.println("repaint end");
        }
    );
  }

  public Texture2D computeValue() {
    String newValue = texturesList.getSelectedValue();
    if (newValue == null) {
      return new Texture2D();
    }

    Texture2D texture2D;
    try {
      texture2D = (Texture2D) assetManager.loadTexture(newValue);
    } catch (
        AssetNotFoundException ex) {
      LOGGER.warn("Texture2D Not Found: {} returning empty Texture", newValue);
      LOGGER.debug("Texture2D Not Found: {} returning empty Texture", newValue, ex);
      texture2D = new Texture2D();
    }
    return texture2D;
  }


  @Override
  public void setPropertyName(String propertyName) {
    super.setPropertyName(propertyName);
    propertyNameLabel.setText("Texture2D: " + propertyName);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    contentPanel = new JPanel();
    contentPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 0), -1, -1));
    propertyNameLabel = new JLabel();
    propertyNameLabel.setText("Label");
    contentPanel.add(propertyNameLabel,
        new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0,
            false));
    imagePanel = new Texture2DPanel();
    contentPanel.add(imagePanel,
        new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    final JScrollPane scrollPane1 = new JScrollPane();
    contentPanel.add(scrollPane1,
        new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null,
            null, null, 0, false));
    scrollPane1.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createLoweredBevelBorder(), null,
            TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    scrollPane1.setViewportView(texturesList);
    clearTextureButton.setText("No Texture");
    contentPanel.add(clearTextureButton,
        new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JSeparator separator1 = new JSeparator();
    contentPanel.add(separator1,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
            GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, null,
            null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }

  public void clearTextureSelection() {
    texturesList.clearSelection();
    setComponent(null);
  }

  private void createUIComponents() {
    clearTextureButton = new JButton();
    clearTextureButton.addActionListener(e -> clearTextureSelection());

    texturesList = new JList();
    ListSelectionListener listSelectionListener = evt -> {
      System.out.println("selection begin");
      JList lsm = (JList) evt.getSource();
      if (lsm.isSelectionEmpty()) {
        return;
      }
      Texture2D oldTexture = component;
      System.out.println("before compute");
      Texture2D newTexture = computeValue();
      System.out.println("after compute");
      if (!oldTexture.equals(newTexture)) {
        setComponent(computeValue());
        System.out.println("firing event");
        firePropertyChange(propertyName, oldTexture, component);
      }
    };
    texturesList.addListSelectionListener(listSelectionListener);
  }

  @Override
  public JComponent getJComponent() {
    return contentPanel;
  }

  public String getAssetRootDirectory() {
    return assetRootDirectory;
  }

  public void setAssetRootDirectory(String assetRootDirectory) {
    this.assetRootDirectory = assetRootDirectory;
    updateTextureChooser();
  }


  public AssetManager getAssetManager() {
    return assetManager;
  }

  public void setAssetManager(AssetManager assetManager) {
    this.assetManager = assetManager;
  }
}
