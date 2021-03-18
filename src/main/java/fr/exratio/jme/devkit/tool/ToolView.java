/*
 * Created by JFormDesigner on Tue Mar 16 19:00:56 CET 2021
 */

package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.forms.ComponentTopMenu;
import fr.exratio.jme.devkit.forms.MainPage.Zone;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JPanel;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Quentin Raphaneau
 */
@Getter
@Setter
public abstract class ToolView extends JPanel {

  private String id = ToolView.class.getName();
  private String title;
  private Icon icon;
  private Component content;
  private Zone zone = Zone.LEFT_TOP;
  private ViewMode viewMode = new Pin();
  private boolean isDisplayed = true;

  public ToolView(String id) {
    this.id = id;
    initComponents();
  }

  public ToolView(String id, String title) {
    this(id);
    setTitle(title);
  }

  public ToolView(String id, String title, Icon icon) {
    this(id, title);
    setIcon(icon);
  }

  public ToolView(String id, String title, Icon icon, ViewMode viewMode) {
    this(id, title);
    setIcon(icon);
  }

  private void createUIComponents() {
    componentTopMenu1 = new ComponentTopMenu(this);

  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    createUIComponents();

    //======== this ========
    setBorder(new javax.swing.border.CompoundBorder(
        new javax.swing.border.TitledBorder(new javax.swing.border
            .EmptyBorder(0, 0, 0, 0), "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",
            javax.swing.border.TitledBorder.CENTER, javax
            .swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dia\u006cog", java.awt.Font.BOLD,
            12), java.awt.Color.red), getBorder()));
    addPropertyChangeListener(new java.beans
        .PropertyChangeListener() {
      @Override
      public void propertyChange(java.beans.PropertyChangeEvent e) {
        if ("\u0062ord\u0065r".equals(e.
            getPropertyName())) {
          throw new RuntimeException();
        }
      }
    });
    setLayout(new BorderLayout());
    add(componentTopMenu1, BorderLayout.NORTH);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private ComponentTopMenu componentTopMenu1;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  public void setContent(Component newContent) {
    this.content = newContent;
    add(content, BorderLayout.CENTER);
  }

  public void changeViewMode(ViewMode newViewMode) {
    this.viewMode = newViewMode;
    viewMode.changeView(this);
  }

  public void setTitle(String title) {
    this.title = title;
    componentTopMenu1.getTitleLabel().setText(title);
  }

  public void setIcon(Icon icon) {
    this.icon = icon;
    componentTopMenu1.getTitleLabel().setIcon(icon);
  }

  public void setZone(Zone zone) {
    this.zone = zone;

  }

  public Zone getZone() {
    return zone;
  }

  public ViewMode getViewMode() {
    return viewMode;
  }

  public void setViewMode(ViewMode viewMode) {
    this.viewMode = viewMode;
  }

  public boolean isDisplayed() {
    return isDisplayed;
  }

  public void setDisplayed(boolean displayed) {
    isDisplayed = displayed;
    if (isDisplayed) {
      viewMode.changeView(this);
    } else {
      ServiceManager.getService(ToolLocationService.class).hide(this);
    }
  }

  public void display(boolean b) {

  }
}
