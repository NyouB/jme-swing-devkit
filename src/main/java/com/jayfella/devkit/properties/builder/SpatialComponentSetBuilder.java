package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.BooleanComponent;
import com.jayfella.devkit.properties.component.EnumComponent;
import com.jayfella.devkit.properties.component.MaterialChooserComponent;
import com.jayfella.devkit.properties.component.QuaternionComponent;
import com.jayfella.devkit.properties.component.StringComponent;
import com.jayfella.devkit.properties.component.Vector3fComponent;
import com.jayfella.devkit.properties.component.events.SpatialNameChangedEvent;
import com.jayfella.devkit.service.EventService;
import com.jayfella.devkit.service.PropertyInspectorService;
import com.jayfella.devkit.service.ServiceManager;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;

public class SpatialComponentSetBuilder extends AbstractComponentSetBuilder<Spatial> {

  public static final String LOCAL_ROTATION = "localRotation";
  public static final String LOCAL_TRANSLATION = "localTranslation";
  public static final String LOCAL_SCALE = "localScale";
  public static final String IGNORE_TRANSFORM = "ignoreTransform";
  public static final String NAME = "name";
  public static final String CULL_HINT = "cullHint";
  public static final String SHADOW_MODE = "shadowMode";
  public static final String QUEUE_BUCKET = "queueBucket";
  public static final String BATCH_HINT = "batchHint";
  public static final String MATERIAL = "material";

  public SpatialComponentSetBuilder(Spatial object, String... ignoredProperties) {
    super(object, ignoredProperties);
  }

  @Override
  public List<PropertySection> build() {

    List<PropertySection> propertySections = new ArrayList<>();

    // Transform : location, rotation, scale

    Vector3fComponent localTranslation = new Vector3fComponent(object.getLocalTranslation());
    localTranslation.setPropertyName(LOCAL_TRANSLATION);
    bind(FieldUtils.getField(Spatial.class, LOCAL_TRANSLATION), localTranslation);

    QuaternionComponent localRotation = new QuaternionComponent(object.getLocalRotation());
    localRotation.setPropertyName(LOCAL_ROTATION);
    bind(FieldUtils.getField(Spatial.class, LOCAL_ROTATION), localRotation);

    Vector3fComponent localScale = new Vector3fComponent(object.getLocalScale());
    localScale.setPropertyName(LOCAL_SCALE);
    bind(FieldUtils.getField(Spatial.class, LOCAL_SCALE), localScale);

    PropertySection transformSection = new PropertySection("Transform", localTranslation,
        localRotation, localScale);
    propertySections.add(transformSection);

    // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint
    // fire an event that the spatial name changed.
    // the scene tree needs to know when this happened so it can change the name visually.
    PropertyChangeListener propertyChangeListener = evt -> {
      ServiceManager.getService(EventService.class)
          .fireEvent(new SpatialNameChangedEvent(object));
    };
    StringComponent name = new StringComponent(object.getName());
    name.addPropertyChangeListener(propertyChangeListener);
    name.setPropertyName("name");
    bind(FieldUtils.getField(Spatial.class, NAME), name);

    EnumComponent cullHint = new EnumComponent(object.getCullHint());
    cullHint.setPropertyName("cullHint");
    bind(FieldUtils.getField(Spatial.class, CULL_HINT), cullHint);

    EnumComponent shadowMode = new EnumComponent(object.getShadowMode());
    shadowMode.setPropertyName("shadowMode");
    bind(FieldUtils.getField(Spatial.class, SHADOW_MODE), shadowMode);

    EnumComponent queueBucket = new EnumComponent(object.getQueueBucket());
    queueBucket.setPropertyName("queueBucket");
    bind(FieldUtils.getField(Spatial.class, QUEUE_BUCKET), queueBucket);

    EnumComponent batchHint = new EnumComponent(object.getBatchHint());
    batchHint.setPropertyName("batchHint");
    bind(FieldUtils.getField(Spatial.class, BATCH_HINT), batchHint);

    PropertySection spatialSection = new PropertySection("Spatial", name, cullHint, shadowMode,
        queueBucket, batchHint);
    propertySections.add(spatialSection);

    if (object instanceof Node) {
      // Node doesn't have any properties we want.
      // Leave the comment here so we're aware that we know.
    } else if (object instanceof Geometry) {
      // Geometry-specific data

      BooleanComponent ignoreTransform = new BooleanComponent(
          ((Geometry) object).isIgnoreTransform());
      ignoreTransform.setPropertyName("ignoreTransform");
      bind(FieldUtils.getField(Spatial.class, IGNORE_TRANSFORM), ignoreTransform);

      // @TODO: only show lod level if LOD levels are set.
      //getter = object.getClass().getMethod("getLodLevel");
      //setter = object.getClass().getMethod("setLodLevel", int.class);

      //IntegerComponent lodLevel = new IntegerComponent(object, getter, setter);
      //lodLevel.setPropertyName("lodLevel");

      // Material chooser.
      MaterialChooserComponent materialChooser = new MaterialChooserComponent(
          ((Geometry) object).getMaterial());
      PropertyChangeListener materialChangeListener = evt -> {
        Material material = (Material) evt.getNewValue();
        List<PropertySection> sections = buildMaterialSection(material);
        if (!sections.isEmpty()) {
          ServiceManager.getService(PropertyInspectorService.class)
              .updateSections(sections);
        }
      };

      materialChooser.addPropertyChangeListener(materialChangeListener);
      bind(FieldUtils.getField(Geometry.class, MATERIAL), materialChooser);

      PropertySection geometrySection = new PropertySection("Geometry", ignoreTransform,
          materialChooser /*, lodLevel */);
      propertySections.add(geometrySection);

      // Material
      Material material = ((Geometry) object).getMaterial();
      List<PropertySection> sections = buildMaterialSection(material);
      if (!sections.isEmpty()) {
        propertySections.addAll(sections);
      }

    }

    return propertySections;
  }

  public List<PropertySection> buildMaterialSection(Material material) {
    List<PropertySection> sections = new ArrayList<>();
    if (material != null) {
      MaterialComponentSetBuilder materialComponentSetBuilder = new MaterialComponentSetBuilder(
          material);
      sections = materialComponentSetBuilder.build();
    }
    return sections;
  }
}
