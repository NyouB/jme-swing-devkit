package com.jayfella.devkit.registration;

import java.util.HashSet;
import java.util.Set;

public class Registrar<T extends ClassRegistrar<?>> {

    private final Set<T> registrations = new HashSet<>();

    private Class<T> classType;

    public Registrar(Class<T> classType) {
        this.classType = classType;
    }

    public Class<T> getClassType() {
        return classType;
    }

    public void register(T registrar) {

        Class<?> classToRegister = registrar.getRegisteredClass();

        Class<?> existingClass = registrations.stream()
                .filter(r -> r.getRegisteredClass().getName().equals(classToRegister.getName()))
                .findFirst()
                .map(ClassRegistrar::getRegisteredClass)
                .orElse(null);

        if (existingClass == null) {
            registrations.add(registrar);
        }
    }

    public Set<T> getRegistrations() {
        return registrations;
    }

}
