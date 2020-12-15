import com.jme3.app.SimpleApplication;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.Geometry;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class TestAwtPanels extends SimpleApplication {

  private static TestAwtPanels app;
  private static int panelsClosed = 0;

  private static void createWindowForPanel(AwtPanel panel, int location) {
    JFrame frame = new JFrame("Render Display " + location);
    frame.getContentPane().setLayout(new BorderLayout());
    JPanel northArea = new JPanel();
    northArea.setLayout(new BoxLayout(northArea, BoxLayout.X_AXIS));
    northArea.add(new JButton("toolbarButton"));
    northArea.add(new JLabel("NORTH"));
    frame.getContentPane().add(northArea, BorderLayout.NORTH);
    frame.getContentPane().add(new JLabel("SOUTH"), BorderLayout.SOUTH);
    frame.getContentPane().add(new JLabel("EAST"), BorderLayout.EAST);
    frame.getContentPane().add(new JLabel("WEST"), BorderLayout.WEST);
    frame.getContentPane().add(panel, BorderLayout.CENTER);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    frame.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        if (++panelsClosed == 2) {
          app.stop();
        }
      }
    });
    frame.pack();
    frame.setLocation(location, Toolkit.getDefaultToolkit().getScreenSize().height - 400);
    frame.setVisible(true);
  }

  public static void main(String[] args) {
    Logger.getLogger("com.jme3").setLevel(Level.WARNING);

    app = new TestAwtPanels();
    app.setShowSettings(false);
    AppSettings settings = new AppSettings(true);
    settings.setCustomRenderer(AwtPanelsContext.class);
    settings.setFrameRate(60);
    app.setSettings(settings);
    app.start();
    while (app.getViewPort() == null) {

      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }

    SwingUtilities.invokeLater(() -> {
      final AwtPanelsContext ctx = (AwtPanelsContext) app.getContext();
      AwtPanel panel = ctx.createPanel(PaintMode.Accelerated);
      panel.setPreferredSize(new Dimension(400, 300));
      ctx.setInputSource(panel);
      panel.attachTo(true, app.viewPort);
      AwtPanel panel2 = ctx.createPanel(PaintMode.Accelerated);
      panel2.setPreferredSize(new Dimension(400, 300));
      panel2.attachTo(false, app.guiViewPort);
      app.guiViewPort.setClearFlags(true, true, true);

      createWindowForPanel(panel, 300);
      createWindowForPanel(panel2, 700);
    });
  }

  @Override
  public void simpleInitApp() {
    flyCam.setDragToRotate(true);

    Box b = new Box(1, 1, 1); // create cube shape
    Geometry geom = new Geometry("Box", b);  // create cube geometry from the shape
    Material mat = new Material(assetManager,
        "Common/MatDefs/Misc/Unshaded.j3md");  // create a simple material
    mat.setColor("Color", ColorRGBA.Blue);   // set color of material to blue
    geom.setMaterial(mat);                   // set the cube's material
    rootNode.attachChild(geom);
    viewPort.setBackgroundColor(ColorRGBA.Red);

  }
}