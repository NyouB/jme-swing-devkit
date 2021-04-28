package fr.exratio.jme.devkit.action;

import com.jme3.asset.ModelKey;
import com.jme3.scene.AssetLinkNode;
import fr.exratio.jme.devkit.registration.spatial.AssetLinkNodeRegistrar.AssetLinkNodeTreeNode;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.service.SceneTreeService;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveLinkedAssetAction extends AbstractAction {

  private final EditorJmeApplication editorJmeApplication;
  private final SceneGraphService sceneGraphService;
  private final SceneTreeService sceneTreeService;

  @Autowired
  public RemoveLinkedAssetAction(
      EditorJmeApplication editorJmeApplication,
      SceneGraphService sceneGraphService,
      SceneTreeService sceneTreeService) {
    this.editorJmeApplication = editorJmeApplication;
    this.sceneGraphService = sceneGraphService;
    this.sceneTreeService = sceneTreeService;
  }

  @Override
  public boolean accept(Object sender) {
    return false;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    // then run this "later" so the GUI can display the "disabled" view now.
    SwingUtilities.invokeLater(() -> {

      AssetLinkNodeTreeNode assetLinkNodeTreeNode = ((AssetLinkNodeTreeNode) sceneGraphService
          .getSelectedObject());

      // get the list of models in the AWT thread.
      ArrayList<ModelKey> selectedModelKeys = assetLinkNodeTreeNode.getUserObject()
          .getAssetLoaderKeys();

      editorJmeApplication.enqueue(() -> {

        AssetLinkNode assetLinkNode = ((AssetLinkNodeTreeNode) sceneGraphService
            .getSelectedObject()).getUserObject();

        for (ModelKey key : selectedModelKeys) {
          assetLinkNode.removeLinkedChild(key);
        }

        // reload all models.
        assetLinkNode.attachLinkedChildren(editorJmeApplication.getAssetManager());

        // close the window on the AWT thread.
        SwingUtilities.invokeLater(() -> {

          sceneTreeService.reloadTreeNode(assetLinkNodeTreeNode);

          JButton button = (JButton) e.getSource();
          Window window = SwingUtilities.getWindowAncestor(button);
          window.dispose();
        });

      });

    });
  }
}
