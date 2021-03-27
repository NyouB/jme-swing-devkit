package fr.exratio.jme.devkit.properties.component.texture2d;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jme3.asset.AssetManager;
import com.jme3.asset.AssetNotFoundException;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Texture2D;
import fr.exratio.jme.devkit.properties.component.AbstractPropertyEditor;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileSystemView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Texture2DEditor extends AbstractPropertyEditor<Texture2D> {

  public static final String DEFAULT_ASSET_ROOT_DIRECTORY = ".";
  public static final int TEXTURE_ICON_WIDTH = 60;
  public static final int TEXTURE_ICON_HEIGHT = 60;
  private static final Logger LOGGER = LoggerFactory.getLogger(Texture2DEditor.class);
  private JPanel contentPanel;
  private JLabel imageIconLabel;
  private ImageIcon imageIcon;
  private String assetRootDirectory;
  private Path assetRootDirectoryPath;
  private AssetManager assetManager;
  private String currentTexturePath;
  private JFileChooser fileChooser;

  public Texture2DEditor() {
    this(null);
  }

  public Texture2DEditor(Texture2D texture2D) {
    this(texture2D, null, DEFAULT_ASSET_ROOT_DIRECTORY);
  }

  public Texture2DEditor(Texture2D texture2D, AssetManager assetManager,
      String assetRootDirectory) {
    super(texture2D);
    this.assetRootDirectory = assetRootDirectory;
    this.assetManager = assetManager;
    $$$setupUI$$$();
    setTypedValue(texture2D);
  }

  public String toAssetManagerFormat(Path filePath) {
    //filePath.relativize(assetRootDirectoryPath);
    String relativePath = assetRootDirectoryPath.relativize(filePath).toString();

    // remove any trailing slashes.
    if (relativePath.startsWith("/") || relativePath.startsWith("\\")) {
      relativePath = relativePath.substring(1);
    }
    return relativePath.replace("\\", "/");
  }


  @Override
  public void setTypedValue(Texture2D newValue) {
    if (newValue == null) {
      value = null;
      imageIconLabel.setIcon(null);
      return;
    }
    // if the texture is embedded it won't have a key.
    String keyString =
        newValue.getKey() != null ? newValue.getKey().getName() : newValue.getName();
    displayPreview(Path.of(assetRootDirectory, keyString));
    super.setTypedValue(newValue);

  }

  public void setTextureFromFile(File file) {
    if (file == null) {
      value = null;
      imageIcon.setImage(null);
      return;
    }
    String newTexturePath = toAssetManagerFormat(file.toPath());
    if (newTexturePath.equals(currentTexturePath)) {
      LOGGER.debug(
          "<< setTextureFromFile() The new selected texture file did not change, keeping the same texture.");
      return;
    }
    currentTexturePath = newTexturePath;

    try {
      TextureKey key = new TextureKey(currentTexturePath, true);
      key.setGenerateMips(true);
      Texture2D texture2D = (Texture2D) assetManager.loadTexture(key);
      setTypedValue(texture2D);
    } catch (
        AssetNotFoundException ex) {
      LOGGER.warn("Texture2D Not Found: {} returning empty Texture", file.toString());
      LOGGER.debug("Texture2D Not Found: {} returning empty Texture", file.toString(), ex);
      setTypedValue(null);
    }
  }

  private void displayPreview(File file) {
    try {
      ImageIcon imageIcon = new ImageIcon(Files.readAllBytes(file.toPath()));
      imageIcon
          .setImage(getScaledImage(imageIcon.getImage(), TEXTURE_ICON_WIDTH, TEXTURE_ICON_HEIGHT));
      imageIconLabel.setIcon(imageIcon);
      //displayPreview(previewImage);
    } catch (IOException ex) {
      LOGGER.debug("<< displayPreview() Impossible to load image from file {}",
          file.getAbsolutePath());
    }
  }

  private void displayPreview(Path path) {
    displayPreview(new File(path.toUri()));
  }

  private void displayPreview(BufferedImage image) {
    SwingUtilities.invokeLater(() -> {
          this.imageIcon.setImage(image);
          contentPanel.revalidate();
          contentPanel.repaint();
        }
    );
  }

  public Texture2D computeValue() {
    return value;
  }


  public void clearTextureSelection() {
    setTypedValue(null);

  }

  private void createUIComponents() {
    contentPanel = this;

    imageIconLabel = new JLabel(imageIcon);

    fileChooser = new JFileChooser();
    fileChooser.setFileFilter(new TextureFilter());
    updateFileChooserRootDirectory(new File(assetRootDirectory));
    imageIconLabel.setBorder(BorderFactory
        .createLineBorder(Color.DARK_GRAY, 1));
    imageIconLabel.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
//not used because click is inconsistent
      }

      @Override
      public void mousePressed(MouseEvent e) {

      }

      @Override
      public void mouseReleased(MouseEvent e) {
        int returnVal = fileChooser.showOpenDialog(imageIconLabel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fileChooser.getSelectedFile();
          //This is where a real application would open the file.
          LOGGER.debug("Opening: {}.", file.getName());
          Texture2DEditor.this.setTextureFromFile(file);
        } else {
          LOGGER.debug("Open command cancelled by user.");
        }
      }

      @Override
      public void mouseEntered(MouseEvent e) {

      }

      @Override
      public void mouseExited(MouseEvent e) {

      }
    });

  }

  public String getAssetRootDirectory() {
    return assetRootDirectory;
  }

  public void setAssetRootDirectory(String assetRootDirectory) {
    this.assetRootDirectory = assetRootDirectory;
    File file = new File(assetRootDirectory);
    assetRootDirectoryPath = file.toPath().toAbsolutePath();
    updateFileChooserRootDirectory(file);

  }

  private void updateFileChooserRootDirectory(File rootDirectory) {
    FileSystemView fsv = new DirectoryRestrictedFileSystemView(rootDirectory);
    fileChooser.setFileSystemView(fsv);
    fileChooser.setCurrentDirectory(rootDirectory);
  }


  public AssetManager getAssetManager() {
    return assetManager;
  }

  public void setAssetManager(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  private Image getScaledImage(Image srcImg, int w, int h) {
    BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = resizedImg.createGraphics();

    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2.drawImage(srcImg, 0, 0, w, h, null);
    g2.dispose();

    return resizedImg;
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    contentPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    contentPanel.add(imageIconLabel,
        new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
            new Dimension(60, 60), null, 0, false));
  }


  public JComponent $$$getRootComponent$$$() {
    return contentPanel;
  }


}
