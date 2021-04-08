package fr.exratio.jme.devkit.properties.builder;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.quaternion.QuaternionEditor;
import fr.exratio.jme.devkit.properties.component.vector3f.Vector3fEditor;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.inspector.SpatialMoveEvent;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class TransformSection extends PropertySection {

  public static final String LOCAL_ROTATION = "localRotation";
  public static final String LOCAL_TRANSLATION = "localTranslation";
  public static final String LOCAL_SCALE = "localScale";

  public static final String TITLE = "Transform";

  private static final Logger LOGGER = LoggerFactory.getLogger(TransformSection.class);
  private final Spatial spatial;
  private Vector3fEditor localTranslation;
  private QuaternionEditor localRotation;
  private Vector3fEditor localScale;

  public TransformSection(Spatial spatial) {
    super(TITLE);
    this.spatial = spatial;
    init();
  }

  public void init() {
    localTranslation = new Vector3fEditor(spatial.getLocalTranslation());
    addProperty(LOCAL_TRANSLATION, localTranslation.getCustomEditor());

    localRotation = new QuaternionEditor(spatial.getLocalRotation());
    addProperty(LOCAL_ROTATION, localRotation.getCustomEditor());

    localScale = new Vector3fEditor(spatial.getLocalScale());
    addProperty(LOCAL_SCALE, localScale.getCustomEditor());
  }

  @Subscribe
  public void onSpatialMove(SpatialMoveEvent event) {
    Spatial selectedItem = event.getSpatialItem();
    if (selectedItem == null) {
      LOGGER.warn(
          "-- onSpatialMove() the object contained in the event is null. Doing nothing");
      return;
    }
    localTranslation.setTypedValue(spatial.getLocalTranslation());
    localRotation.setTypedValue(spatial.getLocalRotation());
    localScale.setTypedValue(spatial.getLocalScale());
  }
}
