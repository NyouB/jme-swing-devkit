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
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.reflect.FieldUtils;

public class SpatialComponentSetBuilder extends AbstractComponentSetBuilder<Spatial> {

  public static final String LOCAL_ROTATION = "localRotation";
  public static final String LOCAL_TRANSLATION = "localTranslation";
  public static final String LOCAL_SCALE = "localScale";

  public SpatialComponentSetBuilder(Spatial object, String... ignoredProperties) {
    super(object, ignoredProperties);
  }

  @Override
  public List<PropertySection> build() {

    List<PropertySection> propertySections = new ArrayList<>();

    Method getter, setter;

    try {

      // Transform : location, rotation, scale

      Vector3fComponent localTranslation = new Vector3fComponent(object.getLocalTranslation());
      localTranslation.setPropertyName(LOCAL_TRANSLATION);

      QuaternionComponent localRotation = new QuaternionComponent(object.getLocalRotation());
      localRotation.setPropertyName(LOCAL_ROTATION);
      bind(FieldUtils.getField(Spatial.class, LOCAL_ROTATION), localRotation);

      Vector3fComponent localScale = new Vector3fComponent(object.getLocalScale());
      localScale.setPropertyName(LOCAL_SCALE);

      PropertySection transformSection = new PropertySection("Transform", localTranslation,
          localRotation, localScale);
      propertySections.add(transformSection);

      // Spatial: name, cullHint, lastFrustumIntersection, shadowMode, QueueBucket, BatchHint

      getter = object.getClass().getMethod("getName");
      setter = object.getClass().getMethod("setName", String.class);

      // fire an event that the spatial name changed.
      // the scene tree needs to know when this happened so it can change the name visually.
      StringComponent name = new StringComponent(object, getter, setter) {
        @Override
        public void propertyChanged(String value) {
          ServiceManager.getService(EventService.class)
              .fireEvent(new SpatialNameChangedEvent(object));
        }
      };
      name.setPropertyName("name");

      getter = object.getClass().getMethod("getCullHint");
      setter = object.getClass().getMethod("setCullHint", com.jme3.scene.Spatial.CullHint.class);

      EnumComponent cullHint = new EnumComponent(object, getter, setter);
      cullHint.setPropertyName("cullHint");

      getter = object.getClass().getMethod("getShadowMode");
      setter = object.getClass().getMethod("setShadowMode", RenderQueue.ShadowMode.class);

      EnumComponent shadowMode = new EnumComponent(object, getter, setter);
      shadowMode.setPropertyName("shadowMode");

      getter = object.getClass().getMethod("getQueueBucket");
      setter = object.getClass().getMethod("setQueueBucket", RenderQueue.Bucket.class);

      EnumComponent queueBucket = new EnumComponent(object, getter, setter);
      queueBucket.setPropertyName("queueBucket");

      getter = object.getClass().getMethod("getBatchHint");
      setter = object.getClass().getMethod("setBatchHint", com.jme3.scene.Spatial.BatchHint.class);

      EnumComponent batchHint = new EnumComponent(object, getter, setter);
      batchHint.setPropertyName("batchHint");

      PropertySection spatialSection = new PropertySection("Spatial", name, cullHint, shadowMode,
          queueBucket, batchHint);
      propertySections.add(spatialSection);


    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }

    if (object instanceof Node) {
      // Node doesn't have any properties we want.
      // Leave the comment here so we're aware that we know.
    } else if (object instanceof Geometry) {

      try {

        // Geometry-specific data
        getter = object.getClass().getMethod("isIgnoreTransform");
        setter = object.getClass().getMethod("setIgnoreTransform", boolean.class);

        BooleanComponent ignoreTranform = new BooleanComponent(object, getter, setter);
        ignoreTranform.setPropertyName("ignoreTransform");

        // @TODO: only show lod level if LOD levels are set.
        //getter = object.getClass().getMethod("getLodLevel");
        //setter = object.getClass().getMethod("setLodLevel", int.class);

        //IntegerComponent lodLevel = new IntegerComponent(object, getter, setter);
        //lodLevel.setPropertyName("lodLevel");

        // Material chooser.
        getter = object.getClass().getMethod("getMaterial");
        setter = object.getClass().getMethod("setMaterial", Material.class);
        MaterialChooserComponent materialChooser = new MaterialChooserComponent(object, getter,
            setter) {

          @Override
          public void selectionChanged(Material material) {

            if (material != null) {
              MaterialComponentSetBuilder materialComponentSetBuilder = new MaterialComponentSetBuilder(
                  material);
              List<PropertySection> sections = materialComponentSetBuilder.build();
              // propertySections.addAll(sections);

              ServiceManager.getService(PropertyInspectorService.class)
                  .updateSections(sections);
            }

          }

        };

        PropertySection geometrySection = new PropertySection("Geometry", ignoreTranform,
            materialChooser /*, lodLevel */);
        propertySections.add(geometrySection);

        // Material
        getter = object.getClass().getMethod("getMaterial");
        Material material = null;

        try {
          material = (Material) getter.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException e) {
          e.printStackTrace();
        }

        if (material != null) {
          MaterialComponentSetBuilder materialComponentSetBuilder = new MaterialComponentSetBuilder(
              material);
          List<PropertySection> sections = materialComponentSetBuilder.build();
          propertySections.addAll(sections);
        }

      } catch (NoSuchMethodException e) {
        e.printStackTrace();
      }

    }

    return propertySections;
  }


  @Override
  public void propertyChange(PropertyChangeEvent evt) {

  }
}
