package com.jayfella.devkit.core;

import com.jme3.math.ColorRGBA;

import java.awt.*;

public class ColorConverter {

    public static ColorRGBA toColorRGBA(Color color) {
        return new ColorRGBA(
                (float) color.getRed() / 255f,
                (float) color.getGreen() / 255f,
                (float) color.getBlue() / 255f,
                (float) color.getAlpha() / 255f
        );
    }

    public static Color toColor(ColorRGBA color) {
        return new Color(color.r, color.g, color.b, color.a);
    }

}
