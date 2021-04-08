package fr.exratio.jme.devkit.service;

import com.google.common.eventbus.EventBus;
import fr.exratio.jme.devkit.event.EventManager;


public class EventService implements EventManager {

  private final EventBus eventBus = new EventBus();

  public EventService() {
  }

  @Override
  public void register(Object eventListener) {
    eventBus.register(eventListener);
  }

  @Override
  public void unregister(Object eventListener) {
    eventBus.unregister(eventListener);
  }

  @Override
  public void post(Object event) {
    eventBus.post(event);
  }
}
