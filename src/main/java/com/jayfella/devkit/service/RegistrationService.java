package com.jayfella.devkit.service;

import com.jayfella.devkit.properties.builder.AbstractPropertySectionBuilder;
import com.jayfella.devkit.properties.builder.GeometryPropertySectionBuilder;
import com.jayfella.devkit.properties.builder.MaterialPropertySectionBuilder;
import com.jayfella.devkit.properties.builder.SpatialPropertySectionBuilder;
import com.jayfella.devkit.properties.component.bool.BooleanEditor;
import com.jayfella.devkit.properties.component.colorgba.ColorRGBAEditor;
import com.jayfella.devkit.properties.component.control.AnimComposerEditor;
import com.jayfella.devkit.properties.component.control.AnimControlEditor;
import com.jayfella.devkit.properties.component.enumeration.EnumEditor;
import com.jayfella.devkit.properties.component.floatc.FloatEditor;
import com.jayfella.devkit.properties.component.integer.IntegerEditor;
import com.jayfella.devkit.properties.component.quaternion.QuaternionEditor;
import com.jayfella.devkit.properties.component.string.StringEditor;
import com.jayfella.devkit.properties.component.texture2d.Texture2DEditor;
import com.jayfella.devkit.properties.component.vector2f.Vector2fEditor;
import com.jayfella.devkit.properties.component.vector3f.Vector3fEditor;
import com.jayfella.devkit.properties.component.vector4f.Vector4fEditor;
import com.jayfella.devkit.registration.Registrar;
import com.jayfella.devkit.registration.control.ControlRegistrar;
import com.jayfella.devkit.registration.control.NoArgsControlRegistrar;
import com.jayfella.devkit.registration.spatial.AssetLinkNodeRegistrar;
import com.jayfella.devkit.registration.spatial.BatchNodeRegistrar;
import com.jayfella.devkit.registration.spatial.GeometryRegistrar;
import com.jayfella.devkit.registration.spatial.InstancedNodeSpatialRegistrar;
import com.jayfella.devkit.registration.spatial.NoArgsSpatialRegistrar;
import com.jayfella.devkit.registration.spatial.NodeRegistrar;
import com.jme3.anim.AnimComposer;
import com.jme3.animation.AnimControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.texture.Texture2D;
import java.beans.PropertyEditorManager;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A service that binds types to components. Used for creating components from reflected values to
 * allow modifying the value.
 */
public class RegistrationService implements Service {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(RegistrationService.class);

  private final Map<Class<?>, Class<? extends AbstractPropertySectionBuilder>> propertySectionBuilders = new HashMap<>();

  private final Registrar<NodeRegistrar> nodeRegistration = new Registrar<>(NodeRegistrar.class);
  private final Registrar<GeometryRegistrar> geometryRegistration = new Registrar<>(
      GeometryRegistrar.class);

  private final Registrar<ControlRegistrar> controlRegistration = new Registrar<>(
      ControlRegistrar.class);

  private final long threadId;


  public RegistrationService() {

    threadId = Thread.currentThread().getId();
    PropertyEditorManager.registerEditor(Boolean.class, BooleanEditor.class);
    PropertyEditorManager.registerEditor(boolean.class, BooleanEditor.class);
    PropertyEditorManager.registerEditor(ColorRGBA.class, ColorRGBAEditor.class);
    PropertyEditorManager.registerEditor(Enum.class, EnumEditor.class);
    PropertyEditorManager.registerEditor(Float.class, FloatEditor.class);
    PropertyEditorManager.registerEditor(float.class, FloatEditor.class);
    PropertyEditorManager.registerEditor(Integer.class, IntegerEditor.class);
    PropertyEditorManager.registerEditor(int.class, IntegerEditor.class);
    PropertyEditorManager.registerEditor(Quaternion.class, QuaternionEditor.class);
    PropertyEditorManager.registerEditor(String.class, StringEditor.class);
    PropertyEditorManager.registerEditor(Texture2D.class, Texture2DEditor.class);
    PropertyEditorManager.registerEditor(Vector2f.class, Vector2fEditor.class);
    PropertyEditorManager.registerEditor(Vector3f.class, Vector3fEditor.class);
    PropertyEditorManager.registerEditor(Vector4f.class, Vector4fEditor.class);
    PropertyEditorManager.registerEditor(AnimControl.class, AnimControlEditor.class);
    PropertyEditorManager.registerEditor(AnimComposer.class, AnimComposerEditor.class);

    registerPropertySectionBuilder(Spatial.class, SpatialPropertySectionBuilder.class);
    registerPropertySectionBuilder(Geometry.class, GeometryPropertySectionBuilder.class);
    registerPropertySectionBuilder(Material.class, MaterialPropertySectionBuilder.class);

    nodeRegistration.register(new NoArgsSpatialRegistrar(Node.class));
    nodeRegistration.register(new AssetLinkNodeRegistrar());
    nodeRegistration.register(new BatchNodeRegistrar());
    nodeRegistration.register(new InstancedNodeSpatialRegistrar());

    // geometryRegistration.register(new ParticleEmitterSpatialRegistrar());

    controlRegistration.register(NoArgsControlRegistrar.create(BillboardControl.class));

  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException("THis method shouldn't be called");
  }

  /**
   * Registers a class with a component. For example, the built-in components are: - boolean.class,
   * BooleanComponent.class - ColorRGBA.class, ColorRGBAComponent.class - Enum.class,
   * EnumComponent.class and so on.
   *
   * @param clazz the class to map.
   * @param componentClass the component to create for the mapped class.
   */
  public <T> void registerPropertySectionBuilder(Class<T> clazz,
      Class<? extends AbstractPropertySectionBuilder> componentClass) {
    propertySectionBuilders.put(clazz, componentClass);
  }

  public Class<? extends AbstractPropertySectionBuilder> getPropertySectionBuilder(Class clazz) {
    return propertySectionBuilders.get(clazz);
  }

  public AbstractPropertySectionBuilder<?> getPropertySectionBuilderInstance(Class clazz,
      Object object) {
    Class<? extends AbstractPropertySectionBuilder> builderClass = propertySectionBuilders
        .get(clazz);
    if (builderClass == null) {
      return null;
    }
    AbstractPropertySectionBuilder<?> builder = null;
    try {
      builder = builderClass.getConstructor().newInstance(object);
    } catch (Exception e) {
      LOGGER.warn("-- find() Error while instanciating builder {}",
          builderClass.getSimpleName(), e);
    }
    return builder;
  }

  public Set<Class<?>> getRegisteredClasses() {
    return propertySectionBuilders.keySet();
  }

  public void registerNode(NodeRegistrar registrar) {
    nodeRegistration.register(registrar);
  }

  public Registrar<NodeRegistrar> getNodeRegistration() {
    return nodeRegistration;
  }

  public void registerGeometry(GeometryRegistrar registrar) {
    geometryRegistration.register(registrar);
  }

  public Registrar<GeometryRegistrar> getGeometryRegistration() {
    return geometryRegistration;
  }

  public void registerControl(ControlRegistrar registrar) {
    controlRegistration.register(registrar);
  }

  public Registrar<ControlRegistrar> getControlRegistration() {
    return controlRegistration;
  }

}
