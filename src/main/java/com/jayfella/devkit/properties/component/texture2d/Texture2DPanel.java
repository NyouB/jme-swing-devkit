package com.jayfella.devkit.properties.component.texture2d;

import com.jme3.math.ColorRGBA;
import com.jme3.texture.Texture2D;
import com.jme3.texture.image.ImageRaster;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import javax.swing.JPanel;

public class Texture2DPanel extends JPanel {

  private BufferedImage img;
  private Image scaled;

  public Texture2DPanel() {
    super();

  }

  public void setTexture(Texture2D texture2D) {

    if (texture2D != null && texture2D.getImage() != null) {

      System.out.println("panel begin set texture");
      // this is slow.. but it works.

      int texWidth = texture2D.getImage().getWidth();
      int texHeight = texture2D.getImage().getHeight();

      // create a BufferedImage the same dimensions as the texture.
      img = new BufferedImage(
          texWidth,
          texHeight,
          BufferedImage.TYPE_INT_ARGB);

      WritableRaster writableRaster = img.getRaster();
      System.out.println("rasterize");
      // write the texture to the BufferedImage.
      ImageRaster textureRaster = ImageRaster.create(texture2D.getImage());

      for (int x = 0; x < texture2D.getImage().getWidth(); x++) {
        for (int y = 0; y < texture2D.getImage().getHeight(); y++) {

          ColorRGBA pixel = textureRaster.getPixel(x, y);
          writableRaster.setPixel(x, y, new float[]{
              pixel.r * 255,
              pixel.g * 255,
              pixel.b * 255,
              //pixel.a * 255});
              255});
        }
      }

      img.setData(writableRaster);

      // define a max height and width, then determine the biggest from the texture.
      // the width of this won't work because the panel will be resized by it's parent.

      int maxHeight = 100;
      int newHeight = Math.min(texHeight, maxHeight);

      float widthRatio = (float) texWidth / (float) texHeight;

      int newWidth = (int) (newHeight * widthRatio);

      Dimension dim = new Dimension(newWidth, newHeight);

      setSize(dim);
      setMinimumSize(dim);
      setMaximumSize(dim);
      setPreferredSize(dim);
      System.out.println("finishrasterize");
    } else {
      img = null;

      Dimension zeroDim = new Dimension(0, 0);

      setSize(zeroDim);
      setMinimumSize(zeroDim);
      setMaximumSize(zeroDim);
      setPreferredSize(zeroDim);
    }
  }

  @Override
  public void invalidate() {
    super.invalidate();

    if (img != null) {

      int width = getWidth();
      int height = getHeight();

      if (width > 0 && height > 0) {
        scaled = img.getScaledInstance(getWidth(), getHeight(),
            Image.SCALE_SMOOTH);
      }

    }

  }

  @Override
  public Dimension getPreferredSize() {
        /*
        return img == null ? new Dimension(200, 200) : new Dimension(
                img.getWidth(this), img.getHeight(this));

         */
    return super.getPreferredSize();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(scaled, 0, 0, null);
  }

}
