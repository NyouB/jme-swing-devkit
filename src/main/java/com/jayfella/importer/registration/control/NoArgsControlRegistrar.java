package com.jayfella.importer.registration.control;

import com.jayfella.importer.tree.control.ControlTreeNode;
import com.jme3.app.SimpleApplication;
import com.jme3.scene.control.Control;

import javax.swing.tree.TreeNode;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class NoArgsControlRegistrar extends ControlRegistrar {

    public static NoArgsControlRegistrar create(Class<? extends Control> controlClass) {
        NoArgsControlRegistrar registrar = new NoArgsControlRegistrar();
        registrar.setRegisteredClass(controlClass);
        return registrar;
    }

    @Override
    public Control createInstance(SimpleApplication application) {

        try {
            Constructor<? extends Control> constructor = getRegisteredClass().getConstructor();
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public TreeNode createSceneTreeNode(Control control, SimpleApplication application) {
        return new ControlTreeNode(control);
    }

}
