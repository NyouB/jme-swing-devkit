package fr.exratio.jme.devkit;

public interface InitialisationTemplate {

  void engineLoading();

  void GUILoading();

  void serviceLoading();

  void toolLoading();

  void appStateLoading();

  default void initialize() {
    engineLoading();
    GUILoading();
    serviceLoading();
    toolLoading();
    appStateLoading();
  }

}
