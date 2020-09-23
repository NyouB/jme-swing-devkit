package com.jayfella.devkit.properties.component.events;

import com.jayfella.devkit.event.Event;
import com.jme3.light.Light;

public class LightRemovedEvent extends Event {

    private final Light light;

    public LightRemovedEvent(Light light) {
        this.light = light;
    }

    public Light getLight() {
        return light;
    }

}
