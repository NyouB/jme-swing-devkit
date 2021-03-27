/*
 * Created by JFormDesigner on Sat Mar 20 12:03:12 CET 2021
 */

package fr.exratio.jme.devkit.forms;

import fr.exratio.jme.devkit.ZonedToolBar;
import fr.exratio.jme.devkit.ZonedToolBar.ToolBarZone;
import fr.exratio.jme.devkit.swing.JSplitPaneWithZeroSizeDivider;
import fr.exratio.jme.devkit.tool.Tool;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Quentin Raphaneau
 */
@Getter
@Setter
public class MainPage2 extends JPanel {

  public static String WINDOW_ID = "main";

  public MainPage2() {
    initComponents();
    Zone.LEFT_TOP.setConcreteZone(
        new ConcreteZone(Zone.LEFT_TOP, leftToolBar.getSecondZone(), leftVerticalFinalSplitPane,
            JSplitPane.TOP));
    Zone.LEFT_BOTTOM.setConcreteZone(
        new ConcreteZone(Zone.LEFT_BOTTOM, leftToolBar.getFirstZone(), leftVerticalFinalSplitPane,
            JSplitPane.BOTTOM));
    Zone.RIGHT_TOP.setConcreteZone(
        new ConcreteZone(Zone.RIGHT_TOP, rightToolBar.getSecondZone(), rightVerticalFinalSplitPane,
            JSplitPane.TOP));
    Zone.RIGHT_BOTTOM.setConcreteZone(
        new ConcreteZone(Zone.RIGHT_BOTTOM, rightToolBar.getFirstZone(), leftVerticalFinalSplitPane,
            JSplitPane.BOTTOM));
    Zone.BOTTOM_LEFT.setConcreteZone(
        new ConcreteZone(Zone.BOTTOM_LEFT, bottomToolBar.getFirstZone(), bottomFinalSplitPane,
            JSplitPane.LEFT));
    Zone.BOTTOM_RIGHT.setConcreteZone(
        new ConcreteZone(Zone.BOTTOM_RIGHT, bottomToolBar.getSecondZone(), bottomFinalSplitPane,
            JSplitPane.RIGHT));
  }

