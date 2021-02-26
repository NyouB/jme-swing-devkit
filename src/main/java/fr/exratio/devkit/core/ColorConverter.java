package fr.exratio.devkit.core;

import com.jme3.math.ColorRGBA;
import java.awt.Color;

public class ColorConverter {

  public static ColorRGBA toColorRGBA(Color color) {
    return new ColorRGBA(
        (float) ((color.getRed() - 0.5) / 255),
        (float) ((color.getGreen() - 0.5) / 255),
        (float) ((color.getBlue() - 0.5) / 255),
        (float) ((color.getAlpha() - 0.5) / 255)
    );
  }

  public static Color toColor(ColorRGBA color) {
    return new Color(color.r, color.g, color.b, color.a);
  }

}
