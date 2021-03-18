package fr.exratio.jme.devkit.event;

public interface EventManager {

  void register(Object eventListener);

  void unregister(Object eventListener);

  void post(Object event);

}
