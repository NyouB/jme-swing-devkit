package fr.exratio.jme.devkit;

import fr.exratio.jme.devkit.forms.MainPage2.Zone;
import fr.exratio.jme.devkit.tool.Tool;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.painter.MattePainter;

public class ZonedToolBar extends JToolBar {

  public static final String COLOR_BACKGROUND = "background";
  public static final String COLOR_BACKGROUND_SELECTED = "backgroundSelectedSecondary";
  public static final String COLOR_BACKGROUND_HOVER = "backgroundHover";

  public static final int LEFT = 0;
  public static final int RIGHT = 1;
  public static final int TOP = 2;
  public static final int BOTTOM = 3;

  private final ToolBarZone firstZone = new ToolBarZone();
  private final ToolBarZone secondZone = new ToolBarZone();
  private Side side;

  public ZonedToolBar() {
    this(Side.CENTER);
  }

  public ZonedToolBar(Side side) {
    setSide(side);
    setFloatable(false);
    add(secondZone);
    add(getOrientation() == VERTICAL ? Box.createVerticalGlue() : Box.createHorizontalGlue());
    add(firstZone);
    setOpaque(true);
    setBackground(Color.BLUE);
  }

  public ToolBarZone getFirstZone() {
    return firstZone;
  }

  public ToolBarZone getSecondZone() {
    return secondZone;
  }

  public Side getSide() {
    return side;
  }

  public void setSide(Side side) {
    this.side = side;
    setOrientation(side == Side.CENTER ? HORIZONTAL : VERTICAL);
  }

  public void add(Tool tool) {
    switch (tool.getZone()) {
      case LEFT_BOTTOM:
      case RIGHT_BOTTOM:
      case BOTTOM_LEFT:
        firstZone.addTool(tool);
        break;
      case LEFT_TOP:
      case RIGHT_TOP:
      case BOTTOM_RIGHT:
        secondZone.addTool(tool);
        break;
      default:
        break;

    }
  }

  public void remove(Tool tool, int zone) {
    switch (zone) {
      case LEFT:
      case BOTTOM:
        firstZone.removeTool(tool);
        break;
      case RIGHT:
      case TOP:
        secondZone.removeTool(tool);
        break;
      default:
        break;

    }
  }

  public enum Side {
    CENTER(Math.toRadians(0)) {
      @Override
      public JLabel getOrientedLabel(String title, Icon icon) {
        JXLabel tabLabel = new JXLabel(title, icon, SwingConstants.LEADING);
        return tabLabel;
      }
    },
    LEFT(Math.toRadians(270)) {
      @Override
      public JLabel getOrientedLabel(String title, Icon icon) {
        JXLabel tabLabel = new JXLabel(title, icon, SwingConstants.LEADING);
        tabLabel.setTextRotation(3 * Math.PI / 2);
        return tabLabel;
      }
    }, RIGHT(Math.toRadians(90)) {
      @Override
      public JLabel getOrientedLabel(String title, Icon icon) {
        JXLabel tabLabel = new JXLabel(title, icon, SwingConstants.LEADING);
        tabLabel.setTextRotation(Math.PI / 2);
        return tabLabel;
      }
    };

    abstract JLabel getOrientedLabel(String title, Icon icon);

    private final double labelRotation;

    Side(double rotation) {
      labelRotation = rotation;
    }
  }

  public class ToolBarZone extends JPanel {

    private Zone zone;
    List<Tool> toolList = new ArrayList<>();
    Map<Tool, JXLabel> labelMap = new HashMap<>();
    Tool selectedTool;

    public ToolBarZone() {
      // ToolBarZone.this.setLayout(new BoxLayout(ToolBarZone.this, BoxLayout.Y_AXIS));
    }

    public void addTool(Tool tool) {
      toolList.add(tool);
      JXLabel label = createLabel(tool.getTitle(), tool.getIcon());
      label.addMouseListener(new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {
          zone.select(tool);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
          if (tool.equals(selectedTool)) {
            return;
          }
          label.setBackgroundPainter(new MattePainter(UIManager.getColor(COLOR_BACKGROUND_HOVER)));

        }

        @Override
        public void mouseExited(MouseEvent e) {
          if (tool.equals(selectedTool)) {
            return;
          }
          label.setBackgroundPainter(new MattePainter(UIManager.getColor(COLOR_BACKGROUND)));
        }
      });
      labelMap.put(tool, label);
      ToolBarZone.this.add(label);
      ToolBarZone.this.selectTool(tool);
    }

    public void selectTool(Tool tool) {
      if (selectedTool != null) {
        SwingUtilities.invokeLater(() ->
            labelMap.get(selectedTool)
                .setBackgroundPainter(new MattePainter(UIManager.getColor(COLOR_BACKGROUND)))
        );
      }
      selectedTool = tool;
      labelMap.get(selectedTool)
          .setBackgroundPainter(new MattePainter(UIManager.getColor(COLOR_BACKGROUND_SELECTED)));
    }


    public void removeTool(Tool tool) {
      toolList.remove(tool);
      ToolBarZone.this.remove(labelMap.get(tool));
      if (selectedTool != null && selectedTool.equals(tool)) {
        selectedTool = null;
      }
    }

    public void setZone(Zone zone) {
      this.zone = zone;
    }

    public JXLabel createLabel(String text, Icon icon) {
      JXLabel jxLabel = new JXLabel(text, icon, CENTER);
      jxLabel.setBackgroundPainter(new MattePainter(UIManager.getColor(COLOR_BACKGROUND)));
      jxLabel.setTextRotation(side.labelRotation);
      jxLabel.setOpaque(true);
      return jxLabel;
    }
  }


}
