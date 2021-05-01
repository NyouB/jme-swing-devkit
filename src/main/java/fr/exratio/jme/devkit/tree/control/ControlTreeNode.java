package fr.exratio.jme.devkit.tree.control;

import com.jme3.scene.control.Control;
import javax.swing.tree.DefaultMutableTreeNode;

public class ControlTreeNode extends DefaultMutableTreeNode {

  public ControlTreeNode(Control control) {
    super(control);
  }

  @Override
  public Control getUserObject() {
    return (Control) super.getUserObject();
  }

}
