package com.jayfella.devkit.swing;

import com.jayfella.devkit.service.SceneTreeService;
import java.awt.Frame;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.WindowConstants;

public class TreeView extends JDialog {

  public TreeView(Frame owner, JTree jTree) {
    super(owner, "Scene Tree");

    setContentPane(new JScrollPane(jTree));
    setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
    pack();
    addComponentListener(new WindowLocationSaver(SceneTreeService.WINDOW_ID));
    addComponentListener(new WindowSizeSaver(SceneTreeService.WINDOW_ID));

    setVisible(true);

  }
}
