/*
 * Created by JFormDesigner on Tue Mar 16 19:00:56 CET 2021
 */

package fr.exratio.jme.devkit.tool;

import com.jformdesigner.annotations.BeanInfo;
import fr.exratio.jme.devkit.forms.MainPage;
import fr.exratio.jme.devkit.forms.MainPage.Zone;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Quentin Raphaneau
 */
@Getter
@Setter
@BeanInfo(isContainer = true, containerDelegate = "getContentPane")
public class Tool extends JPanel {

  protected String id = this.getClass().getName();
  protected String title;
  protected Icon icon;
  protected Zone zone = Zone.LEFT_TOP;
  protected ViewMode viewMode = ViewMode.PIN;
  protected boolean isDisplayed = true;

  public Tool() {
    initComponents();
  }

  public Tool(ViewMode viewMode) {
    this.viewMode = viewMode;
    initComponents();
  }

  public Tool(Zone zone) {
    this.zone = zone;
    initComponents();
  }

  public Tool(ViewMode viewMode, Zone zone) {
    this.viewMode = viewMode;
    this.zone = zone;
    initComponents();
  }

  @Builder(builderMethodName = "toolBuilder")
  public Tool(String id, String title, Icon icon, Zone zone,
      ViewMode viewMode, boolean isDisplayed) {
    this.id = id;
    this.title = title;
    this.icon = icon;
    this.zone = zone;
    this.viewMode = viewMode;
    this.isDisplayed = isDisplayed;
    initComponents();
  }

  private void createUIComponents() {
    menuComponent = new ToolTopMenu(this);
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    createUIComponents();

    contentPane = new JPanel();

    //======== this ========
    setBorder(
        new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing
            .border.EmptyBorder(0, 0, 0, 0),
            "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn",
            javax.swing.border.TitledBorder
                .CENTER, javax.swing.border.TitledBorder.BOTTOM,
            new java.awt.Font("Dia\u006cog", java.
                awt.Font.BOLD, 12), java.awt.Color.red), getBorder()))
    ;
    setLayout(new BorderLayout());
    add(menuComponent, BorderLayout.NORTH);

    //======== contentPane ========
    {
      contentPane.setLayout(new BorderLayout());
    }
    add(contentPane, BorderLayout.CENTER);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private ToolTopMenu menuComponent;
  private JPanel contentPane;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  public void setTitle(String title) {
    this.title = title;
    menuComponent.getTitleLabel().setText(title);
  }

  public void setIcon(Icon icon) {
    this.icon = icon;
    menuComponent.getTitleLabel().setIcon(icon);
  }

  public void setZone(Zone zone) {
    ServiceManager.getService(ToolLocationService.class).moveZone(this, zone);
    this.zone = zone;
    menuComponent.onZoneChange();
  }

  public MainPage.Zone getZone() {
    return zone;
  }

  public ViewMode getViewMode() {
    return viewMode;
  }

  public void setViewMode(ViewMode newViewMode) {
    newViewMode.changeView(this);
    this.viewMode = newViewMode;
    menuComponent.onViewModeChange();
  }

  public boolean isDisplayed() {
    return isDisplayed;
  }

  public void setDisplayed(boolean displayed) {
    setVisible(displayed);
    ServiceManager.getService(ToolLocationService.class).getMainPage().revalidate();
    ServiceManager.getService(ToolLocationService.class).getMainPage().repaint();
  }

  public void display(boolean b) {

  }

}
