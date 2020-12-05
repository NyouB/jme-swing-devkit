package com.jayfella.devkit.service.inspector;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.jayfella.devkit.properties.PropertySection;
import com.jayfella.devkit.service.Service;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
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
  private PropertySectionListFinder propertySectionListBuilder;

  public PropertyInspectorService(JPanel rootPanel) {
    threadId = Thread.currentThread().getId();
    this.rootPanel = rootPanel;
    sectionPanel = new JPanel(new VerticalLayout());
    rootPanel.add(sectionPanel);
    sectionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
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
    sectionPanel.setLayout(
        new GridLayoutManager(section.getComponents().entrySet().size(), 2, new Insets(0, 0, 0, 0),
            -1, -1));
    int i = 0;
    for (Entry<String, Component> entry : section.getComponents().entrySet()) {
      sectionPanel
          .add(new JLabel(entry.getKey()), new GridConstraints(i, 0, 1, 2, GridConstraints.ANCHOR_CENTER,
              GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null,
              null, 0,
              false));
      sectionPanel
          .add(entry.getValue(), new GridConstraints(i, 1, 1, 2, GridConstraints.ANCHOR_CENTER,
              GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null,
              null, 0,
              false));
      i++;
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
        .find(object,
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

  public PropertySectionListFinder getPropertySectionListBuilder() {
    return propertySectionListBuilder;
  }

  public void setPropertySectionListBuilder(
      PropertySectionListFinder propertySectionListBuilder) {
    this.propertySectionListBuilder = propertySectionListBuilder;
  }
}
