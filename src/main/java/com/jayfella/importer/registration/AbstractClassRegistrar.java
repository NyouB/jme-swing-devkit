package com.jayfella.importer.registration;

public abstract class AbstractClassRegistrar<T> implements ClassRegistrar<T> {

    private Class<? extends T> registeredClass;

    public AbstractClassRegistrar() {

    }

    @Override
    public void setRegisteredClass(Class<? extends T> classToRegister) {
        this.registeredClass = classToRegister;
    }

    @Override
    public Class<? extends T> getRegisteredClass() {
        return registeredClass;
    }

}
