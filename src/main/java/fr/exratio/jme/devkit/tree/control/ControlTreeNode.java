package fr.exratio.jme.devkit.tree.control;

import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.tree.TreeContextMenu;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

public class ControlTreeNode extends DefaultMutableTreeNode implements TreeContextMenu {

  private final ControlContextMenu controlContextMenu;

  public ControlTreeNode(Control control,
      ControlContextMenu controlContextMenu) {
    super(control);
    this.controlContextMenu = controlContextMenu;
  }

  @Override
  public Control getUserObject() {
    return (Control) super.getUserObject();
  }

  @Override
  public JPopupMenu getContextMenu() {
    return controlContextMenu;
  }

}
