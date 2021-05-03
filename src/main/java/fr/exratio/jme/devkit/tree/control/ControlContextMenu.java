package fr.exratio.jme.devkit.tree.control;

import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.service.SceneGraphService;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlContextMenu extends JPopupMenu {

  private final SceneGraphService sceneGraphService;

  @Autowired
  public ControlContextMenu(SceneGraphService sceneGraphService) {
    super();
    this.sceneGraphService = sceneGraphService;

    JMenuItem deleteItem = add(new JMenuItem("Delete"));
    deleteItem.addActionListener(e -> sceneGraphService
        .remove((Control) sceneGraphService.getSelectedObject()));
    deleteItem.setMnemonic('D');

  }

}
