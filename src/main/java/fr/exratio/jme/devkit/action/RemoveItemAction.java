package fr.exratio.jme.devkit.action;

import com.jme3.light.Light;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.springframework.stereotype.Component;

@Component
public class RemoveItemAction extends AbstractAction {

  private final SceneGraphService sceneGraphService;

  public RemoveItemAction(SceneGraphService sceneGraphService) {
    this.sceneGraphService = sceneGraphService;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    Object object = sceneGraphService.getSelectedObject();
    if (object instanceof Light) {
      sceneGraphService.remove((Light) object, sceneGraphService.getParentOf(object));
    }
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
