package com.jayfella.devkit.properties.component.events;

import com.jayfella.devkit.event.Event;
import com.jme3.scene.Spatial;

public class SpatialRemovedEvent extends Event {

    private final Spatial spatial;

    public SpatialRemovedEvent(Spatial spatial) {
        this.spatial = spatial;
    }

    public Spatial getSpatial() {
        return spatial;
    }

}
