package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.AmbientLight;
import fr.exratio.jme.devkit.tree.light.menu.AmbientLightContextMenu;
import javax.swing.JPopupMenu;

public class AmbientLightTreeNode extends LightTreeNode {

  public AmbientLightTreeNode(AmbientLight light) {
    super(light);
  }

  @Override
  public AmbientLight getUserObject() {
    return (AmbientLight) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return new AmbientLightContextMenu(this);
  }
}
