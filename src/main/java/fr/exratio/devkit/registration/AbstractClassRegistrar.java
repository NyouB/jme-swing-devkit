package fr.exratio.devkit.registration;

public abstract class AbstractClassRegistrar<T> implements ClassRegistrar<T> {

  private Class<? extends T> registeredClass;

  public AbstractClassRegistrar() {

  }

  @Override
  public Class<? extends T> getRegisteredClass() {
    return registeredClass;
  }

  @Override
  public void setRegisteredClass(Class<? extends T> classToRegister) {
    this.registeredClass = classToRegister;
  }

}
