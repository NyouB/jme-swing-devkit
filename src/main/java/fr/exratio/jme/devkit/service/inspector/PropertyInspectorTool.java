/*
 * Created by JFormDesigner on Sun Mar 21 15:32:01 CET 2021
 */

package fr.exratio.jme.devkit.service.inspector;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import fr.exratio.jme.devkit.event.SelectedItemEvent;
import fr.exratio.jme.devkit.main.MainPage.Zone;
import fr.exratio.jme.devkit.properties.PropertySection;
import fr.exratio.jme.devkit.service.RegistrationService;
import fr.exratio.jme.devkit.tool.Tool;
import fr.exratio.jme.devkit.tool.ViewMode;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author Quentin Raphaneau
 */
public class PropertyInspectorTool extends Tool {

  private static final Logger LOGGER = LoggerFactory.getLogger(PropertyInspectorTool.class);
  private static final String TITLE = "Property";
  private static final String NOTHING_LABEL = "Nothing Selected";

  private final JLabel nothingSelectedLabel = new JLabel(NOTHING_LABEL);
  private List<PropertySection> displayedSections;
  private PropertySectionListFinder propertySectionListBuilder;
  private final RegistrationService registrationService;
  private final EventBus eventBus;
  private final ExactMatchFinder exactMatchFinder;
  private final InheritedMatchFinder inheritedMatchFinder;
  private final DefaultMatchFinder defaultMatchFinder;

  public PropertyInspectorTool(
      RegistrationService registrationService, EventBus eventBus,
      ExactMatchFinder exactMatchFinder,
      InheritedMatchFinder inheritedMatchFinder,
      DefaultMatchFinder defaultMatchFinder) {
    super(PropertyInspectorTool.class.getName(), TITLE, null, Zone.RIGHT_TOP, ViewMode.PIN, true);
    this.registrationService = registrationService;
    this.eventBus = eventBus;
    this.exactMatchFinder = exactMatchFinder;
    this.inheritedMatchFinder = inheritedMatchFinder;
    this.defaultMatchFinder = defaultMatchFinder;
    initComponents();
    initialize();
  }

  private void initialize() {
    clearInspector();
    propertySectionListBuilder = exactMatchFinder;
    propertySectionListBuilder.chainWith(inheritedMatchFinder)
        .chainWith(defaultMatchFinder);
    eventBus.register(this);
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    scrollPane = new JScrollPane();
    contentPane = new JPanel();

    //======== this ========
    var contentPane2 = getContentPane();
    contentPane2.setLayout(new BorderLayout());

    //======== scrollPane ========
    {

      //======== contentPane ========
      {
        contentPane.setLayout(new MigLayout(
            null,
            // columns
            "[]" +
                "[grow, fill]",
            // rows
            null));
      }
      scrollPane.setViewportView(contentPane);
    }
    contentPane2.add(scrollPane, BorderLayout.CENTER);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  /**
   * Removes all inspection properties of the object being inspected.
   */
  public void clearInspector() {
    contentPane.removeAll();
    contentPane.add(nothingSelectedLabel);
    contentPane.repaint();
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
      contentPane.remove(nothingSelectedLabel);
      for (PropertySection section : propertySections) {
        addSection(section);
      }

    } else {
      clearInspector();
    }

    revalidate();
    repaint();
  }

  private void addSection(PropertySection section) {
    JLabel sectionTitle = new JLabel(section.getTitle());
    sectionTitle.setIcon(section.getIcon());
    sectionTitle.setBackground(new Color(90, 90, 90));
    sectionTitle.setOpaque(true);
    contentPane.add(sectionTitle, "growx, span 2, wrap");
    for (Entry<String, Component> entry : section.getComponents().entrySet()) {
      contentPane.add(new JLabel(entry.getKey()));
      contentPane.add(entry.getValue(), "growx, wrap");
      contentPane.add(new JSeparator(SwingConstants.HORIZONTAL), "growx, span 2, wrap");
    }
  }

  @Subscribe
  public void onItemSelected(SelectedItemEvent event) {
    Object selectedItem = event.getSelectedItem();
    if (selectedItem == null) {
      LOGGER.warn(
          "-- onItemSelected() the object contained in the event is null. Doing nothing");
      return;
    }
    inspect(selectedItem);
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
    contentPane.removeAll();
    if (displayedSections != null) {
      displayedSections = null;
    }
  }

  public PropertySectionListFinder getPropertySectionListBuilder() {
    return propertySectionListBuilder;
  }

  public void setPropertySectionListBuilder(
      PropertySectionListFinder propertySectionListBuilder) {
    this.propertySectionListBuilder = propertySectionListBuilder;
  }


  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private JScrollPane scrollPane;
  private JPanel contentPane;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
