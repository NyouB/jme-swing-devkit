package fr.exratio.devkit.tree.light;

import com.jme3.light.LightProbe;
import fr.exratio.devkit.tree.light.menu.LightProbeContextMenu;
import javax.swing.JPopupMenu;

public class LightProbeTreeNode extends LightTreeNode {

  public LightProbeTreeNode(LightProbe light) {
    super(light);
  }

  @Override
  public LightProbe getUserObject() {
    return (LightProbe) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return new LightProbeContextMenu(this);
  }

}
