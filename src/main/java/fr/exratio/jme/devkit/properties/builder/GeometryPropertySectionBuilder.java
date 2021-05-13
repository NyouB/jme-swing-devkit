package fr.exratio.jme.devkit.properties.builder;

import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import fr.exratio.jme.devkit.properties.MaterialSection;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.bool.BooleanEditor;
import fr.exratio.jme.devkit.properties.component.integer.IntegerEditor;
import fr.exratio.jme.devkit.properties.component.material.MaterialChooserEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
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
  private final ReflectedPropertySectionBuilder reflectedPropertySectionBuilder;


  @Autowired
  public GeometryPropertySectionBuilder(
      EditorJmeApplication editorJmeApplication,
      SpatialPropertySectionBuilder spatialPropertySectionBuilder,
      MaterialPropertySectionBuilder materialPropertySectionBuilder,
      ReflectedPropertySectionBuilder reflectedPropertySectionBuilder) {
    this.editorJmeApplication = editorJmeApplication;
    this.spatialPropertySectionBuilder = spatialPropertySectionBuilder;
    this.materialPropertySectionBuilder = materialPropertySectionBuilder;
    this.reflectedPropertySectionBuilder = reflectedPropertySectionBuilder;
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

    // Material

    Material material = object.getMaterial();
    MaterialSection materialSection = new MaterialSection(material,
        reflectedPropertySectionBuilder);

    // Material chooser.
    MaterialChooserEditor materialChooser = new MaterialChooserEditor(material,
        editorJmeApplication);

    materialChooser.addPropertyChangeListener(evt -> {
      materialSection.updateSection(material);
    });
    //this listener juste propagate the material change to the jmeEngine
    materialChooser.addPropertyChangeListener(
        value -> editorJmeApplication.enqueue(() -> object.setMaterial(
            (Material) value.getNewValue())));

    propertySections.add(geometrySection);
    geometrySection.addProperty(MATERIAL, materialChooser.getCustomEditor());
    propertySections.add(materialSection);

    return propertySections;
  }

}
