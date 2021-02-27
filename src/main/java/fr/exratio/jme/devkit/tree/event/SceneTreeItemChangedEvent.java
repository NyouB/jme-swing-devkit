package fr.exratio.jme.devkit.tree.event;

import fr.exratio.jme.devkit.event.Event;
import fr.exratio.jme.devkit.tree.JmeTreeNode;

/**
 * Fired whenever the scene tree selected item is changed. This event is fired from the SWING
 * thread.
 */
public class SceneTreeItemChangedEvent extends Event {

  private final JmeTreeNode treeNode;

  public SceneTreeItemChangedEvent(JmeTreeNode treeNode) {
    this.treeNode = treeNode;
  }

  public JmeTreeNode getTreeNode() {
    return treeNode;
  }
}
