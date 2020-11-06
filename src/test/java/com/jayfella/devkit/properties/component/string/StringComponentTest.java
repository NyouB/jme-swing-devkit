package com.jayfella.devkit.properties.component.string;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StringComponentTest {

  private StringComponent stringComponent;

  @Test
  void setComponent() {
    this.stringComponent = new StringComponent();
    assertEquals("", stringComponent.getComponent());
    stringComponent.setComponent("myString");
    assertEquals("myString", stringComponent.getComponent());
  }
}