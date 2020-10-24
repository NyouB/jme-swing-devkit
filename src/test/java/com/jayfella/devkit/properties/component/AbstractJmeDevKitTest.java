package com.jayfella.devkit.properties.component;

import com.jayfella.devkit.service.JmeEngineService;
import com.jayfella.devkit.service.ServiceManager;
import com.jayfella.devkit.service.impl.JmeEngineServiceImpl;

public class AbstractJmeDevKitTest {

  @org.junit.jupiter.api.BeforeAll
  static void beforeAll() {
    JmeEngineService engineService = ServiceManager.registerService(JmeEngineServiceImpl.class);

  }

}
