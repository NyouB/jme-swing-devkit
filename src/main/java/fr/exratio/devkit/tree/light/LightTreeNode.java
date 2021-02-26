package fr.exratio.devkit.tree.light;

import com.jme3.light.Light;
import fr.exratio.devkit.tree.TreeContextMenu;
import javax.swing.tree.DefaultMutableTreeNode;

public abstract class LightTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

  public LightTreeNode(Light light) {
    super(light);
  }

  @Override
  public Light getUserObject() {
    return (Light) super.getUserObject();
  }

}
