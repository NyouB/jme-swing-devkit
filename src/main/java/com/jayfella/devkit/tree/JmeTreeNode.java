package com.jayfella.devkit.tree;

import javax.swing.tree.DefaultMutableTreeNode;

public abstract class JmeTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

  public JmeTreeNode(Object object) {
    super(object);
  }
}
