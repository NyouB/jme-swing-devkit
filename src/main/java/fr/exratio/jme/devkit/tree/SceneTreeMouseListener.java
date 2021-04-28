package fr.exratio.jme.devkit.tree;

import fr.exratio.jme.devkit.tree.spatial.menu.SpatialContextMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTree;

public class SceneTreeMouseListener extends MouseAdapter {

  private final SpatialContextMenu nodeContextMenu;

  public SceneTreeMouseListener(SpatialContextMenu nodeContextMenu) {
    this.nodeContextMenu = nodeContextMenu;
  }

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
      nodeContextMenu.show(tree, e.getX(), e.getY());
    }
  }

}
