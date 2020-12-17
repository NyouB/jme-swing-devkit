package com.jayfella.devkit.swing;

import com.jayfella.devkit.service.inspector.PropertyInspectorService;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import org.jdesktop.swingx.VerticalLayout;

public class InspectorService extends JDialog {

  JPanel panelRoot;

  public InspectorService(Frame frame) {
    super(frame, PropertyInspectorService.WINDOW_ID);
    panelRoot = new JPanel(new VerticalLayout());
    panelRoot.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(new JScrollPane(panelRoot));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    addComponentListener(new WindowLocationSaver(PropertyInspectorService.WINDOW_ID));
    addComponentListener(new WindowSizeSaver(PropertyInspectorService.WINDOW_ID));
    setVisible(true);
  }

  public JPanel getPanelRoot() {
    return panelRoot;
  }

  public void setPanelRoot(JPanel panelRoot) {
    this.panelRoot = panelRoot;
  }
}
