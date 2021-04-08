/*
 * Created by JFormDesigner on Tue Mar 16 19:00:56 CET 2021
 */

package fr.exratio.jme.devkit.tool;

import com.jformdesigner.annotations.BeanInfo;
import fr.exratio.jme.devkit.main.MainPage;
import fr.exratio.jme.devkit.main.MainPage.Zone;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
import java.awt.BorderLayout;
import javax.swing.Icon;
import javax.swing.JPanel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

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
  protected boolean registered = false;
  @Autowired
  protected ToolLocationService toolLocationService;

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

  public void setZone(Zone newZone) {
    //to prevent the tool to be docked if the zone is changed while the voiewmode is window
    if (viewMode == ViewMode.WINDOW) {
      this.zone = newZone;
      return;
    }
    if (zone == newZone || viewMode == ViewMode.WINDOW) {
      return;
    }
    Zone oldZone = zone;
    this.zone = newZone;
    oldZone.remove(this);
    newZone.add(this);
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
    boolean oldValue = this.isDisplayed;
    setVisible(displayed);
    firePropertyChange("isDisplayed", oldValue, displayed);
  }

  public void display(boolean b) {

  }

}
