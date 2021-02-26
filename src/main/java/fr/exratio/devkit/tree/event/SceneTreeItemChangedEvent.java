package fr.exratio.devkit.tree.event;

import fr.exratio.devkit.event.Event;
import fr.exratio.devkit.tree.JmeTreeNode;

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
