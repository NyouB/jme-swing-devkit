package com.jayfella.devkit.properties.builder;

import com.jayfella.devkit.properties.ComponentSetBuilder;
import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.*;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CustomPropertyBuilder implements ComponentSetBuilder<Object> {

    private static final Logger log = Logger.getLogger(CustomPropertyBuilder.class.getName());

    private final List<CustomProperty> properties = new ArrayList<>();
    private final Object object;

    public CustomPropertyBuilder(Object object) {
        this.object = object;
    }

    public void addProperty(CustomProperty property) {
        properties.add(property);
    }

    public void addProperty(String propertyName, String declaredGetter, String declaredSetter, Class<?> returnType) {
        properties.add(new CustomProperty(propertyName, declaredGetter, declaredSetter, returnType));
    }

    @Override
    public List<PropertySection> build() {

        ReflectedSdkComponent<?>[] components = new ReflectedSdkComponent[properties.size()];

        for (int i = 0; i < properties.size(); i++) {

            CustomProperty property = properties.get(i);
            ReflectedSdkComponent<?> component = null;

            if (property.getReturnType() == boolean.class) {
                try {
                    component = new BooleanComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            else if (property.getReturnType() == ColorRGBA.class) {
                try {
                    component = new ColorRGBAComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            else if (property.getReturnType().isEnum()) {
                try {
                    component = new EnumComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            else if (property.getReturnType() == float.class) {
                try {
                    component = new FloatComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            else if (property.getReturnType() == int.class) {
                try {
                    component = new IntegerComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            else if (property.getReturnType() == String.class) {
                try {
                    component = new StringComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

            else if (property.getReturnType() == Vector3f.class) {
                try {
                    component = new Vector3fComponent(object,
                            object.getClass().getDeclaredMethod(property.getDeclaredGetter()),
                            object.getClass().getDeclaredMethod(property.getDeclaredSetter(), property.getReturnType()));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }


            else {
                log.warning("Unable to create Property for: " + property.getReturnType());
            }

            if (component != null) {
                component.setPropertyName(property.getPropertyName());
                components[i] = component;
            }

        }

        PropertySection propertySection = new PropertySection(object.getClass().getSimpleName(), components);

        List<PropertySection> sections = new ArrayList<>();
        sections.add(propertySection);

        return sections;
    }

    public static class CustomProperty {

        private final String propertyName;
        private final String declaredGetter;
        private final String declaredSetter;
        private final Class<?> returnType;

        public CustomProperty(String propertyName, String declaredGetter, String declaredSetter, Class<?> returnType) {
            this.propertyName = propertyName;
            this.declaredGetter = declaredGetter;
            this.declaredSetter = declaredSetter;
            this.returnType = returnType;
        }

        public String getPropertyName() {
            return propertyName;
        }

        public String getDeclaredGetter() {
            return declaredGetter;
        }

        public String getDeclaredSetter() {
            return declaredSetter;
        }

        public Class<?> getReturnType() {
            return returnType;
        }
    }

}
