package fr.exratio.jme.devkit.tree.spatial.menu;

import com.google.common.eventbus.EventBus;
import com.jme3.scene.BatchNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.action.CreateBoxAction;
import fr.exratio.jme.devkit.action.CreateCylinderAction;
import fr.exratio.jme.devkit.action.CreateDomeAction;
import fr.exratio.jme.devkit.action.CreateQuadAction;
import fr.exratio.jme.devkit.action.CreateSphereAction;
import fr.exratio.jme.devkit.action.RemoveItemAction;
import fr.exratio.jme.devkit.event.BatchEvent;
import fr.exratio.jme.devkit.forms.AddModels;
import fr.exratio.jme.devkit.forms.CreateSkyBoxDialog;
import fr.exratio.jme.devkit.registration.spatial.GeometryRegistrar;
import fr.exratio.jme.devkit.registration.spatial.NodeRegistrar;
import fr.exratio.jme.devkit.service.ClipboardService;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.MenuController;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.service.SceneGraphService;
import fr.exratio.jme.devkit.tree.spatial.NodeTreeNode;
import fr.exratio.jme.devkit.util.GUIUtils;
import java.awt.HeadlessException;
import java.util.List;
import java.util.Set;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class NodeContextMenu extends SpatialContextMenu {

  private final CreateQuadAction createQuadAction;
  private final CreateDomeAction createDomeAction;
  private final CreateCylinderAction createCylinderAction;
  private final CreateSphereAction createSphereAction;
  private final CreateBoxAction createBoxAction;
  private final EventBus eventBus;

  @Autowired
  public NodeContextMenu(
      RemoveItemAction removeItemAction, AddModels addModels,
      CreateSkyBoxDialog createSkyBoxDialog,
      RegistrationService registrationService,
      SceneGraphService sceneGraphService,
      EditorJmeApplication editorJmeApplication,
      ClipboardService clipboardService,
      MenuController menuController,
      CreateQuadAction createQuadAction,
      CreateDomeAction createDomeAction,
      CreateCylinderAction createCylinderAction,
      CreateSphereAction createSphereAction,
      CreateBoxAction createBoxAction, EventBus eventBus) throws HeadlessException {
    super(editorJmeApplication, sceneGraphService, clipboardService,
        registrationService, menuController);
    this.createQuadAction = createQuadAction;
    this.createDomeAction = createDomeAction;
    this.createCylinderAction = createCylinderAction;
    this.createSphereAction = createSphereAction;
    this.createBoxAction = createBoxAction;
    this.eventBus = eventBus;

    // Add -> Shape
    JMenu addShapeMenu = (JMenu) getAddMenu().add(new JMenu("Shape..."));
    addShapes(addShapeMenu);
    addShapeMenu.setMnemonic('S');

    // Add -> Model(s)...
    JMenuItem addModelsItem = getAddMenu().add(new JMenuItem("Model(s)..."));
    addModelsItem.addActionListener(e -> GUIUtils.createDialog(SwingUtilities
        .getWindowAncestor(this), addModels, "Add Model(s)"));
    addModelsItem.setMnemonic('M');

    // Add -> SkyBox...
    JMenuItem genSkyBoxItem = getAddMenu().add(new JMenuItem("SkyBox..."));
    genSkyBoxItem.addActionListener(e -> GUIUtils.createDialog(SwingUtilities
        .getWindowAncestor(this), createSkyBoxDialog, "Create SkyBox"));
    genSkyBoxItem.setMnemonic('K');

    // Add -> Registered Spatials
    Set<NodeRegistrar> nodeRegistrars = registrationService.getNodeRegistration()
        .getRegistrations();

    if (!nodeRegistrars.isEmpty()) {

      getAddMenu().add(new JSeparator());

      for (NodeRegistrar registrar : nodeRegistrars) {

        JMenuItem menuItem = getAddMenu()
            .add(new JMenuItem(registrar.getRegisteredClass().getSimpleName()));

        menuItem.addActionListener(e -> {
//todo make a command for node creation or move node creation to a specific view like in godot
          Node node = registrar.createInstance(editorJmeApplication);
          sceneGraphService.add(node, (Node) sceneGraphService.getSelectedObject());

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
          Geometry geometry = registrar.createInstance(editorJmeApplication);
          sceneGraphService.add(geometry, (Node) sceneGraphService.getSelectedObject());

        });

      }

    }

    add(new JSeparator());

    JMenuItem pasteItem = add(new JMenuItem("Paste"));
    pasteItem
        .setEnabled(clipboardService.hasSpatialClipboardItem());
    pasteItem.addActionListener(e -> {

      Spatial clonedSpatial = clipboardService.getSpatialClipboardItem().getClonedCopy();
      sceneGraphService.add(clonedSpatial, (Node) sceneGraphService.getSelectedObject());

    });

    // Allow users to also add their options....
    List<JMenuItem> customItems = menuController.getCustomMenuItems(NodeTreeNode.class);

    if (customItems != null && !customItems.isEmpty()) {

      // add a separator for clarity.
      add(new JSeparator());

      for (JMenuItem customItem : customItems) {
        add(customItem);
      }
    }

    JMenuItem deleteItem = add(new JMenuItem("Delete"));
    deleteItem.setAction(removeItemAction);

    add(new JSeparator());

    JMenuItem batchItem = add(new JMenuItem("Batch"));
    batchItem
        .addActionListener(e -> editorJmeApplication.enqueue(() -> {
          ((BatchNode) sceneGraphService.getSelectedObject()).batch();
          eventBus.post(new BatchEvent(sceneGraphService.getSelectedObject()));
        }));

  }

  private void addShapes(JMenu parent) {
    JMenuItem cubeItem = parent.add(new JMenuItem("Cube"));
    cubeItem.setAction(createBoxAction);

    JMenuItem cylinderItem = parent.add(new JMenuItem("Cylinder"));
    cylinderItem.setAction(createCylinderAction);

    JMenuItem domeItem = parent.add(new JMenuItem("Dome"));
    domeItem.setAction(createDomeAction);

    JMenuItem quadItem = parent.add(new JMenuItem("Quad"));
    quadItem.setAction(createQuadAction);

    JMenuItem sphereItem = parent.add(new JMenuItem("Sphere"));
    sphereItem.setAction(createSphereAction);
  }


}
