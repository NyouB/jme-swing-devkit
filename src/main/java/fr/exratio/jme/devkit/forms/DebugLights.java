package fr.exratio.jme.devkit.forms;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.Light;
import com.jme3.light.LightList;
import com.jme3.light.LightProbe;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.core.ColorConverter;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import org.springframework.stereotype.Controller;

@Controller
public class DebugLights extends JPanel {

  public static final String DEBUG_LIGHTS_WINDOW_TITLE = "Debug Lights";

  private JPanel rootPanel;

  private AmbientLight ambientLight;
  private DirectionalLight directionalLight;
  private LightProbe lightProbe;
  private final EditorJmeApplication editorJmeApplication;

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JTabbedPane tabbedPane1;

  public LightProbe extractProbe(DemoProbe demoProbe) {

    Spatial probeHolder = editorJmeApplication.getAssetManager()
        .loadModel(demoProbe.getResourcePath());

    LightProbe lightProbe = (LightProbe) probeHolder.getLocalLightList().get(0);
    probeHolder.removeLight(lightProbe);

    lightProbe.getArea().setRadius(500);
    lightProbe.setName(demoProbe.resourcePath);

    return lightProbe;
  }

  private void querySceneForDebugLights() {

    editorJmeApplication.enqueue(() -> {

      // read the lights from the JME thread.
      LightList lights = editorJmeApplication.getRootNode().getLocalLightList();

      for (Light light : lights) {

        if (light instanceof AmbientLight) {

          // set the ambientLight reference from the JME thread.
          ambientLight = (AmbientLight) light;
          final ColorRGBA colorRGBA = ambientLight.getColor();

          // set the JavaFX value from the JavaFX thread.
          SwingUtilities.invokeLater(() -> {
            ambientCheckBox.setSelected(true);
            ambientColorChooser.setColor(ColorConverter.toColor(colorRGBA));
          });
        } else if (light instanceof DirectionalLight) {

          // set the ambientLight reference from the JME thread.
          directionalLight = (DirectionalLight) light;
          final ColorRGBA colorRGBA = directionalLight.getColor();

          // set the JavaFX value from the JavaFX thread.
          SwingUtilities.invokeLater(() -> {
            directionalCheckBox.setSelected(true);
            directionalColorChooser.setColor(ColorConverter.toColor(colorRGBA));
          });
        } else if (light instanceof LightProbe) {

          // set the ambientLight reference from the JME thread.
          lightProbe = (LightProbe) light;
          final String name = lightProbe.getName();

          if (name != null) {

            // set the JavaFX value from the JavaFX thread.
            SwingUtilities.invokeLater(() -> {
              probeCheckBox.setSelected(true);
              //DemoProbe demoProbe = DemoProbe.fromResourcePath(name);
              //probeChoiceBox.getSelectionModel().select(demoProbe);
            });

          }

        }

      }

    });

  }


  private enum DemoProbe {

    Bathroom("Probes/bathroom.j3o"),
    City_Night_Lights("Probes/City_Night_Lights.j3o"),
    Corsica_Beach("Probes/corsica_beach.j3o"),
    Dresden_Station_Night("Probes/dresden_station_night.j3o"),
    Flower_Road("Probes/flower_road.j3o"),
    Glass_Passage("Probes/glass_passage.j3o"),
    Parking_Lot("Probes/Parking_Lot.j3o"),
    River_Road("Probes/River_Road.j3o"),
    Road_In_Tenerife_Mountain("Probes/road_in_tenerife_mountain.j3o"),
    Sky_Cloudy("Probes/Sky_Cloudy.j3o"),
    StoneWall("Probes/Stonewall.j3o"),
    Studio("Probes/studio.j3o");

    private final String resourcePath;

    DemoProbe(String resourcePath) {
      this.resourcePath = resourcePath;
    }

    public static DemoProbe fromResourcePath(String resourcePath) {

      for (DemoProbe demoProbe : values()) {
        if (demoProbe.getResourcePath().equals(resourcePath)) {
          return demoProbe;
        }
      }

      return null;
    }

