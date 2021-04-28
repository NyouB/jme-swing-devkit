package fr.exratio.jme.devkit.tree.spatial;

import com.jme3.scene.Geometry;
import fr.exratio.jme.devkit.tree.spatial.menu.GeometryContextMenu;
import javax.swing.JPopupMenu;

public class GeometryTreeNode extends SpatialTreeNode {

  private final GeometryContextMenu geometryContextMenu;

  public GeometryTreeNode(Geometry geometry, GeometryContextMenu geometryContextMenu) {
    super(geometry);
    this.geometryContextMenu = geometryContextMenu;
  }

  @Override
  public Geometry getUserObject() {
    return (Geometry) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return geometryContextMenu;
  }
}
