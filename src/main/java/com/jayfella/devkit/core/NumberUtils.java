package com.jayfella.devkit.core;

public class NumberUtils {

  public static boolean isInteger(String value) {
    return value.matches("\\d*");
  }

  public static boolean isFloat(String value) {
    return value.matches("^([+-]?\\d*\\.?\\d*)$");
  }

}
