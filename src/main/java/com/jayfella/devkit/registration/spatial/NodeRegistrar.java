package com.jayfella.devkit.registration.spatial;

import com.jayfella.devkit.registration.AbstractClassRegistrar;
import com.jme3.scene.Node;

public abstract class NodeRegistrar extends AbstractClassRegistrar<Node> {

    public NodeRegistrar(Class<? extends Node> clazz) {
        setRegisteredClass(clazz);
    }
}
