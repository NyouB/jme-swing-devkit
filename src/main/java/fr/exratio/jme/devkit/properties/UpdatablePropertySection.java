package fr.exratio.jme.devkit.properties;

import fr.exratio.jme.devkit.jme.IgnoredProperties;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

public abstract class UpdatablePropertySection<T> extends PropertySection {

  public static final List<String> IGNORED_PROPERTIES = Arrays
      .asList(IgnoredProperties.material.clone()).stream()
      .map(String::toLowerCase)
      .collect(Collectors.toList());

  protected T object;
  protected T oldValue;
  private java.util.Vector<PropertyChangeListener> listeners;

  public UpdatablePropertySection(String title, T object) {
    super("Material");
    this.object = object;
  }

  public void updateSection(T section) {
    oldValue = object;
    object = section;
    buildView();
    firePropertyChange();
  }

  protected abstract void buildView();

  /**
   * Adds a listener for the value change. When the property editor changes its value it should fire
   * a {@link PropertyChangeEvent} on all registered {@link PropertyChangeListener}s, specifying the
   * {@code null} value for the property name. If the source property is set, it should be used as
   * the source of the event.
   * <p>
   * The same listener object may be added more than once, and will be called as many times as it is
   * added. If {@code listener} is {@code null}, no exception is thrown and no action is taken.
   *
   * @param listener the {@link PropertyChangeListener} to add
   */
  public synchronized void addPropertyChangeListener(
      PropertyChangeListener listener) {
    if (listeners == null) {
      listeners = new java.util.Vector<>();
    }
    listeners.addElement(listener);
  }

  /**
   * Removes a listener for the value change.
   * <p>
   * If the same listener was added more than once, it will be notified one less time after being
   * removed. If {@code listener} is {@code null}, or was never added, no exception is thrown and no
   * action is taken.
   *
   * @param listener the {@link PropertyChangeListener} to remove
   */
  public synchronized void removePropertyChangeListener(
      PropertyChangeListener listener) {
    if (listeners == null) {
      return;
    }
    listeners.removeElement(listener);
  }

  /**
   * Report that we have been modified to any interested listeners.
   */
  public void firePropertyChange() {
    java.util.Vector<PropertyChangeListener> targets;
    synchronized (this) {
      if (listeners == null) {
        return;
      }
      targets = (Vector<PropertyChangeListener>) listeners.clone();
    }
    // Tell our listeners that "everything" has changed.
    PropertyChangeEvent evt = new PropertyChangeEvent(this, null, null, object);

    for (int i = 0; i < targets.size(); i++) {
      PropertyChangeListener target = targets.elementAt(i);
      target.propertyChange(evt);
    }
  }

}