    public String getResourcePath() {
      return resourcePath;
    }

  }

  private JCheckBox ambientCheckBox;
  private JColorChooser ambientColorChooser;
  private JCheckBox directionalCheckBox;
  private JColorChooser directionalColorChooser;
  private JCheckBox probeCheckBox;
  private JComboBox probesComboBox;

  public DebugLights(EditorJmeApplication editorJmeApplication) {
    initComponents();
    this.editorJmeApplication = editorJmeApplication;

    // we don't want preview panels.
    ambientColorChooser.setPreviewPanel(new JPanel());
    directionalColorChooser.setPreviewPanel(new JPanel());

    // we don't need opacity with lights.
//        for (AbstractColorChooserPanel ccPanel : ambientColorChooser.getChooserPanels()) {
//            ccPanel.setColorTransparencySelectionEnabled(
//                    false);
//        }
//
//        for (AbstractColorChooserPanel ccPanel : directionalColorChooser.getChooserPanels()) {
//            ccPanel.setColorTransparencySelectionEnabled(
//                    false);
//        }

    DefaultComboBoxModel<DemoProbe> listModel = new DefaultComboBoxModel<>();
    for (DemoProbe demoProbe : DemoProbe.values()) {
      listModel.addElement(demoProbe);
    }
    probesComboBox.setModel(listModel);
    probesComboBox.addActionListener(e -> {

      final boolean isSelected = probeCheckBox.isSelected();

      JComboBox<DemoProbe> comboBox = (JComboBox<DemoProbe>) e.getSource();
      final DemoProbe demoProbe = (DemoProbe) comboBox.getSelectedItem();

      editorJmeApplication.enqueue(() -> {

        if (lightProbe != null) {
          editorJmeApplication.getRootNode().removeLight(lightProbe);
        }

        if (isSelected && demoProbe != null) {
          lightProbe = extractProbe(demoProbe);
          editorJmeApplication.getRootNode().addLight(lightProbe);
        }

      });

    });

    ambientColorChooser.getSelectionModel().addChangeListener(e -> {
      DefaultColorSelectionModel colorChooser = (DefaultColorSelectionModel) e.getSource();
      final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(colorChooser.getSelectedColor());

      editorJmeApplication.enqueue(() -> {

        if (ambientLight != null) {
          ambientLight.setColor(colorRGBA);
        }

      });

    });

    directionalColorChooser.getSelectionModel().addChangeListener(e -> {
      DefaultColorSelectionModel colorChooser = (DefaultColorSelectionModel) e.getSource();
      final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(colorChooser.getSelectedColor());

      editorJmeApplication.enqueue(() -> {

        if (directionalLight != null) {
          directionalLight.setColor(colorRGBA);
        }

      });

    });

    ambientCheckBox.addActionListener(e -> {

      JCheckBox checkBox = (JCheckBox) e.getSource();

      final boolean isSelected = checkBox.isSelected();
      final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(ambientColorChooser.getColor());

      editorJmeApplication.enqueue(() -> {

        if (isSelected) {

          if (ambientLight == null) {
            ambientLight = new AmbientLight(colorRGBA);
            editorJmeApplication.getRootNode().addLight(ambientLight);
          } else {
            ambientLight.setColor(colorRGBA);
          }
        } else {

          if (ambientLight != null) {
            editorJmeApplication.getRootNode().removeLight(ambientLight);
            ambientLight = null;
          }
        }

      });

    });

    directionalCheckBox.addActionListener(e -> {

      JCheckBox checkBox = (JCheckBox) e.getSource();

      final boolean isSelected = checkBox.isSelected();
      final ColorRGBA colorRGBA = ColorConverter.toColorRGBA(directionalColorChooser.getColor());

      editorJmeApplication.enqueue(() -> {

        if (isSelected) {

          if (directionalLight == null) {
            directionalLight = new DirectionalLight(new Vector3f(-1, -1, -1).normalizeLocal(),
                colorRGBA);
            editorJmeApplication.getRootNode().addLight(directionalLight);
          } else {
            directionalLight.setColor(colorRGBA);
          }
        } else {

          if (directionalLight != null) {
            editorJmeApplication.getRootNode().removeLight(directionalLight);
            directionalLight = null;
          }
        }

      });

    });

    probeCheckBox.addActionListener(e -> {

      JCheckBox checkBox = (JCheckBox) e.getSource();

      final boolean isSelected = checkBox.isSelected();
      final DemoProbe demoProbe = (DemoProbe) probesComboBox.getSelectedItem();

      editorJmeApplication.enqueue(() -> {

        if (isSelected) {

          if (lightProbe != null) {
            editorJmeApplication.getRootNode().removeLight(lightProbe);
          }

          if (demoProbe != null) {
            lightProbe = extractProbe(demoProbe);
            editorJmeApplication.getRootNode().addLight(lightProbe);
          }

        } else {
          if (lightProbe != null) {
            editorJmeApplication.getRootNode().removeLight(lightProbe);
            lightProbe = null;
          }
        }

      });

    });

    querySceneForDebugLights();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    tabbedPane1 = new JTabbedPane();
    var panel1 = new JPanel();
    ambientCheckBox = new JCheckBox();
    ambientColorChooser = new JColorChooser();
    var panel2 = new JPanel();
    directionalCheckBox = new JCheckBox();
    directionalColorChooser = new JColorChooser();
    var panel3 = new JPanel();
    probeCheckBox = new JCheckBox();
    var vSpacer1 = new Spacer();
    var label1 = new JLabel();
    probesComboBox = new JComboBox();
    var hSpacer1 = new Spacer();
    var vSpacer2 = new Spacer();
    var hSpacer2 = new Spacer();

    //======== this ========
    setLayout(new GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));

