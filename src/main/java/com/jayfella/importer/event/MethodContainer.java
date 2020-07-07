package com.jayfella.importer.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodContainer {

    private final Object parent;
    private final Method method;
    private final boolean ignoreCancelled;

    public MethodContainer(Object parent, Method method, boolean ignoreCancelled) {
        this.parent = parent;
        this.method = method;
        this.ignoreCancelled = ignoreCancelled;
    }

    public Object getParent() {
        return parent;
    }

    public Method getMethod() {
        return method;
    }

    public boolean ignoreCancelled() {
        return ignoreCancelled;
    }

    public void invokeMethod(Object value) throws InvocationTargetException, IllegalAccessException {

        method.invoke(parent, value);
    }

}
