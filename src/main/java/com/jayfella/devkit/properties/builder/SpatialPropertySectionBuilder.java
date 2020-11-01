package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.bool.BooleanComponent;
import com.jayfella.devkit.properties.component.enumeration.EnumComponent;
import com.jayfella.devkit.properties.component.events.SpatialNameChangedEvent;
import com.jayfella.devkit.properties.component.integer.IntegerComponent;
import com.jayfella.devkit.properties.component.material.MaterialChooserComponent;
import com.jayfella.devkit.properties.component.quaternion.QuaternionComponent;
import com.jayfella.devkit.properties.component.string.StringComponent;
import com.jayfella.devkit.properties.component.vector3f.Vector3fComponent;
import com.jayfella.devkit.service.EventService;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.PropertyInspectorService;
import com.jayfella.devkit.service.ServiceManager;
import com.jme3.material.Material;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.Spatial.CullHint;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class SpatialPropertySectionBuilder extends AbstractPropertySectionBuilder<Spatial> {

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
  public static final String LOD_LEVEL = "lodLevel";


  public SpatialPropertySectionBuilder(Spatial object, Field... ignoredProperties) {
    super(object, ignoredProperties);
  }

  @Override
  public List<PropertySection> build() {

    List<PropertySection> propertySections = new ArrayList<>();

    // Transform : location, rotation, scale

    Vector3fComponent localTranslation = new Vector3fComponent(object.getLocalTranslation());
    localTranslation.setPropertyName(LOCAL_TRANSLATION);
    localTranslation.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class)
            .enqueue(() -> object.setLocalTranslation((Vector3f) value.getNewValue())));

    QuaternionComponent localRotation = new QuaternionComponent(object.getLocalRotation());
    localRotation.setPropertyName(LOCAL_ROTATION);
    localRotation.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class)
            .enqueue(() -> object.setLocalRotation((Quaternion) value.getNewValue())));

    Vector3fComponent localScale = new Vector3fComponent(object.getLocalScale());
    localScale.setPropertyName(LOCAL_SCALE);
    localScale.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setLocalScale((Vector3f) value.getNewValue())));

    PropertySection transformSection = new PropertySection("Transform", localTranslation,
        localRotation, localScale);
    propertySections.add(transformSection);

    // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint
    // fire an event that the spatial name changed.
    // the scene tree needs to know when this happened so it can change the name visually.
    StringComponent name = new StringComponent(object.getName());
    name.setPropertyName(NAME);
    name.addPropertyChangeListener(evt -> ServiceManager.getService(EventService.class)
        .fireEvent(new SpatialNameChangedEvent(object)));
    name.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setName((String) value.getNewValue())));

    EnumComponent cullHint = new EnumComponent(object.getCullHint());
    cullHint.setPropertyName(CULL_HINT);
    cullHint.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> object.setCullHint(
            (CullHint) value.getNewValue())));

    EnumComponent shadowMode = new EnumComponent(object.getShadowMode());
    shadowMode.setPropertyName(SHADOW_MODE);
    shadowMode.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setShadowMode(
            (ShadowMode) value.getNewValue())));

    EnumComponent queueBucket = new EnumComponent(object.getQueueBucket());
    queueBucket.setPropertyName(QUEUE_BUCKET);
    queueBucket.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setQueueBucket(
            (Bucket) value.getNewValue())));

    EnumComponent batchHint = new EnumComponent(object.getBatchHint());
    batchHint.setPropertyName(BATCH_HINT);
    batchHint.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setBatchHint(
            (BatchHint) value.getNewValue())));

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
      ignoreTransform.setPropertyName(IGNORE_TRANSFORM);
      ignoreTransform.addPropertyChangeListener(
          value -> ServiceManager.getService(JmeEngineService.class)
              .enqueue(() -> ((Geometry) object).setIgnoreTransform(
                  (Boolean) value.getNewValue())));

      boolean isLODSet = ((Geometry) object).getMesh().getNumLodLevels() == 0;
      IntegerComponent lodLevel = new IntegerComponent(((Geometry) object).getLodLevel());
      lodLevel.setPropertyName(LOD_LEVEL + " THE LOD LEVEL ARE NOT SET");
      if (isLODSet) {
        lodLevel.setPropertyName(LOD_LEVEL + " THE LOD LEVEL ARE SET");
        lodLevel.addPropertyChangeListener(
            value -> ServiceManager.getService(JmeEngineService.class).enqueue(() ->
                object.setLodLevel(
                    (Integer) value.getNewValue())));
      }

      // Material chooser.
      MaterialChooserComponent materialChooser = new MaterialChooserComponent(
          ((Geometry) object).getMaterial());

      // This listener trigger the update of material detail on material selection
      PropertyChangeListener materialChangeListener = evt -> {
        Material material = (Material) evt.getNewValue();
        List<PropertySection> sections = buildMaterialSection(material);
        if (!sections.isEmpty()) {
          ServiceManager.getService(PropertyInspectorService.class)
              .updateSections(sections);
        }
      };

      materialChooser.addPropertyChangeListener(materialChangeListener);
      //this listener juste propagate the material change to the jmeEngine
      materialChooser.addPropertyChangeListener(
          value -> ServiceManager.getService(JmeEngineService.class)
              .enqueue(() -> object.setMaterial(
                  (Material) value.getNewValue())));

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
      MaterialPropertySectionBuilder materialComponentSetBuilder = new MaterialPropertySectionBuilder(
          material);
      sections = materialComponentSetBuilder.build();
    }
    return sections;
  }
}
