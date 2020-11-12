package com.jayfella.devkit.properties.component.texture2d;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel {

  private BufferedImage image;
  private Graphics2D blankImage = new BufferedImage(200, 20, BufferedImage.TYPE_INT_ARGB).createGraphics();

  public ImagePanel(){

  }

  public ImagePanel(BufferedImage image) {
    this.image = image;
  }

  public void setImage(BufferedImage bufferedImage) {
    this.image = bufferedImage;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(image, 0, 0, this); // see javadoc for more info on the parameters
  }

  public void clear(){
   paintComponent(blankImage);
  }

}
