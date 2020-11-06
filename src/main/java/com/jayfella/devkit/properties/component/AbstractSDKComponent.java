package com.jayfella.devkit.properties.component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSDKComponent<T> implements SdkComponent {
  /*
   * In some cases using "this" as an object to synchronize by
   * can lead to a deadlock if client code also uses synchronization
   * by a component object. For every such situation revealed we should
   * consider possibility of replacing "this" with the package private
   * objectLock object introduced below. So far there are 3 issues known:
   * - CR 6708322 (the getName/setName methods);
   * - CR 6608764 (the PropertyChangeListener machinery);
   * - CR 7108598 (the Container.paint/KeyboardFocusManager.clearMostRecentFocusOwner methods).
   *
   * Note: this field is considered final, though readObject() prohibits
   * initializing final fields.
   */
  private final transient Object objectLock = new Object();
  protected T component;
  protected String propertyName = "";
  private PropertyChangeSupport changeSupport;

  public AbstractSDKComponent(T component) {
    this.component = component;
    this.propertyName = toString();
  }

  public AbstractSDKComponent(T component, String propertyName) {
    this.component = component;
    this.propertyName = propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public T getComponent() {
    return component;
  }

  public void setComponent(T component) {
    this.component = component;
  }

  public void cleanup() {

  }

  public PropertyChangeListener getPropertyChangeListener() {
    return evt -> {
      if ((evt.getOldValue() !=null && evt.getOldValue().equals(evt.getNewValue()))) {
        return;
      }
      T oldComponent = component;
      T newComponent = computeValue();
      if(!oldComponent.equals(newComponent)){
        setComponent(newComponent);
        firePropertyChange(propertyName, oldComponent, component);
      }
    };
  }


  protected abstract T computeValue();

  Object getObjectLock() {
    return objectLock;
  }

  /**
   * Adds a PropertyChangeListener to the listener list. The listener is registered for all bound
   * properties of this class, including the following:
   * <ul>
   *    <li>this Component's font ("font")</li>
   *    <li>this Component's background color ("background")</li>
   *    <li>this Component's foreground color ("foreground")</li>
   *    <li>this Component's focusability ("focusable")</li>
   *    <li>this Component's focus traversal keys enabled state
   *        ("focusTraversalKeysEnabled")</li>
   *    <li>this Component's Set of FORWARD_TRAVERSAL_KEYS
   *        ("forwardFocusTraversalKeys")</li>
   *    <li>this Component's Set of BACKWARD_TRAVERSAL_KEYS
   *        ("backwardFocusTraversalKeys")</li>
   *    <li>this Component's Set of UP_CYCLE_TRAVERSAL_KEYS
   *        ("upCycleFocusTraversalKeys")</li>
   *    <li>this Component's preferred size ("preferredSize")</li>
   *    <li>this Component's minimum size ("minimumSize")</li>
   *    <li>this Component's maximum size ("maximumSize")</li>
   *    <li>this Component's name ("name")</li>
   * </ul>
   * Note that if this {@code Component} is inheriting a bound property, then no
   * event will be fired in response to a change in the inherited property.
   * <p>
   * If {@code listener} is {@code null},
   * no exception is thrown and no action is performed.
   *
   * @param listener the property change listener to be added
   * @see #removePropertyChangeListener
   * @see #getPropertyChangeListeners
   * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(
      PropertyChangeListener listener) {
    synchronized (getObjectLock()) {
      if (listener == null) {
        return;
      }
      if (changeSupport == null) {
        changeSupport = new PropertyChangeSupport(this);
      }
      changeSupport.addPropertyChangeListener(listener);
    }
  }

  /**
   * Removes a PropertyChangeListener from the listener list. This method should be used to remove
   * PropertyChangeListeners that were registered for all bound properties of this class.
   * <p>
   * If listener is null, no exception is thrown and no action is performed.
   *
   * @param listener the PropertyChangeListener to be removed
   * @see #addPropertyChangeListener
   * @see #getPropertyChangeListeners
   * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(
      PropertyChangeListener listener) {
    synchronized (getObjectLock()) {
      if (listener == null || changeSupport == null) {
        return;
      }
      changeSupport.removePropertyChangeListener(listener);
    }
  }

  /**
   * Returns an array of all the property change listeners registered on this component.
   *
   * @return all of this component's {@code PropertyChangeListener}s or an empty array if no
   * property change listeners are currently registered
   * @see #addPropertyChangeListener
   * @see #removePropertyChangeListener
   * @see #getPropertyChangeListeners(java.lang.String)
   * @see java.beans.PropertyChangeSupport#getPropertyChangeListeners
   * @since 1.4
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
    synchronized (getObjectLock()) {
      if (changeSupport == null) {
        return new PropertyChangeListener[0];
      }
      return changeSupport.getPropertyChangeListeners();
    }
  }

  /**
   * Adds a PropertyChangeListener to the listener list for a specific property. The specified
   * property may be user-defined, or one of the following:
   * <ul>
   *    <li>this Component's font ("font")</li>
   *    <li>this Component's background color ("background")</li>
   *    <li>this Component's foreground color ("foreground")</li>
   *    <li>this Component's focusability ("focusable")</li>
   *    <li>this Component's focus traversal keys enabled state
   *        ("focusTraversalKeysEnabled")</li>
   *    <li>this Component's Set of FORWARD_TRAVERSAL_KEYS
   *        ("forwardFocusTraversalKeys")</li>
   *    <li>this Component's Set of BACKWARD_TRAVERSAL_KEYS
   *        ("backwardFocusTraversalKeys")</li>
   *    <li>this Component's Set of UP_CYCLE_TRAVERSAL_KEYS
   *        ("upCycleFocusTraversalKeys")</li>
   * </ul>
   * Note that if this {@code Component} is inheriting a bound property, then no
   * event will be fired in response to a change in the inherited property.
   * <p>
   * If {@code propertyName} or {@code listener} is {@code null},
   * no exception is thrown and no action is taken.
   *
   * @param propertyName one of the property names listed above
   * @param listener the property change listener to be added
   * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   * @see #getPropertyChangeListeners(java.lang.String)
   * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   */
  public void addPropertyChangeListener(
      String propertyName,
      PropertyChangeListener listener) {
    synchronized (getObjectLock()) {
      if (listener == null) {
        return;
      }
      if (changeSupport == null) {
        changeSupport = new PropertyChangeSupport(this);
      }
      changeSupport.addPropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * Removes a {@code PropertyChangeListener} from the listener list for a specific property. This
   * method should be used to remove {@code PropertyChangeListener}s that were registered for a
   * specific bound property.
   * <p>
   * If {@code propertyName} or {@code listener} is {@code null}, no exception is thrown and no
   * action is taken.
   *
   * @param propertyName a valid property name
   * @param listener the PropertyChangeListener to be removed
   * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   * @see #getPropertyChangeListeners(java.lang.String)
   * @see #removePropertyChangeListener(java.beans.PropertyChangeListener)
   */
  public void removePropertyChangeListener(
      String propertyName,
      PropertyChangeListener listener) {
    synchronized (getObjectLock()) {
      if (listener == null || changeSupport == null) {
        return;
      }
      changeSupport.removePropertyChangeListener(propertyName, listener);
    }
  }

  /**
   * Returns an array of all the listeners which have been associated with the named property.
   *
   * @param propertyName the property name
   * @return all of the {@code PropertyChangeListener}s associated with the named property; if no
   * such listeners have been added or if {@code propertyName} is {@code null}, an empty array is
   * returned
   * @see #addPropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   * @see #removePropertyChangeListener(java.lang.String, java.beans.PropertyChangeListener)
   * @see #getPropertyChangeListeners
   * @since 1.4
   */
  public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
    synchronized (getObjectLock()) {
      if (changeSupport == null) {
        return new PropertyChangeListener[0];
      }
      return changeSupport.getPropertyChangeListeners(propertyName);
    }
  }

  /**
   * Support for reporting bound property changes for Object properties. This method can be called
   * when a bound property has changed and it will send the appropriate PropertyChangeEvent to any
   * registered PropertyChangeListeners.
   *
   * @param propertyName the property whose value has changed
   * @param oldValue the property's previous value
   * @param newValue the property's new value
   */
  protected void firePropertyChange(String propertyName,
      Object oldValue, Object newValue) {
    PropertyChangeSupport changeSupport;
    synchronized (getObjectLock()) {
      changeSupport = this.changeSupport;
    }
    if (changeSupport == null ||
        (oldValue != null && newValue != null && oldValue.equals(newValue))) {
      return;
    }
    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Support for reporting bound property changes for boolean properties. This method can be called
   * when a bound property has changed and it will send the appropriate PropertyChangeEvent to any
   * registered PropertyChangeListeners.
   *
   * @param propertyName the property whose value has changed
   * @param oldValue the property's previous value
   * @param newValue the property's new value
   * @since 1.4
   */
  protected void firePropertyChange(String propertyName,
      boolean oldValue, boolean newValue) {
    PropertyChangeSupport changeSupport = this.changeSupport;
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Support for reporting bound property changes for integer properties. This method can be called
   * when a bound property has changed and it will send the appropriate PropertyChangeEvent to any
   * registered PropertyChangeListeners.
   *
   * @param propertyName the property whose value has changed
   * @param oldValue the property's previous value
   * @param newValue the property's new value
   * @since 1.4
   */
  protected void firePropertyChange(String propertyName,
      int oldValue, int newValue) {
    PropertyChangeSupport changeSupport = this.changeSupport;
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    changeSupport.firePropertyChange(propertyName, oldValue, newValue);
  }

  /**
   * Reports a bound property change.
   *
   * @param propertyName the programmatic name of the property that was changed
   * @param oldValue the old value of the property (as a byte)
   * @param newValue the new value of the property (as a byte)
   * @see #firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   * @since 1.5
   */
  public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Byte.valueOf(oldValue), Byte.valueOf(newValue));
  }

  /**
   * Reports a bound property change.
   *
   * @param propertyName the programmatic name of the property that was changed
   * @param oldValue the old value of the property (as a char)
   * @param newValue the new value of the property (as a char)
   * @see #firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   * @since 1.5
   */
  public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Character.valueOf(oldValue), Character.valueOf(newValue));
  }

  /**
   * Reports a bound property change.
   *
   * @param propertyName the programmatic name of the property that was changed
   * @param oldValue the old value of the property (as a short)
   * @param newValue the new value of the property (as a short)
   * @see #firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   * @since 1.5
   */
  public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Short.valueOf(oldValue), Short.valueOf(newValue));
  }


  /**
   * Reports a bound property change.
   *
   * @param propertyName the programmatic name of the property that was changed
   * @param oldValue the old value of the property (as a long)
   * @param newValue the new value of the property (as a long)
   * @see #firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   * @since 1.5
   */
  public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Long.valueOf(oldValue), Long.valueOf(newValue));
  }

  /**
   * Reports a bound property change.
   *
   * @param propertyName the programmatic name of the property that was changed
   * @param oldValue the old value of the property (as a float)
   * @param newValue the new value of the property (as a float)
   * @see #firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   * @since 1.5
   */
  public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Float.valueOf(oldValue), Float.valueOf(newValue));
  }

  /**
   * Reports a bound property change.
   *
   * @param propertyName the programmatic name of the property that was changed
   * @param oldValue the old value of the property (as a double)
   * @param newValue the new value of the property (as a double)
   * @see #firePropertyChange(java.lang.String, java.lang.Object, java.lang.Object)
   * @since 1.5
   */
  public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    if (changeSupport == null || oldValue == newValue) {
      return;
    }
    firePropertyChange(propertyName, Double.valueOf(oldValue), Double.valueOf(newValue));
  }


}
