package com.jayfella.importer.properties.reflection;

import com.jayfella.importer.properties.component.EnumComponent;
import com.jayfella.importer.properties.component.SdkComponent;
import com.jayfella.importer.service.ComponentRegistrationService;
import com.jayfella.importer.service.ServiceManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ComponentBuilder {

    // private final Map<Class<?>, Class<? extends SdkComponent>> componentClasses = new HashMap<>();
    private final List<SdkComponent> sdkComponents = new ArrayList<>();

    private final UniqueProperties props;

    public ComponentBuilder(UniqueProperties props) {

        this.props = props;

        // add the default supported components
        // these should be all the objects commonly used in jmonkey.

//        componentClasses.put(boolean.class, BooleanComponent.class);
//        componentClasses.put(ColorRGBA.class, ColorRGBAComponent.class);
//        componentClasses.put(Enum.class, EnumComponent.class);
//        componentClasses.put(float.class, FloatComponent.class);
//        componentClasses.put(Quaternion.class, QuaternionComponent.class);
//        componentClasses.put(Vector3f.class, Vector3fComponent.class);
//        componentClasses.put(Vector4f.class, Vector4fComponent.class);
//
//        componentClasses.put(Material.class, MaterialComponent.class);

    }

//    public void registerComponent(Class<?> clazz, Class<? extends SdkComponent> component) {
//        componentClasses.put(clazz, component);
//    }

    public void build() {

        Map<Class<?>, Class<? extends SdkComponent<?>>> componentClasses =
                ServiceManager.getService(ComponentRegistrationService.class).getComponentClasses();

        props.getGetters().sort(Comparator.comparing(Method::getName));

        for (Method getter : props.getGetters()) {

            Map.Entry<Class<?>, Class<? extends SdkComponent<?>>> entry = componentClasses.entrySet().stream()
                    .filter(c -> getter.getReturnType() == c.getKey() || ( getter.getReturnType().isEnum() && c.getKey() == Enum.class ) )
                    .findFirst()
                    .orElse(null);

            if (entry != null) {

                Method setter = props.getSetters().stream()
                        .filter(s -> {

                            String getterSuffix = UniqueProperties.getSuffix(getter.getName());
                            String setterSuffix = UniqueProperties.getSuffix(s.getName());

                            return getterSuffix.equalsIgnoreCase(setterSuffix);

                        })
                        .findFirst()
                        .orElse(null);

                try {

                    Class<? extends SdkComponent<?>> componentClass = entry.getValue();
                    Constructor<? extends SdkComponent<?>> constructor = componentClass.getConstructor(Object.class, Method.class, Method.class);
                    SdkComponent<?> sdkComponent = constructor.newInstance(props.getObject(), getter, setter);

                    sdkComponent.setPropertyName(UniqueProperties.getSuffix(getter.getName()));

                    if (getter.getReturnType().isEnum()) {

                        Class<? extends Enum> values = (Class<? extends Enum>) getter.getReturnType();

                        EnumComponent enumComponent = (EnumComponent) sdkComponent;
                        enumComponent.setEnumValues(values);
                    }


                    sdkComponents.add(sdkComponent);

                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public List<SdkComponent> getSdkComponents() {
        return sdkComponents;
    }

}
