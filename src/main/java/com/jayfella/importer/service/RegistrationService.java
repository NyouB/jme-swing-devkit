package com.jayfella.importer.service;

import com.google.common.collect.ImmutableMap;
import com.jayfella.importer.properties.ControlSdkComponent;
import com.jayfella.importer.properties.builder.AbstractComponentSetBuilder;
import com.jayfella.importer.properties.builder.MaterialComponentSetBuilder;
import com.jayfella.importer.properties.builder.SpatialComponentSetBuilder;
import com.jayfella.importer.properties.component.*;
import com.jayfella.importer.properties.component.control.AnimComposerComponent;
import com.jayfella.importer.properties.component.control.AnimControlComponent;
import com.jayfella.importer.registration.Registrar;
import com.jayfella.importer.registration.control.ControlRegistrar;
import com.jayfella.importer.registration.control.NoArgsControlRegistrar;
import com.jayfella.importer.registration.spatial.*;
import com.jme3.anim.AnimComposer;
import com.jme3.animation.AnimControl;
import com.jme3.material.Material;
import com.jme3.math.*;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.control.Control;
import com.jme3.texture.Texture2D;

import java.util.HashMap;
import java.util.Map;

/**
 * A service that binds types to components. Used for creating components from reflected values to allow modifying the value.
 */
public class RegistrationService implements Service {

    // a single object that returns a single component. For example a vector3f or a float.
    private final Map<Class<?>, Class<? extends ReflectedSdkComponent<?>>> componentClasses = new HashMap<>();

    // a single object that returns multiple components. For example a spatial or a material.
    private final Map<Class<?>, Class<? extends AbstractComponentSetBuilder<?>>> componentBuilderClasses = new HashMap<>();

    // a control that returns a single component.
    private final Map<Class<? extends Control>, Class<? extends ControlSdkComponent<?>>> controlComponentClasses = new HashMap<>();

    private final Registrar<NodeRegistrar> nodeRegistration = new Registrar<>(NodeRegistrar.class);
    private final Registrar<GeometryRegistrar> geometryRegistration = new Registrar<>(GeometryRegistrar.class);

    private final Registrar<ControlRegistrar> controlRegistration = new Registrar<>(ControlRegistrar.class);

    private final long threadId;

    public RegistrationService() {

        threadId = Thread.currentThread().getId();

        // register all the built-in components.

        componentClasses.put(boolean.class, BooleanComponent.class);
        componentClasses.put(ColorRGBA.class, ColorRGBAComponent.class);
        componentClasses.put(Enum.class, EnumComponent.class);
        componentClasses.put(float.class, FloatComponent.class);
        componentClasses.put(int.class, IntegerComponent.class);
        componentClasses.put(Quaternion.class, QuaternionComponent.class);
        componentClasses.put(String.class, StringComponent.class);
        componentClasses.put(Texture2D.class, Texture2DComponent.class);
        componentClasses.put(Vector2f.class, Vector2fComponent.class);
        componentClasses.put(Vector3f.class, Vector3fComponent.class);
        componentClasses.put(Vector4f.class, Vector4fComponent.class);

        componentBuilderClasses.put(Spatial.class, SpatialComponentSetBuilder.class);
        componentBuilderClasses.put(Material.class, MaterialComponentSetBuilder.class);

        controlComponentClasses.put(AnimControl.class, AnimControlComponent.class);
        controlComponentClasses.put(AnimComposer.class, AnimComposerComponent.class);

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

    }

    /**
     * Registers a class with a component. For example, the built-in components are:
     * - boolean.class, BooleanComponent.class
     * - ColorRGBA.class, ColorRGBAComponent.class
     * - Enum.class, EnumComponent.class
     * and so on.
     *
     * @param clazz          the class to map.
     * @param componentClass the component to create for the mapped class.
     */
    public void registerComponent(Class<?> clazz, Class<? extends ReflectedSdkComponent<?>> componentClass) {
        componentClasses.put(clazz, componentClass);
    }

    public Map<Class<?>, Class<? extends ReflectedSdkComponent<?>>> getComponentClasses() {
        return ImmutableMap.copyOf(componentClasses);
    }

    /**
     * Registers a class with a componentBuilder. For example, the built-in componentBuilders are:
     * - Material.class, MaterialComponetSetBuilder.class
     * - Spatial.class, SpatialComponentSetBuilder.class
     *
     * All classes that are not registered are processed using the ReflectedComponentSetBuilder.
     *
     * @param clazz                 the class to map
     * @param componentBuilderClass the componentBuilder to create for the mapped class.
     */
    public void registerComponentBuilder(Class<?> clazz, Class<? extends AbstractComponentSetBuilder<?>> componentBuilderClass) {
        componentBuilderClasses.put(clazz, componentBuilderClass);
    }

    public Map<Class<?>, Class<? extends AbstractComponentSetBuilder<?>>> getComponentBuilderClasses() {
        return ImmutableMap.copyOf(componentBuilderClasses);
    }

    public Class<? extends AbstractComponentSetBuilder<?>> getComponentSetBuilderFor(Class<?> clazz) {
        return componentBuilderClasses.get(clazz);
    }

    /**
     * Registers a Control with a component.
     * @param clazz                 the control to map.
     * @param controlComponentClass the component to create for the mapped class.
     */
    public void registerControlComponent(Class<? extends Control> clazz, Class<? extends ControlSdkComponent<?>> controlComponentClass) {
        controlComponentClasses.put(clazz, controlComponentClass);
    }

    public Map<Class<? extends Control>, Class<? extends ControlSdkComponent<?>>> getControlComponentClasses() {
        return ImmutableMap.copyOf(controlComponentClasses);
    }

    public Class<? extends ControlSdkComponent<?>> getControlSdkComponentFor(Class<? extends Control> controlClass) {
        return controlComponentClasses.get(controlClass);
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
