package fr.exratio.jme.devkit.properties.component;

import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.impl.EditorJmeApplicationImpl;

public class AbstractJmeDevKitTest {

  @org.junit.jupiter.api.BeforeAll
  static void beforeAll() {
    EditorJmeApplication engineService = ServiceManager.registerService(EditorJmeApplicationImpl.class);
    ServiceManager.registerService(RegistrationService.class);

  }

}
