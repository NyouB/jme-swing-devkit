package fr.exratio.jme.devkit.forms;

import fr.exratio.jme.devkit.forms.ToolView.ViewMode;
import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;
import fr.exratio.jme.devkit.swing.JSplitPaneWithZeroSizeDivider;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import org.jdesktop.swingx.JXLabel;

public class MainPage extends JPanel {

  public static String WINDOW_ID = "main";

  private JPanel rootPanel;
  private JPanel botPanel;
  private JPanel rightPanel;
  private JPanel centerPanel;
  private JPanel leftPanel;
  private JTabbedPane centerTabbedPane;
  private JTabbedPane leftTabbedPane;
  private JTabbedPane botTabbedPane;
  private JSplitPane rightSplitPane;
  private JSplitPane leftSplitPane;
  private JSplitPane verticalSplitPane;
  private JScrollPane rightArea;

  public MainPage() {
  }

  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer >>> IMPORTANT!! <<< DO NOT edit this method OR
   * call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    createUIComponents();
    rootPanel.setLayout(new BorderLayout(0, 0));
    final JLabel label1 = new JLabel();
    label1.setText("WEST");
    rootPanel.add(label1, BorderLayout.WEST);
    final JLabel label2 = new JLabel();
    label2.setText("SOUTH");
    rootPanel.add(label2, BorderLayout.SOUTH);
    final JLabel label3 = new JLabel();
    label3.setText("EAST");
    rootPanel.add(label3, BorderLayout.EAST);
    verticalSplitPane.setContinuousLayout(true);
    verticalSplitPane.setOneTouchExpandable(true);
    verticalSplitPane.setOrientation(0);
    rootPanel.add(verticalSplitPane, BorderLayout.CENTER);
    leftSplitPane.setContinuousLayout(true);
    leftSplitPane.setOneTouchExpandable(true);
    verticalSplitPane.setLeftComponent(leftSplitPane);
    rightSplitPane.setOneTouchExpandable(true);
    leftSplitPane.setRightComponent(rightSplitPane);
    rightPanel = new JPanel();
    rightPanel.setLayout(new BorderLayout(0, 0));
    rightPanel.setMinimumSize(new Dimension(100, 50));
    rightPanel.setPreferredSize(new Dimension(200, 500));
    rightSplitPane.setRightComponent(rightPanel);
    rightArea = new JScrollPane();
    rightArea.setMaximumSize(new Dimension(-1, -1));
    rightArea.setMinimumSize(new Dimension(0, 0));
    rightPanel.add(rightArea, BorderLayout.CENTER);
    centerPanel.setLayout(new BorderLayout(0, 0));
    centerPanel.setMinimumSize(new Dimension(300, 300));
    centerPanel.setPreferredSize(new Dimension(600, 480));
    rightSplitPane.setLeftComponent(centerPanel);
    centerTabbedPane = new JTabbedPane();
    centerPanel.add(centerTabbedPane, BorderLayout.CENTER);
    leftPanel = new JPanel();
    leftPanel.setLayout(new BorderLayout(0, 0));
    leftPanel.setMinimumSize(new Dimension(100, 300));
    leftPanel.setPreferredSize(new Dimension(200, 500));
    leftSplitPane.setLeftComponent(leftPanel);
    leftTabbedPane = new JTabbedPane();
    leftTabbedPane.setMinimumSize(new Dimension(200, 300));
    leftTabbedPane.setPreferredSize(new Dimension(200, 300));
    leftTabbedPane.setTabPlacement(2);
    leftPanel.add(leftTabbedPane, BorderLayout.CENTER);
    botPanel = new JPanel();
    botPanel.setLayout(new BorderLayout(0, 0));
    botPanel.setMinimumSize(new Dimension(500, 10));
    botPanel.setPreferredSize(new Dimension(500, 300));
    verticalSplitPane.setRightComponent(botPanel);
    botTabbedPane = new JTabbedPane();
    botPanel.add(botTabbedPane, BorderLayout.CENTER);
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return rootPanel;
  }


  private void createUIComponents() {
    rootPanel = this;
    rightSplitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT);
    leftSplitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.HORIZONTAL_SPLIT);
    verticalSplitPane = new JSplitPaneWithZeroSizeDivider(JSplitPane.VERTICAL_SPLIT);
    centerPanel = new JPanel(new BorderLayout());
    centerPanel.addComponentListener(new ComponentAdapter() {
      @Override
      public void componentResized(ComponentEvent e) {
        getParent().revalidate();
      }
    });

  }

  public void addTab(String title, Component content, Icon icon, Zone zone) {
    switch (zone) {
      case LEFT:
      case LEFT_TOP:
        leftTabbedPane.addTab(title, icon, content);
        int tabIndex = leftTabbedPane.indexOfTab(title);
        JXLabel tabLabel = new JXLabel(title);
        tabLabel.setTextRotation(3 * Math.PI / 2);
        leftTabbedPane.setTabComponentAt(tabIndex, tabLabel);
        break;
      case LEFT_BOTTOM:
        break;
      case TOP:
      case TOP_LEFT:
        break;
      case TOP_RIGHT:
        break;
      case RIGHT:
      case RIGHT_TOP:
        break;
      case RIGHT_BOTTOM:
        break;
      case BOTTOM:
      case BOTTOM_LEFT:
        botTabbedPane.addTab(title, icon, content);
        break;
      case BOTTOM_RIGHT:
        break;
      case CENTER:
        centerTabbedPane.addTab(title, icon, content);
        break;
    }
  }

  public void setRightArea(Component component) {
    rightArea.setViewportView(component);
  }

  public void removeTab(JComponent tool) {
    leftTabbedPane.remove(tool);
    botTabbedPane.remove(tool);
    centerTabbedPane.remove(tool);
    revalidate();
  }

  public enum Zone {
    LEFT, TOP, BOTTOM, RIGHT, LEFT_TOP, LEFT_BOTTOM, TOP_LEFT, TOP_RIGHT, RIGHT_TOP, RIGHT_BOTTOM, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER
  }

}
