package com.jayfella.devkit.properties.component.texture2d;

import static org.junit.jupiter.api.Assertions.*;

import com.jayfella.devkit.CustomAWTLoader;
import com.jayfella.devkit.properties.component.vector4f.Vector4fComponent;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import com.jme3.asset.DesktopAssetManager;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.material.plugins.J3MLoader;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.List;
import javax.swing.DesktopManager;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.junit.jupiter.api.Test;

class Texture2DComponentTest {



  @Test
  void setComponent() {
    DesktopAssetManager assetManager = new DesktopAssetManager();
    assetManager.registerLoader(AWTLoader.class, "png", "jpg");
    assetManager.registerLocator(TEST_ASSET_ROOT_DIRECTORY, FileLocator.class);
    Texture2D texture1 = (Texture2D) assetManager.loadTexture("textures/texture1.jpg");
    Texture2DComponent texture2DComponent = new Texture2DComponent();
    texture2DComponent.setAssetManager(assetManager);
    texture2DComponent.setAssetRootDirectory(TEST_ASSET_ROOT_DIRECTORY);
    texture2DComponent.setComponent(texture1);
    assertEquals(texture1, texture2DComponent.getComponent());

  }

  public static final String TEST_ASSET_ROOT_DIRECTORY = "src/test/resources/assetTest";

  public static final void main(String[] args) throws ParseException {
    DesktopAssetManager assetManager = new DesktopAssetManager();
    assetManager.registerLoader(CustomAWTLoader.class, "png", "jpg", "gif", "jpeg");
    //assetManager.registerLoader(AWTLoader.class, "tiff", "bmp");
    assetManager.registerLocator(TEST_ASSET_ROOT_DIRECTORY, FileLocator.class);

    JFrame frame = new JFrame("Test");
    Texture2DComponent texture2DComponent = new Texture2DComponent();
    texture2DComponent.setAssetManager(assetManager);
    texture2DComponent.setAssetRootDirectory(TEST_ASSET_ROOT_DIRECTORY);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    SwingUtilities.invokeLater(() ->{
      frame.add(texture2DComponent.getJComponent());
      frame.pack();
      frame.setVisible(true);
    });
  }


}