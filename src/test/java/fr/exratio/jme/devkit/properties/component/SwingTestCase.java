package fr.exratio.jme.devkit.properties.component;

import javax.swing.JFrame;

public abstract class SwingTestCase {

  private JFrame testFrame;

  protected void tearDown() throws Exception {
    if (this.testFrame != null) {
      this.testFrame.dispose();
      this.testFrame = null;
    }
  }

  public JFrame getTestFrame() {
    if (this.testFrame == null) {
      this.testFrame = new JFrame("Test");
    }
    testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    return this.testFrame;
  }
}