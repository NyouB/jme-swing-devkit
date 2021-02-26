package fr.exratio.devkit.config;

import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

public class SceneConfig {

  // grid
  private boolean showGrid = true;
  private Vector3f gridSize = new Vector3f(200, 200, 1.0f); // size x, y, lineWidth
  private ColorRGBA gridColor = new ColorRGBA(.75f, 0.75f, 0.75f, 1.0f);
  private Vector3f gridLocation = new Vector3f(-100, 0, -100);

  public SceneConfig() {

  }

  public boolean isShowGrid() {
    return showGrid;
  }

  public void setShowGrid(boolean showGrid) {
    this.showGrid = showGrid;
  }

  public Vector3f getGridSize() {
    return gridSize;
  }

  public void setGridSize(Vector3f gridSize) {
    this.gridSize = gridSize;
  }

  public ColorRGBA getGridColor() {
    return gridColor;
  }

  public void setGridColor(ColorRGBA gridColor) {
    this.gridColor = gridColor;
  }

  public Vector3f getGridLocation() {
    return gridLocation;
  }

  public void setGridLocation(Vector3f gridLocation) {
    this.gridLocation = gridLocation;
  }
}
