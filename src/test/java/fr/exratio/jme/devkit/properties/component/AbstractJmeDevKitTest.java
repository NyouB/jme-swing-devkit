package fr.exratio.jme.devkit.properties.component;

import fr.exratio.jme.devkit.service.JmeEngineService;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.impl.JmeEngineServiceImpl;

public class AbstractJmeDevKitTest {

  @org.junit.jupiter.api.BeforeAll
  static void beforeAll() {
    JmeEngineService engineService = ServiceManager.registerService(JmeEngineServiceImpl.class);
    ServiceManager.registerService(RegistrationService.class);

  }

}
