package fr.exratio.jme.devkit.tree.spatial.menu;

import fr.exratio.jme.devkit.service.ClipboardService;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.MenuController;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.tree.spatial.GeometryTreeNode;
import java.awt.HeadlessException;
import java.util.List;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GeometryContextMenu extends SpatialContextMenu {

  private final EditorJmeApplication editorJmeApplication;
  private final SceneGraphService sceneGraphService;
  private final ClipboardService clipboardService;
  private final RegistrationService registrationService;
  private final MenuController menuController;

  @Autowired
  public GeometryContextMenu(
      EditorJmeApplication editorJmeApplication,
      SceneGraphService sceneGraphService,
      ClipboardService clipboardService,
      RegistrationService registrationService,
      MenuController menuController) throws HeadlessException {
    super(editorJmeApplication, sceneGraphService, clipboardService,
        registrationService, menuController);
    this.editorJmeApplication = editorJmeApplication;
    this.sceneGraphService = sceneGraphService;
    this.clipboardService = clipboardService;
    this.registrationService = registrationService;
    this.menuController = menuController;

//        // Determine if the geometry of this mesh is a child of an InstancedNode
//        // If it is, give the user the option to create an instance based on this mesh.
//
//        JmeEngineService engineService = ServiceManager.getService(JmeEngineService.class);
//
//        JSeparator separator = (JSeparator) add(new JSeparator());
//
//        JMenuItem newInstanceItem = add(new JMenuItem("Create New Instance"));
//        newInstanceItem.addActionListener(e -> {
//
//        });
//        newInstanceItem.setEnabled(false);
//
//        // the parent is attached to the JME scene, so we must touch it on the JMEt thread.
//        engineService.enqueue(() -> {
//
//            // this can only be true.
//            // GeometryTreeNode geometryTreeNode = (GeometryTreeNode) meshTreeNode.getParent();
//            Geometry geometry = geometryTreeNode.getUserObject();
//
//            Spatial parent = geometry.getParent();
//            boolean isInstancedNode = false;
//
//            while (!isInstancedNode && parent != null) {
//                isInstancedNode = parent instanceof InstancedNode;
//                parent = parent.getParent();
//            }
//
//            boolean finalIsInstancedNode = isInstancedNode;
//
//            SwingUtilities.invokeLater(() -> {
//                if (finalIsInstancedNode) {
//                    newInstanceItem.setEnabled(true);
//                }
//                else {
//                    //remove(loadingItem);
//                    newInstanceItem.setVisible(false);
//                    separator.setVisible(false);
//                }
//
//                // this doesn't seem to work. We'll leave it here for now.
//                // The layout manager still retains its height of the invisible elements.
//                revalidate();
//                repaint();
//
//            });
//
//        });

    // Allow users to also add their options....
    List<JMenuItem> customItems = menuController.getCustomMenuItems(GeometryTreeNode.class);

    if (customItems != null && !customItems.isEmpty()) {

      // add a separator for clarity.
      add(new JSeparator());

      for (JMenuItem customItem : customItems) {
        add(customItem);
      }
    }

  }

}
