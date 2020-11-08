package com.jayfella.devkit.service;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.builder.AbstractPropertySectionBuilder;
import com.jayfella.devkit.properties.builder.PropertySectionBuilderFactory;
import com.jayfella.devkit.properties.builder.ReflectedComponentSetBuilder;
import com.jayfella.devkit.properties.builder.SpatialPropertySectionBuilder;
import com.jayfella.devkit.properties.component.AbstractSDKComponent;
import com.jayfella.devkit.properties.component.SDKComponentFactory;
import com.jayfella.devkit.properties.component.SdkComponent;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.VerticalLayout;

public class PropertyInspectorService implements Service {

  public static final String WINDOW_ID = "Property Inspector";

  private final JPanel rootPanel;
  private final JTabbedPane tabbedPane = new JTabbedPane();

  private final JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");
  private final long threadId;
  private List<PropertySection> displayedSections;

  public PropertyInspectorService(JPanel rootPanel) {
    threadId = Thread.currentThread().getId();

    this.rootPanel = rootPanel;
    clearInspector();
  }

  /**
   * Removes all inspection properties of the object being inspected.
   */
  public void clearInspector() {
    rootPanel.remove(tabbedPane);
    rootPanel.add(nothingSelectedLabel, BorderLayout.CENTER);
    rootPanel.repaint();
  }

  /**
   * Displays all attributes for a given object. This method **must** be called from the AWT
   * thread.
   *
   * @param propertySections The list of property sections to display.
   */
  private void displaySections(List<PropertySection> propertySections) {

    displayedSections = propertySections;

    if (propertySections != null) {
      rootPanel.remove(nothingSelectedLabel);
      rootPanel.add(tabbedPane);
      tabbedPane.removeAll();

      for (PropertySection section : propertySections) {
        addTab(section);
      }

    } else {
      clearInspector();
    }

    rootPanel.revalidate();
    rootPanel.repaint();
  }

  private void addTab(PropertySection section) {
    addTab(section, tabbedPane.getTabCount());
  }

  private void addTab(PropertySection section, int index) {
    JPanel panel = new JPanel(new VerticalLayout(5));
    panel.setBorder(new EmptyBorder(10, 0, 0, 0));

    for (SdkComponent component : section.getComponents()) {
      panel.add(component.getJComponent());
    }
    tabbedPane.insertTab(section.getTitle(), section.getIcon(), panel, null, index);
  }

  /**
   * Inspects a given object. This method **must** be called from the AWT thread.
   *
   * @param object the object to inspect.
   */
  public void inspect(final Object object) {

    cleanup();

    // check controls
    // check componentBuilders

    boolean found = false;

    if (object instanceof Control) {

      Control control = (Control) object;

      SDKComponentFactory factory = ServiceManager
          .getService(RegistrationService.class)
          .getControlComponentFactoryFor(control.getClass());
      if (factory != null) {
        AbstractSDKComponent component = factory.create(object, null);
        PropertySection propertySection = new PropertySection(control.getClass().getSimpleName(),
            component);
        List<PropertySection> propertySections = new ArrayList<>();
        propertySections.add(propertySection);
        displaySections(propertySections);

        found = true;
      }

    } else {

      PropertySectionBuilderFactory factory = ServiceManager
          .getService(RegistrationService.class)
          .getPropertySectionBuilderFactoryFor(object.getClass());

      if (factory != null) {
        AbstractPropertySectionBuilder<?> propertySectionBuilder = factory.create(object);
        List<PropertySection> sections = propertySectionBuilder.build();
        displaySections(sections);

        found = true;
      }

    }

    if (!found) {

      // we display the spatial component builder last to give everything else a chance to find something.
      // Our last effort is to display the "generic" spatial componentBuilder.
      // This is actually most likely the case for 90% of spatial(s), but for things like ParticleEmitters
      // they need the opportunity to find their own custom builder.
      if (object instanceof Spatial) {

        Spatial spatial = (Spatial) object;
        SpatialPropertySectionBuilder componentSetBuilder = new SpatialPropertySectionBuilder(
            spatial);

        List<PropertySection> propertySections = componentSetBuilder.build();
        displaySections(propertySections);

      } else {

        // we don't know what it is, so all we can do is display reflected properties.

        ReflectedComponentSetBuilder componentSetBuilder = new ReflectedComponentSetBuilder(
            object.getClass().getSimpleName(), object);

        List<PropertySection> propertySections = componentSetBuilder.build();
        displaySections(propertySections);

      }

    }

  }

  /**
   * Updates all given sections with a matching name.
   *
   * @param updatedSections the sections you wish to update.
   */
  public void updateSections(Collection<PropertySection> updatedSections) {

    for (PropertySection updatedSection : updatedSections) {

      int index = -1;

      for (int i = 0; i < displayedSections.size(); i++) {

        PropertySection currentSection = displayedSections.get(i);
        if (currentSection.getTitle().equals(updatedSection.getTitle())) {
          index = i;
          break;
        }
      }

      if (index > -1) {
        displayedSections.set(index, updatedSection);
        tabbedPane.remove(index);
        addTab(updatedSection, index);
      }

    }

  }

  private void cleanup() {

    if (displayedSections != null) {

      for (PropertySection section : displayedSections) {

        SdkComponent[] components = section.getComponents();

        if (components != null) {
          for (SdkComponent component : components) {
            component.cleanup();
          }
        }
      }

      displayedSections = null;
    }

  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {
    throw new UnsupportedOperationException("This method shouldn't be called");
  }

}
