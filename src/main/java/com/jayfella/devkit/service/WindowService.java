package com.jayfella.devkit.service;

import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.swing.WindowLocationSaver;
import com.jayfella.devkit.swing.WindowSizeSaver;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Window;
import java.util.Arrays;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * A simple service to set window positions and sizes from saved settings.
 */
public class WindowService implements Service {

  private final JFrame mainFrame;
  private final long threadId;

  public WindowService(JFrame mainFrame) {
    this.mainFrame = mainFrame;
    threadId = Thread.currentThread().getId();
  }

  /**
   * Returns the primary JFrame of the devkit (the window with the scene in it).
   *
   * @return the primary JFrame of the devkit.
   */
  public JFrame getMainFrame() {
    return mainFrame;
  }

  /**
   * Returns an instance of the window matching the exact specified text, or null if none exists.
   *
   * @param title the exact string text title of the window.
   * @return an instance of the window matching the exact specified text, or null if none exists.
   */
  public Window getWindow(String title) {
    return Arrays.stream(mainFrame.getOwnedWindows())
        .filter(w -> w instanceof Dialog)
        .filter(w -> ((Dialog) w).getTitle().equals(title))
        .findFirst()
        .orElse(null);
  }

  /**
   * Sets the position of a window from saved data.
   *
   * @param window the window to position.
   * @param name the name of the window.
   */
  public void positionWindowFromSavedPosition(Window window, String name) {
    Point location = DevKitConfig.getInstance().getSdkConfig().getWindowLocation(name);
    if (location != null) {
      window.setLocation(location);
    }
  }

  /**
   * Resizes the window from saved data.
   *
   * @param window the window to resize.
   * @param name the name of the window.
   */
  public void sizeWindowFromSavedSize(Window window, String name) {
    Dimension dimension = DevKitConfig.getInstance().getSdkConfig().getWindowDimensions(name);
    if (dimension != null) {
      window.setSize(dimension);
    }
  }

  public JDialog createDialog(Frame parent, JComponent content, String title, boolean saveLocation,
      boolean saveSize) {

    JDialog dialog = new JDialog(parent, title);

    dialog.setContentPane(content);
    dialog.pack();

    dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

    if (saveLocation) {
      dialog.addComponentListener(new WindowLocationSaver(title));
    }

    if (saveSize) {
      dialog.addComponentListener(new WindowSizeSaver(title));
    }

    if (saveLocation) {
      ServiceManager.getService(WindowService.class).positionWindowFromSavedPosition(dialog, title);
    }

    if (saveSize) {
      ServiceManager.getService(WindowService.class).sizeWindowFromSavedSize(dialog, title);
    }

    return dialog;

  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  @Override
  public void stop() {

  }

}
