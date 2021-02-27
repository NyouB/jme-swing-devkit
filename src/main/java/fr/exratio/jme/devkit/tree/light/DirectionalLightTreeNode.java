package fr.exratio.jme.devkit.tree.light;

import com.jme3.light.DirectionalLight;
import fr.exratio.jme.devkit.tree.light.menu.DirectionalLightContextMenu;
import javax.swing.JPopupMenu;

public class DirectionalLightTreeNode extends LightTreeNode {

  public DirectionalLightTreeNode(DirectionalLight light) {
    super(light);
  }

  @Override
  public DirectionalLight getUserObject() {
    return (DirectionalLight) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return new DirectionalLightContextMenu(this);
  }
}
