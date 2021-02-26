package fr.exratio.devkit.swing;

import fr.exratio.devkit.config.DevKitConfig;
import fr.exratio.devkit.forms.MainPage;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Saves the size of the JME canvas
 */
public class JmeCanvasSizeSaver implements ComponentListener {

  @Override
  public void componentResized(ComponentEvent e) {
    Canvas canvas = (Canvas) e.getComponent();
    Dimension size = canvas.getSize();

    DevKitConfig.getInstance().getSdkConfig().setWindowDimensions(MainPage.WINDOW_ID, size);
    DevKitConfig.getInstance().save();
  }

  @Override
  public void componentMoved(ComponentEvent e) {

  }

  @Override
  public void componentShown(ComponentEvent e) {

  }

  @Override
  public void componentHidden(ComponentEvent e) {

  }

}
