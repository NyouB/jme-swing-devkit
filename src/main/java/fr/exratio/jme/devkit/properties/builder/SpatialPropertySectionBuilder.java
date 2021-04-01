package fr.exratio.jme.devkit.properties.builder;

import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.Spatial.CullHint;
import fr.exratio.jme.devkit.event.SpatialNameChangedEvent;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.enumeration.EnumEditor;
import fr.exratio.jme.devkit.properties.component.string.StringEditor;
import fr.exratio.jme.devkit.service.EventService;
import fr.exratio.jme.devkit.service.JmeEngineService;
import fr.exratio.jme.devkit.service.ServiceManager;
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

    PropertySection transformSection = new TransformSection(object);
    // Transform : location, rotation, scale

    propertySections.add(transformSection);

    PropertySection spatialSection = new PropertySection("Spatial");
    // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint
    // fire an event that the spatial name changed.
    // the scene tree needs to know when this happened so it can change the name visually.
    StringEditor name = new StringEditor(object.getName());
    name.addPropertyChangeListener(evt -> ServiceManager.getService(EventService.class)
        .post(new SpatialNameChangedEvent(object)));
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
