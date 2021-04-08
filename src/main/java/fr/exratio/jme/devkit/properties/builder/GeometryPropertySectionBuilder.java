package fr.exratio.jme.devkit.properties.builder;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.bool.BooleanEditor;
import fr.exratio.jme.devkit.properties.component.integer.IntegerEditor;
import fr.exratio.jme.devkit.properties.component.material.MaterialChooserEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.inspector.PropertyInspectorTool;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeometryPropertySectionBuilder extends AbstractPropertySectionBuilder<Geometry> {

  public static final String LOCAL_SCALE = "localScale";
  public static final String IGNORE_TRANSFORM = "ignoreTransform";
  public static final String CULL_HINT = "cullHint";
  public static final String LOD_LEVEL = "lodLevel";
  public static final String GEOMETRY = "Geometry";
  public static final String MATERIAL = "Material";
  private final EditorJmeApplication editorJmeApplication;
  private final SpatialPropertySectionBuilder spatialPropertySectionBuilder;
  private final MaterialPropertySectionBuilder materialPropertySectionBuilder;
  private final PropertyInspectorTool propertyInspectorTool;


  @Autowired
  public GeometryPropertySectionBuilder(
      EditorJmeApplication editorJmeApplication,
      SpatialPropertySectionBuilder spatialPropertySectionBuilder,
      MaterialPropertySectionBuilder materialPropertySectionBuilder,
      PropertyInspectorTool propertyInspectorTool) {
    this.editorJmeApplication = editorJmeApplication;
    this.spatialPropertySectionBuilder = spatialPropertySectionBuilder;
    this.materialPropertySectionBuilder = materialPropertySectionBuilder;
    this.propertyInspectorTool = propertyInspectorTool;
  }

  @Override
  public AbstractPropertySectionBuilder<Geometry> withObject(Geometry object) {
    super.withObject(object);
    spatialPropertySectionBuilder.withObject(object);
    return this;
  }

  @Override
  public List<PropertySection> build() {
    List<PropertySection> propertySections = spatialPropertySectionBuilder.build();
    // Geometry-specific data
    PropertySection geometrySection = new PropertySection(GEOMETRY);
    BooleanEditor ignoreTransform = new BooleanEditor(object.isIgnoreTransform());
    ignoreTransform.addPropertyChangeListener(value -> editorJmeApplication
        .enqueue(() -> object.setIgnoreTransform((Boolean) value.getNewValue())));
    geometrySection.addProperty(IGNORE_TRANSFORM, ignoreTransform.getCustomEditor());

    boolean isLODSet = object.getMesh().getNumLodLevels() == 0;
    IntegerEditor lodLevel = new IntegerEditor(object.getLodLevel());
    String lodLevelpropertyName = LOD_LEVEL + " THE LOD LEVEL ARE NOT SET";
    if (isLODSet) {
      lodLevelpropertyName = LOD_LEVEL + " THE LOD LEVEL ARE SET";
      lodLevel.addPropertyChangeListener(
          value -> editorJmeApplication
              .enqueue(() -> object.setLodLevel((Integer) value.getNewValue())));
    }
    geometrySection.addProperty(lodLevelpropertyName, lodLevel.getCustomEditor());

    // Material chooser.
    MaterialChooserEditor materialChooser = new MaterialChooserEditor(object.getMaterial(),
        editorJmeApplication);

    // This listener trigger the update of material detail on material selection
    PropertyChangeListener materialChangeListener = evt -> {
      Material material = (Material) evt.getNewValue();
      List<PropertySection> sections = buildMaterialSection(material);
      if (!sections.isEmpty()) {
        return;
      }
      propertyInspectorTool.updateSections(sections);
    };

    materialChooser.addPropertyChangeListener(materialChangeListener);
    //this listener juste propagate the material change to the jmeEngine
    materialChooser.addPropertyChangeListener(
        value -> editorJmeApplication.enqueue(() -> object.setMaterial(
                (Material) value.getNewValue())));

    geometrySection.addProperty(MATERIAL, materialChooser.getCustomEditor());

    propertySections.add(geometrySection);

    // Material

    Material material = object.getMaterial();
    List<PropertySection> sections = buildMaterialSection(material);
    if (!sections.isEmpty()) {
      propertySections.addAll(sections);
    }

    return propertySections;
  }

  public List<PropertySection> buildMaterialSection(Material material) {
    List<PropertySection> sections = new ArrayList<>();
    if (material != null) {
      sections = materialPropertySectionBuilder.withObject(material).build();
    }
    return sections;
  }
}
