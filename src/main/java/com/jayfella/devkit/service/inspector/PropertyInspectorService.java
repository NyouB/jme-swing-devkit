package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.properties.component.SdkComponent;
import com.jayfella.devkit.service.Service;
import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyInspectorService implements Service {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyInspectorService.class);

  public static final String WINDOW_ID = "Property Inspector";

  private final JPanel rootPanel;
  private final JPanel sectionPanel;

  private final JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");
  private final long threadId;
  private List<PropertySection> displayedSections;
  private PropertySectionListBuilder propertySectionListBuilder;

  public PropertyInspectorService(JPanel rootPanel) {
    threadId = Thread.currentThread().getId();
    this.rootPanel = rootPanel;
    sectionPanel = new JPanel(new VerticalLayout());
    rootPanel.add(sectionPanel);
    sectionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
    clearInspector();
    propertySectionListBuilder = new ControlFinder();
    propertySectionListBuilder.chainWith(new ExactMatchFinder())
        .chainWith(new InheritedMatchFinder())
        .chainWith(new DefaultMatchFinder());

  }

  /**
   * Removes all inspection properties of the object being inspected.
   */
  public void clearInspector() {
    sectionPanel.removeAll();
    sectionPanel.add(nothingSelectedLabel, BorderLayout.CENTER);
    sectionPanel.repaint();
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
      sectionPanel.remove(nothingSelectedLabel);

      for (PropertySection section : propertySections) {
        addSection(section);
      }

    } else {
      clearInspector();
    }

    sectionPanel.revalidate();
    sectionPanel.repaint();
  }

  private void addSection(PropertySection section) {
    JLabel sectionTitle = new JLabel(section.getTitle());
    sectionTitle.setIcon(section.getIcon());
    sectionTitle.setBackground(new Color(90, 90, 90));
    sectionTitle.setOpaque(true);
    sectionPanel.add(sectionTitle);
    for (SdkComponent component : section.getComponents()) {
      sectionPanel.add(component.getJComponent());
    }
  }

  /**
   * Inspects a given object. This method **must** be called from the AWT thread.
   *
   * @param object the object to inspect.
   */
  public void inspect(final Object object) {
    cleanup();
    List<PropertySection> propertySectionList = propertySectionListBuilder
        .find(object.getClass(), object,
            object.getClass().toString());
    displaySections(propertySectionList);
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
        addSection(updatedSection);
      }

    }

  }

  private void cleanup() {
    sectionPanel.removeAll();
    if (displayedSections != null) {

      for (PropertySection section : displayedSections) {

        List<SdkComponent> components = section.getComponents();

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

  public PropertySectionListBuilder getPropertySectionListBuilder() {
    return propertySectionListBuilder;
  }

  public void setPropertySectionListBuilder(
      PropertySectionListBuilder propertySectionListBuilder) {
    this.propertySectionListBuilder = propertySectionListBuilder;
  }
}
