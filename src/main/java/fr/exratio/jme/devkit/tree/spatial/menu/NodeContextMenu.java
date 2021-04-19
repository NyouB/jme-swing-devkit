package fr.exratio.jme.devkit.tree.spatial.menu;

import static devkit.appstate.tool.SpatialMoveToolState.COLOR;
import static devkit.appstate.tool.SpatialMoveToolState.MAT_DEF;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.action.CreateBoxAction;
import fr.exratio.jme.devkit.action.CreateCylinderAction;
import fr.exratio.jme.devkit.action.CreateDomeAction;
import fr.exratio.jme.devkit.action.CreateQuadAction;
import fr.exratio.jme.devkit.action.CreateSphereAction;
import fr.exratio.jme.devkit.forms.AddModels;
import fr.exratio.jme.devkit.forms.CreateSkyBoxDialog;
import fr.exratio.jme.devkit.registration.spatial.GeometryRegistrar;
import fr.exratio.jme.devkit.registration.spatial.NodeRegistrar;
import fr.exratio.jme.devkit.service.ClipboardService;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.MenuController;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import java.awt.HeadlessException;
import java.util.List;
import java.util.Set;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;


public class NodeContextMenu extends SpatialContextMenu {

  private final CreateBoxAction createBoxAction;
  private final CreateCylinderAction createCylinderAction;
  private final CreateDomeAction createDomeAction;
  private final CreateQuadAction createQuadAction;
  private final CreateSphereAction createSphereAction;

  public NodeContextMenu(CreateBoxAction createBoxAction,
      CreateCylinderAction createCylinderAction,
      CreateDomeAction createDomeAction,
      CreateQuadAction createQuadAction,
      CreateSphereAction createSphereAction) throws HeadlessException {
    super();
    this.createBoxAction = createBoxAction;
    this.createCylinderAction = createCylinderAction;
    this.createDomeAction = createDomeAction;
    this.createQuadAction = createQuadAction;
    this.createSphereAction = createSphereAction;
    // Add -> Shape
    JMenu addShapeMenu = (JMenu) getAddMenu().add(new JMenu("Shape..."));
    addShapes(addShapeMenu);
    addShapeMenu.setMnemonic('S');

    // Add -> Model(s)...
    JMenuItem addModelsItem = getAddMenu().add(new JMenuItem("Model(s)..."));
    addModelsItem.addActionListener(e -> {

      AddModels addModels = new AddModels(nodeTreeNode);

      JFrame mainWindow = (JFrame) SwingUtilities
          .getWindowAncestor(ServiceManager.getService(EditorJmeApplication.class).getAWTPanel());

      JDialog dialog = new JDialog(mainWindow, "Add Model(s)", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setContentPane(addModels.$$$getRootComponent$$$());
      dialog.pack();
      dialog.setLocationRelativeTo(mainWindow);
      dialog.setVisible(true);

    });
    addModelsItem.setMnemonic('M');

    // Add -> SkyBox...
    JMenuItem genSkyBoxItem = getAddMenu().add(new JMenuItem("SkyBox..."));
    genSkyBoxItem.addActionListener(e -> {
      CreateSkyBoxDialog createSkyBoxDialog = new CreateSkyBoxDialog(nodeTreeNode);

      JFrame mainWindow = (JFrame) SwingUtilities
          .getWindowAncestor(ServiceManager.getService(EditorJmeApplication.class).getAWTPanel());

      JDialog dialog = new JDialog(mainWindow, "Create SkyBox", true);
      dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      dialog.setContentPane(createSkyBoxDialog.$$$getRootComponent$$$());
      dialog.pack();
      dialog.setLocationRelativeTo(mainWindow);
      dialog.setVisible(true);
    });
    genSkyBoxItem.setMnemonic('K');

    // Add -> Registered Spatials
    RegistrationService registrationService = ServiceManager.getService(RegistrationService.class);
    Set<NodeRegistrar> nodeRegistrars = registrationService.getNodeRegistration()
        .getRegistrations();

    if (!nodeRegistrars.isEmpty()) {

      getAddMenu().add(new JSeparator());

      for (NodeRegistrar registrar : nodeRegistrars) {

        JMenuItem menuItem = getAddMenu()
            .add(new JMenuItem(registrar.getRegisteredClass().getSimpleName()));

        menuItem.addActionListener(e -> {

          Node node = registrar.createInstance(ServiceManager.getService(EditorJmeApplication.class));
          ServiceManager.getService(SceneGraphService.class).addSpatial(node, nodeTreeNode.getUserObject());

        });

      }
    }

    Set<GeometryRegistrar> geometryRegistrars = registrationService.getGeometryRegistration()
        .getRegistrations();
    if (!geometryRegistrars.isEmpty()) {

      getAddMenu().add(new JSeparator());

      for (GeometryRegistrar registrar : geometryRegistrars) {

        JMenuItem menuItem = getAddMenu()
            .add(new JMenuItem(registrar.getRegisteredClass().getSimpleName()));

        menuItem.addActionListener(e -> {

          Geometry geometry = registrar
              .createInstance(ServiceManager.getService(EditorJmeApplication.class));
          ServiceManager.getService(SceneGraphService.class).addSpatial(geometry, nodeTreeNode.getUserObject());

        });

      }

    }

    add(new JSeparator());

    JMenuItem pasteItem = add(new JMenuItem("Paste"));
    pasteItem
        .setEnabled(ServiceManager.getService(ClipboardService.class).hasSpatialClipboardItem());
    pasteItem.addActionListener(e -> {

      Spatial clonedSpatial = ServiceManager.getService(ClipboardService.class)
          .getSpatialClipboardItem().getClonedCopy();
      ServiceManager.getService(SceneGraphService.class).addSpatial(clonedSpatial, nodeTreeNode.getUserObject());

    });

    // Allow users to also add their options....
    List<JMenuItem> customItems = ServiceManager.getService(MenuController.class)
        .getCustomMenuItems(NodeTreeNode.class);

    if (customItems != null && !customItems.isEmpty()) {

      // add a separator for clarity.
      add(new JSeparator());

      for (JMenuItem customItem : customItems) {
        add(customItem);
      }
    }
  }

  private void addShapes(JMenu parent) {


  }
}
