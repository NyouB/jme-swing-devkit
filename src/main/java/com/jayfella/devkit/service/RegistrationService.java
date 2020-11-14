package com.jayfella.devkit.service;

import com.google.common.collect.ImmutableMap;
import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.properties.builder.MaterialComponentSetFactory;
import com.jayfella.devkit.properties.builder.PropertySectionBuilderFactory;
import com.jayfella.devkit.properties.builder.SpatialComponentSetFactory;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jayfella.devkit.properties.component.bool.BooleanComponentFactory;
import com.jayfella.devkit.properties.component.colorgba.ColorRGBAComponentFactory;
import com.jayfella.devkit.properties.component.control.AnimComposerComponentFactory;
import com.jayfella.devkit.properties.component.control.AnimControlComponentFactory;
import com.jayfella.devkit.properties.component.enumeration.EnumComponentFactory;
import com.jayfella.devkit.properties.component.floatc.FloatComponentFactory;
import com.jayfella.devkit.properties.component.integer.IntegerComponentFactory;
import com.jayfella.devkit.properties.component.quaternion.QuaternionComponentFactory;
import com.jayfella.devkit.properties.component.string.StringComponentFactory;
import com.jayfella.devkit.properties.component.texture2d.Texture2DComponentFactory;
import com.jayfella.devkit.properties.component.vector2f.Vector2fComponentFactory;
import com.jayfella.devkit.properties.component.vector3f.Vector3fComponentFactory;
import com.jayfella.devkit.properties.component.vector4f.Vector4fComponentFactory;
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
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.texture.Texture2D;
import java.util.HashMap;
import java.util.Map;

/**
 * A service that binds types to components. Used for creating components from reflected values to
 * allow modifying the value.
 */
public class RegistrationService implements Service {

  // a single object that returns a single component. For example a vector3f or a float.
  private final Map<Class<?>, SDKComponentFactory> componentClasses = new HashMap<>();

  // a single object that returns multiple components. For example a spatial or a material.
  private final Map<Class<?>, PropertySectionBuilderFactory> componentBuilderClasses = new HashMap<>();

  private final Registrar<NodeRegistrar> nodeRegistration = new Registrar<>(NodeRegistrar.class);
  private final Registrar<GeometryRegistrar> geometryRegistration = new Registrar<>(
      GeometryRegistrar.class);

  private final Registrar<ControlRegistrar> controlRegistration = new Registrar<>(
      ControlRegistrar.class);

  private final long threadId;


  public RegistrationService() {

    threadId = Thread.currentThread().getId();

    // register all the built-in components.
    registerComponentFactory(Boolean.class, new BooleanComponentFactory());
    registerComponentFactory(ColorRGBA.class, new ColorRGBAComponentFactory());
    registerComponentFactory(Enum.class, new EnumComponentFactory());
    registerComponentFactory(Float.class, new FloatComponentFactory());
    registerComponentFactory(Integer.class, new IntegerComponentFactory());
    registerComponentFactory(Quaternion.class, new QuaternionComponentFactory());
    registerComponentFactory(String.class, new StringComponentFactory());
    registerComponentFactory(Texture2D.class,
        new Texture2DComponentFactory(ServiceManager.getService(JmeEngineService.class)
            .getAssetManager(), DevKitConfig.getInstance().getProjectConfig().getAssetRootDir()));
    registerComponentFactory(Vector2f.class, new Vector2fComponentFactory());
    registerComponentFactory(Vector3f.class, new Vector3fComponentFactory());
    registerComponentFactory(Vector4f.class, new Vector4fComponentFactory());
    registerComponentFactory(AnimControl.class, new AnimControlComponentFactory());
    registerComponentFactory(AnimComposer.class, new AnimComposerComponentFactory());

    registerPropertySectionBuilderFactory(Spatial.class, new SpatialComponentSetFactory());
    registerPropertySectionBuilderFactory(Material.class, new MaterialComponentSetFactory());

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
  public <T> void registerComponentFactory(Class<T> clazz, SDKComponentFactory<T> componentClass) {
    componentClasses.put(clazz, componentClass);
  }

  public Map<Class<?>, SDKComponentFactory> getComponentFactoryMap() {
    return ImmutableMap.copyOf(componentClasses);
  }

  public SDKComponentFactory getComponentFactoryFor(Class<?> clazz) {
    return componentClasses.get(clazz);
  }

  /**
   * Registers a class with a componentBuilder. For example, the built-in componentBuilders are: -
   * Material.class, MaterialComponetSetBuilder.class - Spatial.class,
   * SpatialComponentSetBuilder.class
   *
   * All classes that are not registered are processed using the ReflectedComponentSetBuilder.
   *
   * @param clazz the class to map
   * @param componentBuilderClass the componentBuilder to create for the mapped class.
   */
  public void registerPropertySectionBuilderFactory(Class<?> clazz,
      PropertySectionBuilderFactory componentBuilderClass) {
    componentBuilderClasses.put(clazz, componentBuilderClass);
  }

  public Map<Class<?>, PropertySectionBuilderFactory> getPropertySectionBuilderFactoryMap() {
    return ImmutableMap.copyOf(componentBuilderClasses);
  }

  public <T> PropertySectionBuilderFactory<T> getPropertySectionBuilderFactoryFor(Class<T> clazz) {
    return componentBuilderClasses.get(clazz);
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
