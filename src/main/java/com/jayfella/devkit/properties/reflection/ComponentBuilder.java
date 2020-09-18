package com.jayfella.devkit.properties.reflection;

import com.jayfella.devkit.properties.component.ReflectedSdkComponent;
import com.jayfella.devkit.service.RegistrationService;
import com.jayfella.devkit.service.ServiceManager;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class ComponentBuilder {

    private final List<ReflectedSdkComponent<?>> sdkComponents = new ArrayList<>();

    private final UniqueProperties props;

    public ComponentBuilder(UniqueProperties props) {
        this.props = props;
    }

    public void build() {

        Map<Class<?>, Class<? extends ReflectedSdkComponent<?>>> componentClasses =
                ServiceManager.getService(RegistrationService.class).getComponentClasses();

        props.getGetters().sort(Comparator.comparing(Method::getName));

        for (Method getter : props.getGetters()) {

            Map.Entry<Class<?>, Class<? extends ReflectedSdkComponent<?>>> entry = componentClasses.entrySet().stream()
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

                    Class<? extends ReflectedSdkComponent<?>> componentClass = entry.getValue();
                    Constructor<? extends ReflectedSdkComponent<?>> constructor = componentClass.getConstructor(Object.class, Method.class, Method.class);
                    ReflectedSdkComponent<?> sdkComponent = constructor.newInstance(props.getObject(), getter, setter);

                    sdkComponent.setPropertyName(UniqueProperties.getSuffix(getter.getName()));

                    sdkComponents.add(sdkComponent);

                } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public List<ReflectedSdkComponent<?>> getSdkComponents() {
        return sdkComponents;
    }

}
