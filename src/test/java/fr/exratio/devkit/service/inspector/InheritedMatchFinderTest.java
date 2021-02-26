package fr.exratio.devkit.service.inspector;

import com.jme3.scene.Geometry;
import com.jme3.ui.Picture;
import fr.exratio.devkit.properties.PropertySection;
import fr.exratio.devkit.properties.component.AbstractJmeDevKitTest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InheritedMatchFinderTest extends AbstractJmeDevKitTest {

  @Test
  void find() {
    Picture picture = new Picture("myTestGeometry");
    InheritedMatchFinder finder = new InheritedMatchFinder();
    List<PropertySection> res = finder.find(picture);
    Assertions.assertEquals(3, res.size());
    Assertions.assertTrue(res.stream().map(propertySection -> propertySection.getTitle()).collect(
        Collectors.toList()).containsAll(Arrays.asList("Geometry", "Spatial", "Transform")));
  }

  @Test
  void findParentClassBuilder() {
    Picture picture = new Picture("myTestPicture");
    InheritedMatchFinder finder = new InheritedMatchFinder();
    Class<?> registeredParentClass = finder
        .findRegisteredParentClass(picture);
    Assertions.assertEquals(Geometry.class, registeredParentClass);
  }
}