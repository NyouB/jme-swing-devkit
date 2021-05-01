package fr.exratio.jme.devkit.service.inspector;

import com.jme3.scene.Geometry;
import com.jme3.ui.Picture;
import fr.exratio.jme.devkit.SpringTestConfiguration;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.service.EditorJmeApplication;
import fr.exratio.jme.devkit.service.RegistrationService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(SpringTestConfiguration.class)
class InheritedMatchFinderTest {

  @Autowired
  private EditorJmeApplication editorJmeApplication;

  @Autowired
  private RegistrationService registrationService;

  @Autowired
  private InheritedMatchFinder inheritedMatchFinder;

  @Test
  void find() {
    Picture picture = new Picture("myTestGeometry");
    List<PropertySection> res = inheritedMatchFinder.find(picture);
    Assertions.assertEquals(3, res.size());
    Assertions.assertTrue(res.stream().map(propertySection -> propertySection.getTitle()).collect(
        Collectors.toList()).containsAll(Arrays.asList("Geometry", "Spatial", "Transform")));
  }

  @Test
  void findParentClassBuilder() {
    Picture picture = new Picture("myTestPicture");
    Class<?> registeredParentClass = inheritedMatchFinder
        .findRegisteredParentClass(picture);
    Assertions.assertEquals(Geometry.class, registeredParentClass);
  }
}