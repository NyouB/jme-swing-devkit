package com.jayfella.devkit.properties.reflection;

import org.reflections.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class UniqueProperties {

    private final Object object;
    private final List<Method> getters = new ArrayList<>();
    private final List<Method> setters = new ArrayList<>();

    private final String[] ignoredProperties;

    public UniqueProperties(Object object, String... ignoredProperties) {
        this.object = object;
        this.ignoredProperties = ignoredProperties;
        create();
    }

    public Object getObject() {
        return object;
    }

    public List<Method> getGetters() {
        return getters;
    }

    public List<Method> getSetters() {
        return setters;
    }

    private void create() {

        Set<Method> getters = ReflectionUtils.getAllMethods(object.getClass(),
                org.reflections.ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withPrefix("get"),
                ReflectionUtils.withParametersCount(0));

        Set<Method> gettersBoolean = ReflectionUtils.getAllMethods(object.getClass(),
                ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withPrefix("is"),
                ReflectionUtils.withParametersCount(0));

        getters.addAll(gettersBoolean);

        Set<Method> setters = ReflectionUtils.getAllMethods(object.getClass(),
                org.reflections.ReflectionUtils.withModifier(Modifier.PUBLIC),
                ReflectionUtils.withPrefix("set"),
                ReflectionUtils.withParametersCount(1));

//        Set<Method> settersBoolean = ReflectionUtils.getAllMethods(object.getClass(),
//                ReflectionUtils.withModifier(Modifier.PUBLIC),
//                ReflectionUtils.withPrefix("is"),
//                ReflectionUtils.withParametersCount(1));
//
//        setters.addAll(settersBoolean);

        // remove any getters that are declared ignored.
        removeIgnoredGetters(getters);

        // remove all getters that have no matching setters
        cleanGetters(getters, setters);

        // remove all setters whose parameter is not the same as the getter return type.
        // e.g. getLocalScale returns Vector3f, setLocalScale must accept Vector3f as parameter.
        cleanSetters(getters, setters);

        // AGAIN remove all getters that have no matching setters
        // we may have removed some setters that don't conform, so we need to remove those getters we can't deal with.
        cleanGetters(getters, setters);

        this.getters.addAll(getters);
        this.setters.addAll(setters);

    }

    private void removeIgnoredGetters(Set<Method> getters) {

        getters.removeIf(getter -> {

            String suffix = getSuffix(getter.getName());

            for (String ignore : ignoredProperties) {
                if (ignore.equalsIgnoreCase(suffix)) {
                    return true;
                }
            }

            return false;

        });

    }

    private void cleanGetters(Set<Method> getters, Set<Method> setters) {
        getters.removeIf(getter -> {

            if (getter.toString().toLowerCase().contains("abstract")) {
                return true;
            }

            String suffix = getSuffix(getter.getName());

            Method setMethod = setters.stream().filter(m -> {

                String setterSuffix = getSuffix(m.getName());
                return setterSuffix.equalsIgnoreCase(suffix);

            })
                    .findFirst()
                    .orElse(null);

            return setMethod == null;
        });
    }

    private void cleanSetters(Set<Method> getters, Set<Method> setters) {
        setters.removeIf(setter -> {

            if (setter.toString().toLowerCase().contains("abstract")) {
                return true;
            }

            String suffix = getSuffix(setter.getName());

            Method getter = getters.stream().filter(g -> {
                String getterSuffix = getSuffix(g.getName());
                return getterSuffix.equalsIgnoreCase(suffix);
            })
                    .findFirst()
                    .orElse(null);

            if (getter == null) {
                return true;
            }

            Class<?>[] params = setter.getParameterTypes();

            boolean same =  params[0].isAssignableFrom(getter.getReturnType());
            return !same;

        });
    }

    public static String getSuffix(final String name) {

        String response = name;

        if (name.startsWith("is"))
            response =  name.substring(2);
        else if (name.startsWith("get"))
            response = name.substring(3);
        else if (name.startsWith("set"))
            response = name.substring(3);

        return response;
    }

}
