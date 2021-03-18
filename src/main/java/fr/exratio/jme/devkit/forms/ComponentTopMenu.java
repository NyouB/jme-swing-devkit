/*
 * Created by JFormDesigner on Tue Mar 16 07:31:26 CET 2021
 */

package fr.exratio.jme.devkit.forms;

import fr.exratio.jme.devkit.forms.ToolView.ViewMode;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
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

  public ComponentTopMenu(ToolView tool) {
    initComponents();
    this.tool = tool;
  }

  private void createUIComponents() {
    menuBar1 = new JMenuBar();
    menuBar1.setUI(new BasicMenuBarUI() {

      @Override
      public void paint(Graphics g, JComponent c) {
        g.setColor(UIManager.getColor("background"));
        g.fillRect(0, 0, c.getWidth(), c.getHeight());
      }

    });
    windowsViewModeRadioButton = new JRadioButtonMenuItem();
    pinnedViewModeRadioButton = new JRadioButtonMenuItem();

    ButtonGroup buttonGroup = new ButtonGroup();
    buttonGroup.add(windowsViewModeRadioButton);
    buttonGroup.add(pinnedViewModeRadioButton);
  }

  private void pinnedViewModeRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      ServiceManager.getService(ToolLocationService.class).moveTool(tool, ViewMode.PINNED);
    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
      // Your deselected code here.
    }
  }

  private void windowsViewModeRadioButtonItemStateChanged(ItemEvent e) {
    if (e.getStateChange() == ItemEvent.SELECTED) {
      ServiceManager.getService(ToolLocationService.class).moveTool(tool, ViewMode.WINDOWED);
    } else if (e.getStateChange() == ItemEvent.DESELECTED) {
      // Your deselected code here.
    }
  }

  private void initComponents() {
    // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
    // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
    createUIComponents();

    titleLabel = new JLabel();
    settings = new JMenu();
    viewModeMenu = new JMenu();

    //======== this ========
    setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax
        .swing.border.EmptyBorder(0, 0, 0, 0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",
        javax.swing
            .border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.
        Font("Dia\u006cog", java.awt.Font.BOLD, 12), java.awt.Color.red
    ), getBorder()));
    addPropertyChangeListener(new java.beans.PropertyChangeListener() {
      @Override
      public void propertyChange(java.beans.PropertyChangeEvent e) {
        if ("bord\u0065r".equals(e.getPropertyName(
        ))) {
          throw new RuntimeException();
        }
      }
    });
    setLayout(new BorderLayout());

    //======== menuBar1 ========
    {
      menuBar1.setBorderPainted(false);

      //---- titleLabel ----
      titleLabel.setText("text");
      menuBar1.add(titleLabel);

      //======== settings ========
      {
        settings.setIcon(new ImageIcon(getClass().getResource("/icons/settings-wheel-16x16.png")));

        //======== viewModeMenu ========
        {
          viewModeMenu.setText("View Mode");

          //---- windowsViewModeRadioButton ----
          windowsViewModeRadioButton.setText("Window");
          windowsViewModeRadioButton
              .addItemListener(e -> windowsViewModeRadioButtonItemStateChanged(e));
          viewModeMenu.add(windowsViewModeRadioButton);

          //---- pinnedViewModeRadioButton ----
          pinnedViewModeRadioButton.setText("Dock Pinned");
          pinnedViewModeRadioButton
              .addItemListener(e -> pinnedViewModeRadioButtonItemStateChanged(e));
          viewModeMenu.add(pinnedViewModeRadioButton);
        }
        settings.add(viewModeMenu);
      }
      menuBar1.add(settings);
    }
    add(menuBar1, BorderLayout.NORTH);
    // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }

  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  // Generated using JFormDesigner Evaluation license - Quentin Raphaneau
  private JMenuBar menuBar1;
  private JLabel titleLabel;
  private JMenu settings;
  private JMenu viewModeMenu;
  private JRadioButtonMenuItem windowsViewModeRadioButton;
  private JRadioButtonMenuItem pinnedViewModeRadioButton;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
