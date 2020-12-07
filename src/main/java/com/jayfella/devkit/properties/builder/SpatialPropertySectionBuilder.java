package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.enumeration.EnumEditor;
import com.jayfella.devkit.properties.component.events.SpatialNameChangedEvent;
import com.jayfella.devkit.properties.component.quaternion.QuaternionEditor;
import com.jayfella.devkit.properties.component.string.StringEditor;
import com.jayfella.devkit.properties.component.vector3f.Vector3fEditor;
import com.jayfella.devkit.service.EventService;
import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.Spatial.CullHint;
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
  public static final String GEOMETRY = "Geometry";


  public SpatialPropertySectionBuilder(Spatial object) {
    super(object);
  }

  @Override
  public List<PropertySection> build() {

    List<PropertySection> propertySections = new ArrayList<>();

    PropertySection transformSection = new PropertySection("Transform");
    // Transform : location, rotation, scale

    Vector3fEditor localTranslation = new Vector3fEditor(object.getLocalTranslation());
    localTranslation.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class)
            .enqueue(() -> object.setLocalTranslation((Vector3f) value.getNewValue())));
    transformSection.addProperty(LOCAL_TRANSLATION, localTranslation.getCustomEditor());

    QuaternionEditor localRotation = new QuaternionEditor(object.getLocalRotation());
    localRotation.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class)
            .enqueue(() -> object.setLocalRotation((Quaternion) value.getNewValue())));
    transformSection.addProperty(LOCAL_ROTATION, localRotation.getCustomEditor());

    Vector3fEditor localScale = new Vector3fEditor(object.getLocalScale());
    localScale.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setLocalScale((Vector3f) value.getNewValue())));
    transformSection.addProperty(LOCAL_SCALE, localScale.getCustomEditor());

    propertySections.add(transformSection);

    PropertySection spatialSection = new PropertySection("Spatial");
    // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint
    // fire an event that the spatial name changed.
    // the scene tree needs to know when this happened so it can change the name visually.
    StringEditor name = new StringEditor(object.getName());
    name.addPropertyChangeListener(evt -> ServiceManager.getService(EventService.class)
        .fireEvent(new SpatialNameChangedEvent(object)));
    name.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setName((String) value.getNewValue())));
    spatialSection.addProperty(NAME, name.getCustomEditor());

    EnumEditor cullHint = new EnumEditor(object.getCullHint());
    cullHint.addPropertyChangeListener(
        value -> ServiceManager.getService(JmeEngineService.class).enqueue(() -> object.setCullHint(
            (CullHint) value.getNewValue())));
    spatialSection.addProperty(CULL_HINT, cullHint.getCustomEditor());

    EnumEditor shadowMode = new EnumEditor(object.getShadowMode());
    shadowMode.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setShadowMode(
            (ShadowMode) value.getNewValue())));
    spatialSection.addProperty(SHADOW_MODE, shadowMode.getCustomEditor());

    EnumEditor queueBucket = new EnumEditor(object.getQueueBucket());
    queueBucket.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setQueueBucket(
            (Bucket) value.getNewValue())));
    spatialSection.addProperty(QUEUE_BUCKET, queueBucket.getCustomEditor());

    EnumEditor batchHint = new EnumEditor(object.getBatchHint());
    batchHint.addPropertyChangeListener(value -> ServiceManager.getService(JmeEngineService.class)
        .enqueue(() -> object.setBatchHint(
            (BatchHint) value.getNewValue())));
    spatialSection.addProperty(BATCH_HINT, batchHint.getCustomEditor());

    propertySections.add(spatialSection);
    return propertySections;
  }
}
