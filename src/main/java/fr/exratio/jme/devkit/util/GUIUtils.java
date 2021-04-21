package fr.exratio.jme.devkit.util;

import fr.exratio.jme.devkit.tool.Tool;
import fr.exratio.jme.devkit.tool.ViewMode;
import java.awt.Dimension;
import java.awt.Window;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class GUIUtils {

  private GUIUtils() {
  }

  public static void closeToolDialog(Tool tool) {
    if (ViewMode.WINDOW == tool.getViewMode()) {
      Window window = (Window) SwingUtilities.getRoot(tool);
      window.dispose();
    }
  }

  public static Window wrapInWindow(Tool tool) {
    Window window = null;
    if (ViewMode.WINDOW != tool.getViewMode()) {
      window = createDialog(null,
          tool,
          tool.getTitle());
      window.setMinimumSize(new Dimension(400, 500));
      window.setVisible(true);
    }
    return window;
  }

  public static JDialog createDialog(Window parent, JComponent content, String title) {
    JDialog dialog = new JDialog(parent, title);
    dialog.setContentPane(content);
    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    dialog.pack();
    dialog.setLocationRelativeTo(parent);
    dialog.setVisible(true);
    return dialog;

  }
}
