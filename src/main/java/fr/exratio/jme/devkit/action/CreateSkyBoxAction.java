package fr.exratio.jme.devkit.action;

import com.jme3.asset.AssetManager;
import fr.exratio.jme.devkit.service.SceneGraphService;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;

public class CreateSkyBoxAction extends AbstractAction {

  private final SceneGraphService sceneGraphService;
  private final AssetManager assetManager;

  public CreateSkyBoxAction(SceneGraphService sceneGraphService,
      AssetManager assetManager) {
    this.sceneGraphService = sceneGraphService;
    this.assetManager = assetManager;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }
}
