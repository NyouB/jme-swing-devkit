package com.jayfella.devkit.service.inspector;

import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.service.Service;
import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertyInspectorService implements Service {

  public static final String WINDOW_ID = "Property Inspector";
  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyInspectorService.class);
  private final JPanel sectionPanel;

  private final JLabel nothingSelectedLabel = new JLabel("Nothing To Inspect.");
  private final long threadId;
  private List<PropertySection> displayedSections;
  private PropertySectionListFinder propertySectionListBuilder;

  public PropertyInspectorService(
  ) {
    threadId = Thread.currentThread().getId();
    sectionPanel = new JPanel(new MigLayout("", "[][grow, fill]"));
    sectionPanel.setBorder(new EmptyBorder(10, 4, 0, 0));
    clearInspector();
    propertySectionListBuilder = new ExactMatchFinder();
    propertySectionListBuilder.chainWith(new InheritedMatchFinder())
        .chainWith(new DefaultMatchFinder());

  }

  /**
   * Removes all inspection properties of the object being inspected.
   */
  public void clearInspector() {
    sectionPanel.removeAll();
    sectionPanel.add(nothingSelectedLabel, "growx");
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
    sectionPanel.add(sectionTitle, "growx, span 2, wrap");
    for (Entry<String, Component> entry : section.getComponents().entrySet()) {
      sectionPanel.add(new JLabel(entry.getKey()));
      sectionPanel.add(entry.getValue(), "growx, wrap");
      sectionPanel.add(new JSeparator(SwingConstants.HORIZONTAL), "growx, span 2, wrap");
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
        .find(object);
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
      displayedSections = null;
    }

  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {
    //throw new UnsupportedOperationException("This method shouldn't be called");
  }

  public PropertySectionListFinder getPropertySectionListBuilder() {
    return propertySectionListBuilder;
  }

  public void setPropertySectionListBuilder(
      PropertySectionListFinder propertySectionListBuilder) {
    this.propertySectionListBuilder = propertySectionListBuilder;
  }

  public JPanel getSectionPanel() {
    return sectionPanel;
  }
}
