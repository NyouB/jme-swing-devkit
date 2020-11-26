package com.jayfella.devkit.properties.component;

import java.beans.PropertyChangeListener;
import java.beans.PropertyEditorSupport;

public abstract class CustomPropertyEditorSupport<T> extends PropertyEditorSupport {

  Object oldValue;
  protected PropertyChangeListener propertyChangeListener = evt -> {
    if ((evt.getOldValue() != null && evt.getOldValue().equals(evt.getNewValue()))) {
      return;
    }
    oldValue = super.getValue();
    Object newValue = computeValue();
    if (!oldValue.equals(newValue)) {
      setValue(newValue);
      firePropertyChange();
    }
  };

  public CustomPropertyEditorSupport() {
    setSource(this);
  }


  public CustomPropertyEditorSupport(T source) {
    if (source == null) {
      throw new NullPointerException();
    }
    setSource(source);
  }

  protected abstract Object computeValue();

  @Override
  public Object getValue() {
    Object newValue = computeValue();
    if (newValue.equals(super.getValue())) {
      return super.getValue();
    }
    setValue(newValue);
    return newValue;
  }

  @Override
  public void setValue(Object value) {
    if (super.getValue().equals(value)) {
      return;
    }
    oldValue = super.getValue();
    super.setValue(value);
  }


}
