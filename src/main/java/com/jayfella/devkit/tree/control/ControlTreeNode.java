package com.jayfella.devkit.tree.control;

import com.jayfella.devkit.tree.TreeContextMenu;
import com.jme3.scene.control.Control;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

public class ControlTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

  public ControlTreeNode(Control control) {
    super(control);
  }

  @Override
  public Control getUserObject() {
    return (Control) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return new ControlContextMenu(this);
  }

}
