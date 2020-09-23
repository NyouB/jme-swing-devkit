package com.jayfella.devkit.properties.component.events;

import com.jayfella.devkit.event.Event;
import com.jme3.light.Light;
import com.jme3.scene.Spatial;

public class LightCreatedEvent extends Event {

    private final Light light;

    public LightCreatedEvent(Light light) {
        this.light = light;
    }

    public Light getLight() {
        return light;
    }

}
