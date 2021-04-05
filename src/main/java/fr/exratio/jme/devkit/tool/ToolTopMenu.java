/*
 * Created by JFormDesigner on Tue Mar 16 07:31:26 CET 2021
 */

package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.main.MainPage.Zone;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicMenuBarUI;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Quentin Raphaneau
 */
@Getter
@Setter
public class ToolTopMenu extends JPanel {

  private final Tool tool;

  public ToolTopMenu(Tool tool) {
    this.tool = tool;
    initComponents();
  }

  private void createUIComponents() {
    titleLabel = new JLabel(tool.getTitle(), tool.getIcon(), SwingConstants.LEFT);
    toolMenuBar = new JMenuBar();
    toolMenuBar.setUI(new BasicMenuBarUI() {

      @Override
      public void paint(Graphics g, JComponent c) {
        g.setColor(UIManager.getColor("background"));
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
      }

    });
    //viewmode buttons
    windowsViewModeRadioButton = new JRadioButtonMenuItem();
    pinnedViewModeRadioButton = new JRadioButtonMenuItem();
    onViewModeChange();
    ButtonGroup viewModeButtonGroup = new ButtonGroup();
    viewModeButtonGroup.add(windowsViewModeRadioButton);
    viewModeButtonGroup.add(pinnedViewModeRadioButton);

    //Zone buttons
    leftTopRadioButton = new JRadioButtonMenuItem();
    leftBotRadioButton = new JRadioButtonMenuItem();
    botLeftRadioButton = new JRadioButtonMenuItem();
    botRightRadioButton = new JRadioButtonMenuItem();
    rightBotRadioButton = new JRadioButtonMenuItem();
    rightTopRadioButton = new JRadioButtonMenuItem();
    onZoneChange();
    ButtonGroup zoneButtonGroup = new ButtonGroup();
    zoneButtonGroup.add(leftTopRadioButton);
    zoneButtonGroup.add(leftBotRadioButton);
    zoneButtonGroup.add(botLeftRadioButton);
    zoneButtonGroup.add(botRightRadioButton);
    zoneButtonGroup.add(rightBotRadioButton);
    zoneButtonGroup.add(rightTopRadioButton);

    
  }

  private void pinnedViewModeRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setViewMode(ViewMode.PIN);
    }
  }

  private void windowsViewModeRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setViewMode(ViewMode.WINDOW);
    }
  }

  private void leftTopRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setZone(Zone.LEFT_TOP);
    }
  }

  private void leftBotRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setZone(Zone.LEFT_BOTTOM);
    }
  }

  private void botLeftRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setZone(Zone.BOTTOM_LEFT);
    }
  }

  private void botRightRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setZone(Zone.BOTTOM_RIGHT);
    }
  }

  private void rightBotRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setZone(Zone.RIGHT_BOTTOM);
    }
  }

  private void rightTopRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.setZone(Zone.RIGHT_TOP);
    }
  }


  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    createUIComponents();

    settings = new JMenu();
    viewModeMenu = new JMenu();
    zoneMenu = new JMenu();

    //======== this ========

    setLayout(new BorderLayout());

    //======== toolMenuBar ========
    {
      toolMenuBar.setBorderPainted(false);
      toolMenuBar.add(titleLabel);

      //======== settings ========
      {
        settings.setIcon(new ImageIcon(getClass().getResource("/icons/settings-wheel-16x16.png")));

        //======== viewModeMenu ========
        {
          viewModeMenu.setText("View Mode");

          //---- windowsViewModeRadioButton ----
          windowsViewModeRadioButton.setText("Window");
          windowsViewModeRadioButton.addItemListener(e -> windowsViewModeRadioButtonItemStateChanged(e));
          viewModeMenu.add(windowsViewModeRadioButton);

          //---- pinnedViewModeRadioButton ----
          pinnedViewModeRadioButton.setText("Dock Pinned");
          pinnedViewModeRadioButton.addItemListener(e -> pinnedViewModeRadioButtonItemStateChanged(e));
          viewModeMenu.add(pinnedViewModeRadioButton);
        }
        settings.add(viewModeMenu);

        //======== zoneMenu ========
        {
          zoneMenu.setText("Zone");

          //---- leftTopRadioButton ----
          leftTopRadioButton.setText("Left Top");
          leftTopRadioButton.addItemListener(e -> leftTopRadioButtonItemStateChanged(e));
          zoneMenu.add(leftTopRadioButton);

          //---- leftBotRadioButton ----
          leftBotRadioButton.setText("Left Bottom");
          leftBotRadioButton.addItemListener(e -> leftBotRadioButtonItemStateChanged(e));
          zoneMenu.add(leftBotRadioButton);

          //---- botLeftRadioButton ----
          botLeftRadioButton.setText("Bot Left");
          botLeftRadioButton.addItemListener(e -> botLeftRadioButtonItemStateChanged(e));
          zoneMenu.add(botLeftRadioButton);

          //---- botRightRadioButton ----
          botRightRadioButton.setText("Bottom Right");
          botRightRadioButton.addItemListener(e -> botRightRadioButtonItemStateChanged(e));
          zoneMenu.add(botRightRadioButton);

          //---- rightBotRadioButton ----
          rightBotRadioButton.setText("Right Bottom");
          rightBotRadioButton.addItemListener(e -> rightBotRadioButtonItemStateChanged(e));
          zoneMenu.add(rightBotRadioButton);

          //---- rightTopRadioButton ----
          rightTopRadioButton.setText("Right Top");
          rightTopRadioButton.addItemListener(e -> rightTopRadioButtonItemStateChanged(e));
          zoneMenu.add(rightTopRadioButton);
        }
        settings.add(zoneMenu);
      }
      toolMenuBar.add(settings);
    }
    add(toolMenuBar, BorderLayout.NORTH);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private JMenuBar toolMenuBar;
  private JLabel titleLabel;
  private JMenu settings;
  private JMenu viewModeMenu;
  private JRadioButtonMenuItem windowsViewModeRadioButton;
  private JRadioButtonMenuItem pinnedViewModeRadioButton;
  private JMenu zoneMenu;
  private JRadioButtonMenuItem leftTopRadioButton;
  private JRadioButtonMenuItem leftBotRadioButton;
  private JRadioButtonMenuItem botLeftRadioButton;
  private JRadioButtonMenuItem botRightRadioButton;
  private JRadioButtonMenuItem rightBotRadioButton;
  private JRadioButtonMenuItem rightTopRadioButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables

  public void onZoneChange() {
    leftTopRadioButton.setSelected(Zone.LEFT_TOP == tool.getZone());
    leftBotRadioButton.setSelected(Zone.LEFT_BOTTOM == tool.getZone());
    botLeftRadioButton.setSelected(Zone.BOTTOM_LEFT == tool.getZone());
    botRightRadioButton.setSelected(Zone.BOTTOM_RIGHT == tool.getZone());
    rightBotRadioButton.setSelected(Zone.RIGHT_BOTTOM == tool.getZone());
    rightTopRadioButton.setSelected(Zone.RIGHT_TOP == tool.getZone());
  }

  public void onViewModeChange() {
    windowsViewModeRadioButton.setSelected(ViewMode.WINDOW == tool.getViewMode());
    pinnedViewModeRadioButton.setSelected(ViewMode.PIN == tool.getViewMode());
  }
}
