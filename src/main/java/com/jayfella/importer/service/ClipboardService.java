package com.jayfella.importer.service;

import com.jayfella.importer.clipboard.SpatialClipboardItem;

/**
 * Keeps a reference of a copied item. Currently only stores a single item.
 */
public class ClipboardService implements Service {

    private SpatialClipboardItem spatialClipboardItem;

    public ClipboardService() {

    }

    public SpatialClipboardItem getSpatialClipboardItem() {
        return spatialClipboardItem;
    }

    public void setSpatialClipboardItem(SpatialClipboardItem spatialClipboardItem) {
        this.spatialClipboardItem = spatialClipboardItem;
    }

    public boolean hasSpatialClipboardItem() {
        return spatialClipboardItem != null;
    }

    @Override
    public void stop() {
        // just remove the references to the objects.
        spatialClipboardItem = null;
    }

}
