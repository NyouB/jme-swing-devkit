package fr.exratio.jme.devkit.tree.spatial.menu;

import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import fr.exratio.jme.devkit.clipboard.SpatialClipboardItem;
import fr.exratio.jme.devkit.forms.GenerateLightProbeDialog;
import fr.exratio.jme.devkit.forms.SaveSpatial;
import fr.exratio.jme.devkit.jme.EditorCameraState;
import fr.exratio.jme.devkit.registration.control.ControlRegistrar;
import fr.exratio.jme.devkit.service.ClipboardService;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.MenuController;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.service.SceneTreeService;
import fr.exratio.jme.devkit.tree.TreeConstants;
import fr.exratio.jme.devkit.tree.spatial.SpatialTreeNode;
import fr.exratio.jme.devkit.util.GUIUtils;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpatialContextMenu extends JPopupMenu {

  private final Spatial spatial;
  private final JMenu addMenu;
  private final EditorJmeApplication editorJmeApplication;
  private final SceneTreeService sceneTreeService;
  private final SceneGraphService sceneGraphService;
  private final ClipboardService clipboardService;

  @Autowired
  public SpatialContextMenu(
      EditorJmeApplication editorJmeApplication,
      SceneTreeService sceneTreeService,
      SceneGraphService sceneGraphService,
      ClipboardService clipboardService,
      RegistrationService registrationService,
      MenuController menuController) {
    super();
    this.editorJmeApplication = editorJmeApplication;
    this.sceneTreeService = sceneTreeService;
    this.sceneGraphService = sceneGraphService;
    this.clipboardService = clipboardService;
    this.spatial = (Spatial) sceneGraphService.getSelectedObject();

    JMenuItem lookAtItem = add(new JMenuItem("Look at Spatial"));
    lookAtItem.addActionListener(e -> {
      editorJmeApplication.getStateManager().getState(EditorCameraState.class)
          .lookAt(spatial.getWorldTranslation(), Vector3f.UNIT_Y);
    });

    addMenu = createAddMenu();
    add(addMenu);

    add(new JSeparator());

    JMenuItem cutItem = add(new JMenuItem("Cut"));
    cutItem.addActionListener(e -> {

      if (spatial.getUserData(TreeConstants.TREE_ROOT) != null) {

        JOptionPane.showMessageDialog(null,
            "You cannot cut a root tree element.",
            "Action Denied",
            JOptionPane.ERROR_MESSAGE);

        return;
      }

      editorJmeApplication.enqueue(() -> {

        // Clone the spatial on the JME thread.
        SpatialClipboardItem spatialClipboardItem = new SpatialClipboardItem(spatial);

        // Put the spatial in the clipboard on the AWT Thread.
        // Remove the treeItem on the AWT thread.
        SwingUtilities.invokeLater(() -> {
          sceneGraphService.remove(spatial);
          clipboardService.setSpatialClipboardItem(spatialClipboardItem);
        });

      });

    });

    add(createCopyMenu());

    add(new JSeparator());

    JMenuItem saveItem = add(new JMenuItem("Save..."));
    saveItem.addActionListener(e -> {

      SaveSpatial saveSpatial = new SaveSpatial(spatial);

      JFrame mainWindow = (JFrame) SwingUtilities
          .getWindowAncestor(editorJmeApplication.getAWTPanel());

      JDialog dialog = new JDialog(mainWindow, "Save Spatial", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setContentPane(saveSpatial.$$$getRootComponent$$$());
      dialog.setLocationRelativeTo(mainWindow);
      dialog.pack();

      dialog.setVisible(true);

    });
    saveItem.setMnemonic('S');

    add(new JSeparator());

    JMenuItem deleteItem = add(new JMenuItem("Delete"));
    deleteItem.addActionListener(e -> {

      // we must only query scene object in the JME thread,
      editorJmeApplication.enqueue(() -> {

        // verify we're allowed to delete this object.
        final boolean isDeletable = spatial.getUserData(TreeConstants.UNDELETABLE_FLAG) == null;

        SwingUtilities.invokeLater(() -> {

          if (isDeletable) {
            sceneGraphService.remove((Spatial) sceneGraphService.getSelectedObject());
          } else {
            JOptionPane.showMessageDialog(null,
                "You are not allowed to delete this spatial.",
                "Delete Rejected",
                JOptionPane.ERROR_MESSAGE);
          }

        });


      });

    });
    deleteItem.setMnemonic('D');

    // Add -> Registered Spatials

    for (ControlRegistrar registrar : registrationService.getControlRegistration()
        .getRegistrations()) {

      JMenuItem menuItem = getAddMenu()
          .add(new JMenuItem(registrar.getRegisteredClass().getSimpleName()));

      menuItem.addActionListener(e -> {

        Control control = registrar
            .createInstance(editorJmeApplication);
        sceneGraphService.add(control, (Node) sceneGraphService.getSelectedObject());

      });

    }

    // Allow users to also add their options....
    List<JMenuItem> customItems = menuController.getCustomMenuItems(SpatialTreeNode.class);

    if (customItems != null && !customItems.isEmpty()) {

      // add a separator for clarity.
      add(new JSeparator());

      for (JMenuItem customItem : customItems) {
        add(customItem);
      }
    }

  }

  public JMenu getAddMenu() {
    return addMenu;
  }

  private JMenu createAddMenu() {

    JMenu menu = new JMenu("Add...");
    menu.setMnemonic('A');

    JMenu lightMenu = createLightMenu();
    menu.add(lightMenu);

    return menu;
  }

  private JMenu createLightMenu() {

    JMenu menu = new JMenu("Light...");
    menu.setMnemonic('L');

    JMenuItem ambLight = menu.add(new JMenuItem("Ambient"));
    ambLight.addActionListener(e -> {
      sceneGraphService.add(new AmbientLight(), spatial);
    });

    JMenuItem dirLight = menu.add(new JMenuItem("Directional"));
    dirLight.addActionListener(e -> {
      DirectionalLight directionalLight = new DirectionalLight(
          new Vector3f(-1, -1, -1).normalizeLocal());
      sceneGraphService.add(directionalLight, spatial);
    });

    JMenuItem pointLight = menu.add(new JMenuItem("Point"));
    pointLight.addActionListener(e -> {

      // This is a bit misleading because of multi-threading. I can't query the camera location on the AWT thread
      // but I need to set its position to the camera position, so it will be set when it's attached - which will
      // be on the JME thread.
      PointLight light = new PointLight(new Vector3f(0, 0, 0), 10);
      sceneGraphService.add(light, spatial);

    });
    GenerateLightProbeDialog generateLightProbeDialog = new GenerateLightProbeDialog(
        editorJmeApplication, sceneGraphService, sceneTreeService, this);
    JMenuItem probeLight = menu.add(new JMenuItem("Generate LightProbe..."));
    probeLight.addActionListener(e -> GUIUtils.createDialog(SwingUtilities
            .getWindowAncestor(this), generateLightProbeDialog.$$$getRootComponent$$$(),
        "Generate LightProbe"));

    return menu;
  }

  private JMenu createCopyMenu() {

    JMenu menu = new JMenu("Copy...");

    // copying types:
    // clone (new material)
    // clone (same material)
    // clone (same mesh, new material)
    // clone (same mesh, same material)

    JMenuItem cloneWithNewMaterial = menu.add(new JMenuItem("New Mesh(es), New Material(s)"));
    cloneWithNewMaterial.addActionListener(e -> {

      editorJmeApplication.enqueue(() -> {
        SpatialClipboardItem spatialClipboardItem = new SpatialClipboardItem(spatial, true,
            true);
        SwingUtilities
            .invokeLater(() -> clipboardService.setSpatialClipboardItem(spatialClipboardItem));
      });

    });

    JMenuItem cloneWithSameMaterial = menu.add(new JMenuItem("New Mesh(es), Same Material(s)"));
    cloneWithSameMaterial.addActionListener(e -> {

      editorJmeApplication.enqueue(() -> {
        SpatialClipboardItem spatialClipboardItem = new SpatialClipboardItem(spatial, false,
            true);
        SwingUtilities
            .invokeLater(() -> clipboardService.setSpatialClipboardItem(spatialClipboardItem));
      });

    });

    JMenuItem cloneWithSameMeshNewMaterial = menu
        .add(new JMenuItem("Same Mesh(es), New Material(s)"));
    cloneWithSameMeshNewMaterial.addActionListener(e -> {

      SpatialClipboardItem spatialClipboardItem = new SpatialClipboardItem(spatial, true,
          false);
      SwingUtilities
          .invokeLater(() -> clipboardService.setSpatialClipboardItem(spatialClipboardItem));

    });

    JMenuItem cloneWithSameMeshSameMaterial = menu
        .add(new JMenuItem("Same Mesh(es), Same Material(s)"));
    cloneWithSameMeshSameMaterial.addActionListener(e -> {

      SpatialClipboardItem spatialClipboardItem = new SpatialClipboardItem(spatial, false,
          false);
      SwingUtilities
          .invokeLater(() -> clipboardService.setSpatialClipboardItem(spatialClipboardItem));

    });

    return menu;
  }

}
