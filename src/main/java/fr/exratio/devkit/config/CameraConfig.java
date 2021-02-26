package fr.exratio.devkit.config;

import com.jme3.math.ColorRGBA;
import java.awt.Dimension;

public class CameraConfig {

  // a regular blue sky
  private ColorRGBA viewportColor = new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f);

  private float fieldOfView = 45.0f;
  private float frustumNear = 0.1f;
  private float frustumFar = 1000.0f;
  private Dimension cameraDimension = new Dimension(600, 480);


  public CameraConfig() {

  }

  public ColorRGBA getViewportColor() {
    return viewportColor;
  }

  public void setViewportColor(ColorRGBA viewportColor) {
    this.viewportColor = viewportColor;
  }

  public float getFieldOfView() {
    return fieldOfView;
  }

  public void setFieldOfView(float fieldOfView) {
    this.fieldOfView = fieldOfView;
  }

  public float getFrustumNear() {
    return frustumNear;
  }

  public void setFrustumNear(float frustumNear) {
    this.frustumNear = frustumNear;
  }

  public float getFrustumFar() {
    return frustumFar;
  }

  public void setFrustumFar(float frustumFar) {
    this.frustumFar = frustumFar;
  }

  public Dimension getCameraDimension() {
    return cameraDimension;
  }

  public void setCameraDimension(Dimension cameraDimension) {
    this.cameraDimension = cameraDimension;
  }

  public void setCameraDimension(int width, int height) {
    this.cameraDimension = new Dimension(width, height);
  }
}
