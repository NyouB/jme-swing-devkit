package com.jayfella.importer.event;

public abstract class Event {

    private final boolean async;
    private boolean cancelled;

    public Event() {
        this(false);
    }

    public Event(boolean async) {
        this.async = async;
    }

    public boolean isAsync() {
        return async;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}
