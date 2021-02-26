package fr.exratio.devkit.properties.component;

import fr.exratio.devkit.service.JmeEngineService;
import fr.exratio.devkit.service.RegistrationService;
import fr.exratio.devkit.service.ServiceManager;
import fr.exratio.devkit.service.impl.JmeEngineServiceImpl;

public class AbstractJmeDevKitTest {

  @org.junit.jupiter.api.BeforeAll
  static void beforeAll() {
    JmeEngineService engineService = ServiceManager.registerService(JmeEngineServiceImpl.class);
    ServiceManager.registerService(RegistrationService.class);

  }

}
