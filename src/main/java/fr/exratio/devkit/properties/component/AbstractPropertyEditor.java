package fr.exratio.devkit.properties.component;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;

public abstract class AbstractPropertyEditor<T> implements PropertyEditor {

  protected T value;
  private Object source;
  private java.util.Vector<PropertyChangeListener> listeners;
  protected PropertyChangeListener propertyChangeListener = evt -> {
    if ((evt.getOldValue() != null && evt.getOldValue().equals(evt.getNewValue()))) {
      return;
    }
    T oldComponent = value;
    T newComponent = computeValue();
    if (!oldComponent.equals(newComponent)) {
      setTypedValue(newComponent);
    }
  };

  public AbstractPropertyEditor(T component) {
    this.source = this;
    this.value = component;
  }

  public AbstractPropertyEditor(Object source, T component) {
    this.source = source;
    this.value = component;
  }

  public T getValue() {
    return value;
  }

  @Override
  public void setValue(Object value) {
    setTypedValue((T) value);
  }

  @Override
  public boolean isPaintable() {
    return false;
  }

  @Override
  public void paintValue(Graphics gfx, Rectangle box) {
  }

  @Override
  public String getJavaInitializationString() {
    return null;
  }

  @Override
  public String getAsText() {
    return null;
  }

  @Override
  public void setAsText(String text) throws IllegalArgumentException {

  }

  @Override
  public String[] getTags() {
    return new String[0];
  }

  @Override
  public boolean supportsCustomEditor() {
    return true;
  }

  public void setTypedValue(T newValue) {
    this.value = newValue;
    firePropertyChange();
  }

  /**
   * Returns the bean that is used as the source of events. If the source has not been explicitly
   * set then this instance of {@code PropertyEditorSupport} is returned.
   *
   * @return the source object or this instance
   * @since 1.5
   */
  public Object getSource() {
    return source;
  }

  /**
   * Sets the source bean.
   * <p>
   * The source bean is used as the source of events for the property changes. This source should be
   * used for information purposes only and should not be modified by the PropertyEditor.
   *
   * @param source source object to be used for events
   * @since 1.5
   */
  public void setSource(Object source) {
    this.source = source;
  }

  protected abstract T computeValue();

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
      targets = unsafeClone(listeners);
    }
    // Tell our listeners that "everything" has changed.
    PropertyChangeEvent evt = new PropertyChangeEvent(source, null, null, value);

    for (int i = 0; i < targets.size(); i++) {
      PropertyChangeListener target = targets.elementAt(i);
      target.propertyChange(evt);
    }
  }

  @SuppressWarnings("unchecked")
  private <T> java.util.Vector<T> unsafeClone(java.util.Vector<T> v) {
    return (java.util.Vector<T>) v.clone();
  }

}
