/*
 * Created by JFormDesigner on Tue Mar 16 07:31:26 CET 2021
 */

package fr.exratio.jme.devkit.forms;

import fr.exratio.jme.devkit.forms.MainPage.Zone;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
import fr.exratio.jme.devkit.tool.Pin;
import fr.exratio.jme.devkit.tool.ToolView;
import fr.exratio.jme.devkit.tool.Window;
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
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicMenuBarUI;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Quentin Raphaneau
 */
@Getter
@Setter
public class ComponentTopMenu extends JPanel {

  private final ToolView tool;
  private ToolLocationService toolLocationService = ServiceManager.getService(ToolLocationService.class);

  public ComponentTopMenu(ToolView tool) {
    initComponents();
    this.tool = tool;
  }

  private void createUIComponents() {
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
    topRightRadioButton = new JRadioButtonMenuItem();
    topLeftRadioButton = new JRadioButtonMenuItem();
    ButtonGroup zoneButtonGroup = new ButtonGroup();
    zoneButtonGroup.add(leftTopRadioButton);
    zoneButtonGroup.add(leftBotRadioButton);
    zoneButtonGroup.add(botLeftRadioButton);
    zoneButtonGroup.add(botRightRadioButton);
    zoneButtonGroup.add(rightBotRadioButton);
    zoneButtonGroup.add(rightTopRadioButton);
    zoneButtonGroup.add(topRightRadioButton);
    zoneButtonGroup.add(topLeftRadioButton);

    
  }

  private void pinnedViewModeRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.changeViewMode(new Pin());
    }
  }

  private void windowsViewModeRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      tool.changeViewMode(new Window());
    }
  }

  private void leftTopRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.LEFT_TOP);
    }
  }

  private void leftBotRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.LEFT_BOTTOM);
    }
  }

  private void botLeftRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.BOTTOM_LEFT);
    }
  }

  private void botRightRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.BOTTOM_RIGHT);
    }
  }

  private void rightBotRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.RIGHT_BOTTOM);
    }
  }

  private void rightTopRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.RIGHT_TOP);
    }
  }

  private void topRightRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.TOP_RIGHT);
    }
  }

  private void topLeftRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      toolLocationService.moveZone(tool, Zone.TOP_LEFT);
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    createUIComponents();

    titleLabel = new JLabel();
    settings = new JMenu();
    viewModeMenu = new JMenu();
    zoneMenu = new JMenu();

    //======== this ========
    setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax . swing. border .EmptyBorder ( 0
    , 0 ,0 , 0) ,  "JFor\u006dDesi\u0067ner \u0045valu\u0061tion" , javax. swing .border . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM
    , new java. awt .Font ( "Dia\u006cog", java .awt . Font. BOLD ,12 ) ,java . awt. Color .red ) ,
     getBorder () ) );  addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java . beans. PropertyChangeEvent e
    ) { if( "bord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException( ) ;} } );
    setLayout(new BorderLayout());

    //======== toolMenuBar ========
    {
      toolMenuBar.setBorderPainted(false);

      //---- titleLabel ----
      titleLabel.setText("text");
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

          //---- topRightRadioButton ----
          topRightRadioButton.setText("Top RIght");
          topRightRadioButton.addItemListener(e -> topRightRadioButtonItemStateChanged(e));
          zoneMenu.add(topRightRadioButton);

          //---- topLeftRadioButton ----
          topLeftRadioButton.setText("Top Left");
          topLeftRadioButton.addItemListener(e -> topLeftRadioButtonItemStateChanged(e));
          zoneMenu.add(topLeftRadioButton);
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
  private JRadioButtonMenuItem topRightRadioButton;
  private JRadioButtonMenuItem topLeftRadioButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