  private void createUIComponents() {
    centerPanel = new JPanel();
    centerPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        SwingUtilities.getRoot(e.getComponent()).revalidate();
      }
    });

    leftToolBar = new ZonedToolBar();
    rightToolBar = new ZonedToolBar();
    bottomToolBar = new ZonedToolBar();
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    createUIComponents();

    verticalMainSplitPane = new JSplitPaneWithZeroSizeDivider();
    horizontalLeftSplitPane = new JSplitPaneWithZeroSizeDivider();
    leftVerticalFinalSplitPane = new JSplitPaneWithZeroSizeDivider();
    horizontalRightSplitPane = new JSplitPaneWithZeroSizeDivider();
    rightVerticalFinalSplitPane = new JSplitPaneWithZeroSizeDivider();
    centerTabbedPane = new JTabbedPane();
    bottomFinalSplitPane = new JSplitPaneWithZeroSizeDivider();

    //======== this ========
    setBorder (new javax. swing. border. CompoundBorder( new javax .swing .border .TitledBorder (new
    javax. swing. border. EmptyBorder( 0, 0, 0, 0) , "JFor\u006dDesi\u0067ner \u0045valu\u0061tion", javax
    . swing. border. TitledBorder. CENTER, javax. swing. border. TitledBorder. BOTTOM, new java
    .awt .Font ("Dia\u006cog" ,java .awt .Font .BOLD ,12 ), java. awt
    . Color. red) , getBorder( )) );  addPropertyChangeListener (new java. beans.
    PropertyChangeListener( ){ @Override public void propertyChange (java .beans .PropertyChangeEvent e) {if ("bord\u0065r" .
    equals (e .getPropertyName () )) throw new RuntimeException( ); }} );
    setLayout(new BorderLayout());

    //======== verticalMainSplitPane ========
    {
      verticalMainSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

      //======== horizontalLeftSplitPane ========
      {

        //======== leftVerticalFinalSplitPane ========
        {
          leftVerticalFinalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        }
        horizontalLeftSplitPane.setLeftComponent(leftVerticalFinalSplitPane);

        //======== horizontalRightSplitPane ========
        {

          //======== rightVerticalFinalSplitPane ========
          {
            rightVerticalFinalSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
            rightVerticalFinalSplitPane.setMinimumSize(new Dimension(57, 49));
          }
          horizontalRightSplitPane.setRightComponent(rightVerticalFinalSplitPane);

          //======== centerPanel ========
          {
            centerPanel.setLayout(new BorderLayout());
            centerPanel.add(centerTabbedPane, BorderLayout.CENTER);
          }
          horizontalRightSplitPane.setLeftComponent(centerPanel);
        }
        horizontalLeftSplitPane.setRightComponent(horizontalRightSplitPane);
      }
      verticalMainSplitPane.setTopComponent(horizontalLeftSplitPane);
      verticalMainSplitPane.setBottomComponent(bottomFinalSplitPane);
    }
    add(verticalMainSplitPane, BorderLayout.CENTER);

    //======== leftToolBar ========
    {
      leftToolBar.setOrientation(SwingConstants.VERTICAL);
      leftToolBar.setSide(ZonedToolBar.Side.LEFT);
    }
    add(leftToolBar, BorderLayout.WEST);
    add(bottomToolBar, BorderLayout.SOUTH);

    //======== rightToolBar ========
    {
      rightToolBar.setOrientation(SwingConstants.VERTICAL);
      rightToolBar.setSide(ZonedToolBar.Side.RIGHT);
    }
    add(rightToolBar, BorderLayout.EAST);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private JSplitPaneWithZeroSizeDivider verticalMainSplitPane;
  private JSplitPaneWithZeroSizeDivider horizontalLeftSplitPane;
  private JSplitPaneWithZeroSizeDivider leftVerticalFinalSplitPane;
  private JSplitPaneWithZeroSizeDivider horizontalRightSplitPane;
  private JSplitPaneWithZeroSizeDivider rightVerticalFinalSplitPane;
  private JPanel centerPanel;
  private JTabbedPane centerTabbedPane;
  private JSplitPaneWithZeroSizeDivider bottomFinalSplitPane;
  private ZonedToolBar leftToolBar;
  private ZonedToolBar bottomToolBar;
  private ZonedToolBar rightToolBar;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  public void addTabCenterPanel(String title, Component jmePanel, ImageIcon icon) {
    centerTabbedPane.addTab(title, icon, jmePanel);
  }

  interface ToolContainer{
    void add(Tool tool);

    void select(Tool tool);

    void remove(Tool tool);
  }

  public enum Zone implements ToolContainer{
    LEFT_TOP, LEFT_BOTTOM, RIGHT_TOP, RIGHT_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER;

    private ConcreteZone concreteZone;

    @Override
    public void add(Tool tool) {
      concreteZone.add(tool);
    }

    @Override
    public void select(Tool tool) {
      concreteZone.select(tool);
    }

    @Override
    public void remove(Tool tool) {
      concreteZone.remove(tool);
    }

    public void setConcreteZone(ConcreteZone concreteZone) {
      //poor final design
      if( this.concreteZone == null){
        this.concreteZone = concreteZone;
      }
    }
  }

  class ConcreteZone implements ToolContainer {

    private final ToolBarZone toolBarZone;
    private final JSplitPane contentPane;
    private final String splitPaneSide;
    private final List<Tool> attachedTools = new LinkedList<>();
    private Tool displayedTool;
    private final Zone zone;

    public ConcreteZone(Zone zone, ToolBarZone toolBarZone, JSplitPane jSplitPane, String side) {
      this.toolBarZone = toolBarZone;
      this.zone = zone;
      toolBarZone.setZone(zone);
      this.contentPane = jSplitPane;
      this.splitPaneSide = side;
    }

    @Override
    public void add(Tool tool) {
      if (displayedTool != null && displayedTool.equals(tool)) {
        return;
      }

      if(!attachedTools.contains(tool)){
        attachedTools.add(tool);
        toolBarZone.addTool(tool);
      }

      select(tool);
    }

    @Override
    public void select(Tool tool){
      if(!attachedTools.contains(tool)){
        attachedTools.add(tool);
        toolBarZone.addTool(tool);
      }
      SwingUtilities.invokeLater(() -> contentPane.add(tool, splitPaneSide));

      displayedTool = tool;
    }

    @Override
    public void remove(Tool tool) {
      attachedTools.remove(tool);
      toolBarZone.removeTool(tool);
      SwingUtilities.invokeLater(() -> contentPane.remove(tool));
      if(tool.equals(displayedTool)){
        displayedTool = null;
      }
      selectNextToolInList();
    }

    private void selectNextToolInList() {
      if(0 < attachedTools.size()) {
        select(attachedTools.get(0));
      }
    }

    public Tool getDisplayedTool() {
      return displayedTool;
    }
  }


}
