package fr.exratio.jme.devkit.properties.builder;

import com.google.common.eventbus.EventBus;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.renderer.queue.RenderQueue.ShadowMode;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.BatchHint;
import com.jme3.scene.Spatial.CullHint;
import fr.exratio.jme.devkit.event.SpatialNameChangedEvent;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.enumeration.EnumEditor;
import fr.exratio.jme.devkit.properties.component.string.StringEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.ServiceManager;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
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

  private final EventBus eventBus;
  private final EditorJmeApplication editorJmeApplication;

  @Autowired
  public SpatialPropertySectionBuilder(EventBus eventBus, EditorJmeApplication editorJmeApplication) {
    this.eventBus = eventBus;
    this.editorJmeApplication = editorJmeApplication;
  }

  @Override
  public List<PropertySection> build() {

    List<PropertySection> propertySections = new ArrayList<>();

    TransformSection transformSection = new TransformSection(object);
    transformSection.getLocalRotation().addPropertyChangeListener(
        value -> editorJmeApplication.enqueue(() -> object.setLocalTranslation((Vector3f) value.getNewValue())));
    transformSection.getLocalScale().addPropertyChangeListener(
        value -> editorJmeApplication.enqueue(() -> object.setLocalScale((Vector3f) value.getNewValue())));
    transformSection.getLocalTranslation().addPropertyChangeListener(
        value -> editorJmeApplication.enqueue(() -> object.setLocalTranslation((Vector3f) value.getNewValue())));
    eventBus.register(transformSection);

    // Transform : location, rotation, scale

    propertySections.add(transformSection);

    PropertySection spatialSection = new PropertySection("Spatial");
    // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint
    // fire an event that the spatial name changed.
    // the scene tree needs to know when this happened so it can change the name visually.
    StringEditor name = new StringEditor(object.getName());
    name.addPropertyChangeListener(evt -> eventBus.post(new SpatialNameChangedEvent(object)));
    name.addPropertyChangeListener(
        value -> editorJmeApplication.enqueue(() -> object.setName((String) value.getNewValue())));
    spatialSection.addProperty(NAME, name.getCustomEditor());

    EnumEditor cullHint = new EnumEditor(object.getCullHint());
    cullHint.addPropertyChangeListener(
        value -> editorJmeApplication
            .enqueue(() -> object.setCullHint((CullHint) value.getNewValue())));
    spatialSection.addProperty(CULL_HINT, cullHint.getCustomEditor());

    EnumEditor shadowMode = new EnumEditor(object.getShadowMode());
    shadowMode.addPropertyChangeListener(value -> editorJmeApplication
        .enqueue(() -> object.setShadowMode((ShadowMode) value.getNewValue())));
    spatialSection.addProperty(SHADOW_MODE, shadowMode.getCustomEditor());

    EnumEditor queueBucket = new EnumEditor(object.getQueueBucket());
    queueBucket.addPropertyChangeListener(value -> editorJmeApplication
        .enqueue(() -> object.setQueueBucket((Bucket) value.getNewValue())));
    spatialSection.addProperty(QUEUE_BUCKET, queueBucket.getCustomEditor());

    EnumEditor batchHint = new EnumEditor(object.getBatchHint());
    batchHint.addPropertyChangeListener(value -> editorJmeApplication
        .enqueue(() -> object.setBatchHint((BatchHint) value.getNewValue())));
    spatialSection.addProperty(BATCH_HINT, batchHint.getCustomEditor());

    propertySections.add(spatialSection);
    return propertySections;
  }
}
