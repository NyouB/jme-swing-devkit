package com.jayfella.devkit;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoadException;
import com.jme3.asset.AssetLoader;
import com.jme3.asset.TextureKey;
import com.jme3.texture.Image;
import com.jme3.texture.Image.Format;
import com.jme3.util.BufferUtils;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;

public class CustomAWTLoader implements AssetLoader {

  public static final ColorModel AWT_RGBA4444 = new DirectColorModel(16,
      0xf000,
      0x0f00,
      0x00f0,
      0x000f);

  public static final ColorModel AWT_RGBA5551
      = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
      new int[]{5, 5, 5, 1},
      true,
      false,
      Transparency.BITMASK,
      DataBuffer.TYPE_BYTE);

  private Object extractImageData(BufferedImage img){
    DataBuffer buf = img.getRaster().getDataBuffer();
    switch (buf.getDataType()){
      case DataBuffer.TYPE_BYTE:
        DataBufferByte byteBuf = (DataBufferByte) buf;
        return byteBuf.getData();
      case DataBuffer.TYPE_USHORT:
        DataBufferUShort shortBuf = (DataBufferUShort) buf;
        return shortBuf.getData();
    }
    return null;
  }

  private void flipImage(byte[] img, int width, int height, int bpp){
    int scSz = (width * bpp) / 8;
    byte[] sln = new byte[scSz];
    int y2 = 0;
    for (int y1 = 0; y1 < height / 2; y1++){
      y2 = height - y1 - 1;
      System.arraycopy(img, y1 * scSz, sln, 0,         scSz);
      System.arraycopy(img, y2 * scSz, img, y1 * scSz, scSz);
      System.arraycopy(sln, 0,         img, y2 * scSz, scSz);
    }
  }

  private void flipImage(short[] img, int width, int height, int bpp){
    int scSz = (width * bpp) / 8;
    scSz /= 2; // Because shorts are 2 bytes
    short[] sln = new short[scSz];
    int y2 = 0;
    for (int y1 = 0; y1 < height / 2; y1++){
      y2 = height - y1 - 1;
      System.arraycopy(img, y1 * scSz, sln, 0,         scSz);
      System.arraycopy(img, y2 * scSz, img, y1 * scSz, scSz);
      System.arraycopy(sln, 0,         img, y2 * scSz, scSz);
    }
  }

  public Image load(BufferedImage img, boolean flipY){
    int width = img.getWidth();
    int height = img.getHeight();

    switch (img.getType()){
      case BufferedImage.TYPE_4BYTE_ABGR: // most common in PNG images w/ alpha
        byte[] dataBuf1 = (byte[]) extractImageData(img);
        if (flipY)
          flipImage(dataBuf1, width, height, 32);

        ByteBuffer data1 = BufferUtils.createByteBuffer(img.getWidth()*img.getHeight()*4);
        data1.put(dataBuf1);
        return new Image(Format.ABGR8, width, height, data1, null, com.jme3.texture.image.ColorSpace.sRGB);
      case BufferedImage.TYPE_3BYTE_BGR: // most common in JPEG images
        byte[] dataBuf2 = (byte[]) extractImageData(img);
        if (flipY)
          flipImage(dataBuf2, width, height, 24);

        ByteBuffer data2 = BufferUtils.createByteBuffer(img.getWidth()*img.getHeight()*3);
        data2.put(dataBuf2);
        return new Image(Format.BGR8, width, height, data2, null, com.jme3.texture.image.ColorSpace.sRGB);
      case BufferedImage.TYPE_BYTE_GRAY: // grayscale fonts
        byte[] dataBuf3 = (byte[]) extractImageData(img);
        if (flipY)
          flipImage(dataBuf3, width, height, 8);
        ByteBuffer data3 = BufferUtils.createByteBuffer(img.getWidth()*img.getHeight());
        data3.put(dataBuf3);
        return new Image(Format.Luminance8, width, height, data3, null, com.jme3.texture.image.ColorSpace.sRGB);
      default:
        break;
    }

    if (img.getTransparency() == Transparency.OPAQUE){
      ByteBuffer data = BufferUtils.createByteBuffer(img.getWidth()*img.getHeight()*3);
      // no alpha
      for (int y = 0; y < height; y++){
        for (int x = 0; x < width; x++){
          int ny = y;
          if (flipY){
            ny = height - y - 1;
          }

          int rgb = img.getRGB(x,ny);
          byte r = (byte) ((rgb & 0x00FF0000) >> 16);
          byte g = (byte) ((rgb & 0x0000FF00) >> 8);
          byte b = (byte) ((rgb & 0x000000FF));
          data.put(r).put(g).put(b);
        }
      }
      data.flip();
      return new Image(Format.RGB8, width, height, data, null, com.jme3.texture.image.ColorSpace.sRGB);
    }else{
      ByteBuffer data = BufferUtils.createByteBuffer(img.getWidth()*img.getHeight()*4);
      // alpha
      for (int y = 0; y < height; y++){
        for (int x = 0; x < width; x++){
          int ny = y;
          if (flipY){
            ny = height - y - 1;
          }

          int rgb = img.getRGB(x,ny);
          byte a = (byte) ((rgb & 0xFF000000) >> 24);
          byte r = (byte) ((rgb & 0x00FF0000) >> 16);
          byte g = (byte) ((rgb & 0x0000FF00) >> 8);
          byte b = (byte) ((rgb & 0x000000FF));
          data.put(r).put(g).put(b).put(a);
        }
      }
      data.flip();
      return new Image(Format.RGBA8, width, height, data, null, com.jme3.texture.image.ColorSpace.sRGB);
    }
  }

  public Image load(byte[] in, boolean flipY) throws IOException{
    ImageIcon imageIcon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(in));
    BufferedImage img =toBufferedImage(imageIcon.getImage());
    if (img == null){
      return null;
    }
    return load(img, flipY);
  }

  public Object load(AssetInfo info) throws IOException {
    if (ImageIO.getImageReadersBySuffix(info.getKey().getExtension()) != null){
      boolean flip = ((TextureKey) info.getKey()).isFlipY();
      InputStream in = null;
      try {
        in = info.openStream();
        byte[] inArray = IOUtils.toByteArray(in);
        Image img = load(inArray, flip);
        if (img == null){
          throw new AssetLoadException("The given image cannot be loaded " + info.getKey());
        }
        return img;
      } finally {
        if (in != null){
          in.close();
        }
      }
    }else{
      throw new AssetLoadException("The extension " + info.getKey().getExtension() + " is not supported");
    }
  }

  /**
   * Converts a given Image into a BufferedImage
   *
   * @param img The Image to be converted
   * @return The converted BufferedImage
   */
  public static BufferedImage toBufferedImage(java.awt.Image img)
  {
    if (img instanceof BufferedImage)
    {
      return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
  }
}