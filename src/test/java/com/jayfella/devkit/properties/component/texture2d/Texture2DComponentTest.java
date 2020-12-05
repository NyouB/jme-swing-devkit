package com.jayfella.devkit.properties.component.texture2d;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jayfella.devkit.CustomAWTLoader;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.text.ParseException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class Texture2DComponentTest {



  public static final void main(String[] args) throws ParseException {
    DesktopAssetManager assetManager = new DesktopAssetManager();
    assetManager.registerLoader(CustomAWTLoader.class, "png", "jpg", "gif", "jpeg");
    //assetManager.registerLoader(AWTLoader.class, "tiff", "bmp");
    assetManager.registerLocator(TEST_ASSET_ROOT_DIRECTORY, FileLocator.class);

    JFrame frame = new JFrame("Test");
    Texture2DEditor texture2DComponent = new Texture2DEditor();
    texture2DComponent.setAssetManager(assetManager);
    texture2DComponent.setAssetRootDirectory(TEST_ASSET_ROOT_DIRECTORY);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    SwingUtilities.invokeLater(() -> {
      frame.add(texture2DComponent.getCustomEditor());
      frame.pack();
      frame.setVisible(true);
    });
  }

  public static final String TEST_ASSET_ROOT_DIRECTORY = "src/test/resources/assetTest";

  @Test
  void setComponent() {
    DesktopAssetManager assetManager = new DesktopAssetManager();
    assetManager.registerLoader(AWTLoader.class, "png", "jpg");
    assetManager.registerLocator(TEST_ASSET_ROOT_DIRECTORY, FileLocator.class);
    Texture2D texture1 = (Texture2D) assetManager.loadTexture("textures/texture1.jpg");
    Texture2DEditor texture2DComponent = new Texture2DEditor();
    texture2DComponent.setAssetManager(assetManager);
    texture2DComponent.setAssetRootDirectory(TEST_ASSET_ROOT_DIRECTORY);
    texture2DComponent.setTypedValue(texture1);
    assertEquals(texture1, texture2DComponent.getValue());

  }


}