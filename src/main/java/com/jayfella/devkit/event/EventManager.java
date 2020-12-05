package com.jayfella.devkit.event;

public interface EventManager {

  void registerEventListener(EventListener eventListener);

  void tryRegisterEventListener(EventListener eventListener) throws EventThreadingException;

  void unregisterEventListener(EventListener eventListener);

  void tryUnregisterEventListener(EventListener eventListener) throws EventThreadingException;

  void fireEvent(Event event);

  void tryFireEvent(Event event) throws EventThreadingException;

}
