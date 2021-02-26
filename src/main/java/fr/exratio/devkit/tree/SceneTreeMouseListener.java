package fr.exratio.devkit.tree;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;

public class SceneTreeMouseListener extends MouseAdapter {

  @Override
  public void mousePressed(MouseEvent e) {
    showPopup(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    showPopup(e);
  }

  private void showPopup(MouseEvent e) {
    if (e.isPopupTrigger()) {

      JTree tree = (JTree) e.getSource();

      if (tree.getLastSelectedPathComponent() instanceof TreeContextMenu) {

        TreeContextMenu menuTreeNode = (TreeContextMenu) tree.getLastSelectedPathComponent();

        // it is possible that the node doesn't have a context menu.
        if (menuTreeNode != null && menuTreeNode.getContextMenu() != null) {
          menuTreeNode.getContextMenu().show(tree, e.getX(), e.getY());
        }

      }
    }
  }

}
