package fr.exratio.jme.devkit.tree.control;

import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.tree.spatial.SpatialTreeNode;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlContextMenu extends JPopupMenu {

  private final SceneTreeService sceneTreeService;
  private final SceneGraphService sceneGraphService;

  @Autowired
  public ControlContextMenu(SceneTreeService sceneTreeService,
      SceneGraphService sceneGraphService) {
    super();
    this.sceneTreeService = sceneTreeService;
    this.sceneGraphService = sceneGraphService;

    JMenuItem deleteItem = add(new JMenuItem("Delete"));
    deleteItem.addActionListener(e -> {
      SpatialTreeNode parentNode = (SpatialTreeNode) sceneTreeService
          .jmeObjectToNode(sceneGraphService.getSelectedObject()).getParent();

      sceneGraphService
          .remove((Control) sceneGraphService.getSelectedObject(), parentNode.getUserObject());
    });
    deleteItem.setMnemonic('D');

  }

}
