package fr.exratio.jme.devkit.tree;

import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

class IconNodeRenderer extends DefaultTreeCellRenderer {

  public Component getTreeCellRendererComponent(JTree tree, Object value,
      boolean sel, boolean expanded, boolean leaf, int row,
      boolean hasFocus) {

    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
        row, hasFocus);

    IconNode iconNode = (IconNode) value;
    Icon icon = iconNode.getIcon();

    if (icon == null) {
      if (iconNode.isLeaf()) {
        icon = getDefaultLeafIcon();
      } else if (tree.isExpanded(new TreePath(iconNode.getPath()))) {
        icon = getDefaultOpenIcon();
      } else {
        icon = getDefaultClosedIcon();
      }
    }
    setIcon(icon);
    return this;
  }
}