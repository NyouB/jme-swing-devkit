package com.jayfella.importer.service;

public interface Service {

    /**
     * Returns the id of the thread that is required to access this service safely, or a negative number if any thread
     * can access this service.
     *
     * @return the threadId of the thread that should be used to access this service, or a negative number if any thread
     * can access this service.
     */
    long getThreadId();

    void stop();

}
