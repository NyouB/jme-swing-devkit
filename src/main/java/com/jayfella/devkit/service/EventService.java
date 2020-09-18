package com.jayfella.devkit.service;

import com.jayfella.devkit.event.*;
import com.jayfella.devkit.event.EventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EventService implements EventManager, Service {

    private static final Logger log = Logger.getLogger(EventService.class.getName());

    private static final String EVENT_REGISTERED = "Registered method '%s(%s)' in Listener '%s'.";
    private static final String EVENT_UNREGISTERED = "Unregistered method '%s(%s)' in Listener '%s'.";

    private static final String EVENT_FIRING = "Firing '%s' Event at method '%s(%s)' in Listener '%s'.";
    private static final String EVENT_FIRED = "Fired '%s' Event at method '%s(%s)' in Listener '%s'.";

    // private final Thread primaryThread;
    private final long threadId;

    private final EnumMap<EventPriority, Map<Class<? extends Event>, List<MethodContainer>>> eventListenerMap = new EnumMap<>(EventPriority.class);

    public EventService() {
        threadId = Thread.currentThread().getId();
    }

    /**
     * Registers an event listener safely.
     *
     * @param eventListener the EventListener to register.
     */
    @Override
    public void registerEventListener(EventListener eventListener) {
        try {
            tryRegisterEventListener(eventListener);
        } catch (EventThreadingException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    /**
     * Registers an event listener.
     *
     * @param eventListener the EventListener to register.
     *
     * @throws EventThreadingException if the event was registered on a thread other than the main thread.
     */
    @Override
    public void tryRegisterEventListener(EventListener eventListener) throws EventThreadingException {

        if (!isPrimaryThread()) {
            throw new EventThreadingException("Event Listeners must be registered on the main thread.");
        }

        Method[] methods = eventListener.getClass().getMethods();

        for (final Method method : methods) {

            final EventHandler eventHandler = method.getAnnotation(EventHandler.class);

            if (eventHandler == null || method.isBridge() || method.isSynthetic()) {
                continue;
            }

            // To maintain java 8 compatibility we use isAccessible().
            // boolean accessible = method.canAccess(listener);

            @SuppressWarnings("deprecation")
            boolean accessible = method.isAccessible();

            if (!accessible) {
                method.setAccessible(true);
            }

            EventPriority priority = eventHandler.priority();

            // get the parameter of the method. Should be an event.
            Class<?>[] classes = method.getParameterTypes();

            // ignore if the method doesn't have a single parameter or the parameter isn't an event.
            if (classes.length != 1 || !classes[0].getSuperclass().isAssignableFrom(Event.class)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Class<? extends Event> eventClass = (Class<? extends Event>) classes[0];

            Map<Class<? extends Event>, List<MethodContainer>> stuff = eventListenerMap
                    .computeIfAbsent(priority, k -> new HashMap<>());

            List<MethodContainer> eventMethods = stuff.computeIfAbsent(eventClass, k -> new ArrayList<>());

            eventMethods.add(new MethodContainer(eventListener, method, eventHandler.ignoreCancelled()));

            log.info(String.format(EVENT_REGISTERED,
                    method.getName(),
                    eventClass.getSimpleName(),
                    eventListener.getClass().getSimpleName()
                    ));


            // don't set it as inaccessible. It's not necessary.
            // if (!accessible) {
            //     method.setAccessible(false);
            // }

        }

    }

    /**
     * Unregisters an event listener safely.
     *
     * @param eventListener the EventListener to unregister.
     */
    @Override
    public void unregisterEventListener(EventListener eventListener) {
        try {
            tryUnregisterEventListener(eventListener);
        } catch (EventThreadingException e) {
            log.severe(e.getMessage());
        }
    }

    /**
     * Unregisters an event listener.
     *
     * @param eventListener the EventListener to unregister.
     *
     * @throws EventThreadingException if the event was unregistered on a thread other than the main thread.
     */
    @Override
    public void tryUnregisterEventListener(EventListener eventListener) throws EventThreadingException {

        if (!isPrimaryThread()) {
            throw new EventThreadingException("Event Listeners must be registered on the main thread.");
        }

        eventListenerMap.forEach( (key, val) -> {

            Method[] methods = eventListener.getClass().getMethods();

            for (Method method : methods) {

                final EventHandler eventHandler = method.getAnnotation(EventHandler.class);

                if (eventHandler == null || method.isBridge() || method.isSynthetic()) {
                    continue;
                }

                // get the parameter of the method. Should be an event.
                Class<?>[] classes = method.getParameterTypes();

                // ignore if the method doesn't have a single parameter or the parameter isn't an event.
                if (classes.length != 1 || !classes[0].getSuperclass().isAssignableFrom(Event.class) ) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                Class<? extends Event> eventClass = (Class<? extends Event>) classes[0];

                List<MethodContainer> methodContainers = val.get(eventClass);

                methodContainers.removeIf(container -> {
                    boolean contains =  container.getParent() == eventListener;

                    log.info(String.format(EVENT_UNREGISTERED,
                            container.getMethod().getName(),
                            eventClass.getSimpleName(),
                            container.getParent().getClass().getSimpleName()
                    ));

                    return contains;
                });

                // if there are no more listeners, remove the empty list.
                if (methodContainers.isEmpty()) {
                    val.remove(eventClass);
                }

            }

        });

    }

    /**
     * Fires the given event to all listeners safely.
     *
     * @param event the event to send to all listeners.
     */
    @Override
    public void fireEvent(Event event) {
        try {
            tryFireEvent(event);
        } catch (EventThreadingException e) {
            log.severe(e.getMessage());
        }
    }

    /**
     * Fires the given event to all listeners.
     *
     * @param event the event to send to all listeners.
     *
     * @throws EventThreadingException if the event was not fired in accordance with its threading status.
     * - A synchronous event must be fired from the main thread.
     * - An asynchronous event must not be fired from synchronous code.
     * - An asynchronous event must not be fired from the main thread.
     */
    @Override
    public void tryFireEvent(Event event) throws EventThreadingException {

        log.fine("Attempting to fire event '" + event.getClass().getSimpleName() + "'.");

        if (event.isAsync()) {

            if (Thread.holdsLock(event)) {
                throw new EventThreadingException(event.getClass().getSimpleName() + " IGNORED. Event is marked as asynchronous but has been fired from synchronous code.");
            }

            if (isPrimaryThread()) {
                throw new EventThreadingException(event.getClass().getSimpleName() + " IGNORED. Event is marked as asynchronous but has been fired from the main thread.");
            }

        }
        else {

            if (!isPrimaryThread()) {
                throw new EventThreadingException(event.getClass().getSimpleName() + " IGNORED. Event is marked as synchronous but has been fired from a different thread.");
            }

        }

        // the iteration will be in the same order as the enum
        for (Map.Entry<EventPriority, Map<Class<? extends Event>, List<MethodContainer>>> entry : eventListenerMap.entrySet()) {

            List<MethodContainer> methodContainers = entry.getValue().get(event.getClass());

            if (methodContainers == null) {
                continue;
            }

            for (MethodContainer methodContainer : methodContainers) {

                if (event.isCancelled() && !methodContainer.ignoreCancelled()) {
                    continue;
                }

                try {

                    log.fine(String.format(EVENT_FIRING,
                            event.getClass().getSimpleName(),
                            methodContainer.getMethod().getName(),
                            event.getClass().getSimpleName(),
                            methodContainer.getParent().getClass().getSimpleName()
                    ));

                    methodContainer.invokeMethod(event);

                    log.fine(String.format(EVENT_FIRED,
                            event.getClass().getSimpleName(),
                            methodContainer.getMethod().getName(),
                            event.getClass().getSimpleName(),
                            methodContainer.getParent().getClass().getSimpleName()
                    ));

                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }

            }

        }

    }

    public int getRegisteredEventCount(EventPriority priority) {
        Map<Class<? extends Event>, List<MethodContainer>> priorityMap = eventListenerMap.get(priority);
        return priorityMap == null ? 0 : priorityMap.size();
    }

    /**
     * Determines whether or not the thread that called this method is the main thread.
     * @return whether or not the thread that called this method is the main thread.
     */
    private boolean isPrimaryThread() {
        return Thread.currentThread().getId() == threadId;
    }

    @Override
    public long getThreadId() {
        return threadId;
    }

    @Override
    public void stop() {

    }

}
