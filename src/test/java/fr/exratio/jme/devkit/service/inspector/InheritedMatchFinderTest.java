package fr.exratio.jme.devkit.service.inspector;

import com.jme3.scene.Geometry;
import com.jme3.ui.Picture;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.properties.component.AbstractJmeDevKitTest;
import fr.exratio.jme.devkit.service.RegistrationService;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InheritedMatchFinderTest extends AbstractJmeDevKitTest {

  @Test
  void find() {
    Picture picture = new Picture("myTestGeometry");
    InheritedMatchFinder finder = new InheritedMatchFinder(new RegistrationService());
    List<PropertySection> res = finder.find(picture);
    Assertions.assertEquals(3, res.size());
    Assertions.assertTrue(res.stream().map(propertySection -> propertySection.getTitle()).collect(
        Collectors.toList()).containsAll(Arrays.asList("Geometry", "Spatial", "Transform")));
  }

  @Test
  void findParentClassBuilder() {
    Picture picture = new Picture("myTestPicture");
    InheritedMatchFinder finder = new InheritedMatchFinder(new RegistrationService());
    Class<?> registeredParentClass = finder
        .findRegisteredParentClass(picture);
    Assertions.assertEquals(Geometry.class, registeredParentClass);
  }
}