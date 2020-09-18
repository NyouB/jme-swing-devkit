package com.jayfella.devkit.service;

import com.jayfella.devkit.clipboard.SpatialClipboardItem;

/**
 * Keeps a reference of a copied item. Currently only stores a single item.
 */
public class ClipboardService implements Service {

    private final long threadId;
    private SpatialClipboardItem spatialClipboardItem;

    public ClipboardService() {
        threadId = Thread.currentThread().getId();
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
    public long getThreadId() {
        return threadId;
    }

    @Override
    public void stop() {
        // just remove the references to the objects.
        spatialClipboardItem = null;
    }

}
