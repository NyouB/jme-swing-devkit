package com.jayfella.importer.registration.spatial;

import com.jayfella.importer.registration.AbstractClassRegistrar;
import com.jme3.scene.Node;

public abstract class NodeRegistrar extends AbstractClassRegistrar<Node> {

    public NodeRegistrar(Class<? extends Node> clazz) {
        setRegisteredClass(clazz);
    }
}
