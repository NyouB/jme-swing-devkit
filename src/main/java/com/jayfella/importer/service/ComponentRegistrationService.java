package com.jayfella.importer.service;

import com.google.common.collect.ImmutableMap;
import com.jayfella.importer.properties.builder.AbstractComponentSetBuilder;
import com.jayfella.importer.properties.component.*;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;

import java.util.HashMap;
import java.util.Map;

/**
 * A service that binds types to components. Used for creating components from reflected values to allow modifying the value.
 */
public class ComponentRegistrationService implements Service {

    private final Map<Class<?>, Class<? extends SdkComponent>> componentClasses = new HashMap<>();
    private final Map<Class<?>, Class<? extends AbstractComponentSetBuilder<?>>> componentBuilderClasses = new HashMap<>();

    public ComponentRegistrationService() {

        // register all the built-in components.
        componentClasses.put(boolean.class, BooleanComponent.class);
        componentClasses.put(ColorRGBA.class, ColorRGBAComponent.class);
        componentClasses.put(Enum.class, EnumComponent.class);
        componentClasses.put(float.class, FloatComponent.class);
        componentClasses.put(int.class, IntegerComponent.class);
        componentClasses.put(Material.class, MaterialComponent.class);
        componentClasses.put(String.class, StringComponent.class);
        componentClasses.put(Vector3f.class, Vector3fComponent.class);
        componentClasses.put(Vector4f.class, Vector4fComponent.class);
        componentClasses.put(Quaternion.class, QuaternionComponent.class);

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
     * @param clazz
     * @param componentClass
     */
    public void registerComponent(Class<?> clazz, Class<? extends SdkComponent> componentClass) {
        componentClasses.put(clazz, componentClass);
    }

    public Map<Class<?>, Class<? extends SdkComponent>> getComponentClasses() {
        return ImmutableMap.copyOf(componentClasses);
    }

    /**
     * Registers a class with a componentBuilder. For example, the built-in componentBuilders are:
     * - Material.class, MaterialComponetSetBuilder.class
     * - Spatial.class, SpatialComponentSetBuilder.class
     *
     * All classes that are not registered are processed using the ReflectedComponentSetBuilder.
     *
     * @param clazz
     * @param componentBuilderClass
     */
    public void registerComponentBuilder(Class<?> clazz, Class<? extends AbstractComponentSetBuilder<?>> componentBuilderClass) {
        componentBuilderClasses.put(clazz, componentBuilderClass);
    }

    public Map<Class<?>, Class<? extends AbstractComponentSetBuilder<?>>> getComponentBuilderClasses() {
        return ImmutableMap.copyOf(componentBuilderClasses);
    }

}