    //======== tabbedPane1 ========
    {

      //======== panel1 ========
      {
        panel1.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));

        //---- ambientCheckBox ----
        ambientCheckBox.setText("Enabled");
        panel1.add(ambientCheckBox, new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null));
        panel1.add(ambientColorChooser, new GridConstraints(1, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
      }
      tabbedPane1.addTab("Ambient", panel1);

      //======== panel2 ========
      {
        panel2.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));

        //---- directionalCheckBox ----
        directionalCheckBox.setText("Enabled");
        panel2.add(directionalCheckBox, new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null));
        panel2.add(directionalColorChooser, new GridConstraints(1, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            null, null, null));
      }
      tabbedPane1.addTab("Directional", panel2);

      //======== panel3 ========
      {
        panel3.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));

        //---- probeCheckBox ----
        probeCheckBox.setText("Enabled");
        panel3.add(probeCheckBox, new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null));
        panel3.add(vSpacer1, new GridConstraints(2, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
            GridConstraints.SIZEPOLICY_CAN_SHRINK,
            GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
            null, null, null));

        //---- label1 ----
        label1.setText("Probe");
        panel3.add(label1, new GridConstraints(1, 0, 1, 1,
            GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE,
            GridConstraints.SIZEPOLICY_FIXED,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null));
        panel3.add(probesComboBox, new GridConstraints(1, 1, 1, 1,
            GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_FIXED,
            null, null, null));
        panel3.add(hSpacer1, new GridConstraints(1, 2, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
            GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK,
            null, null, null));
      }
      tabbedPane1.addTab("LightProbe", panel3);
    }
    add(tabbedPane1, new GridConstraints(0, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
        null, new Dimension(200, 200), null));
    add(vSpacer2, new GridConstraints(1, 0, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        null, null, null));
    add(hSpacer2, new GridConstraints(0, 1, 1, 1,
        GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL,
        GridConstraints.SIZEPOLICY_CAN_GROW | GridConstraints.SIZEPOLICY_WANT_GROW,
        GridConstraints.SIZEPOLICY_CAN_SHRINK,
        null, null, null));
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
