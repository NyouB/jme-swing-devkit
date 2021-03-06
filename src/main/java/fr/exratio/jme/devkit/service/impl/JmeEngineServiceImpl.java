package fr.exratio.jme.devkit.service.impl;

import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.system.awt.AwtPanel;
import com.jme3.system.awt.AwtPanelsContext;
import com.jme3.system.awt.PaintMode;
import com.jme3.util.MaterialDebugAppState;
import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.jme.CameraRotationWidgetState;
import fr.exratio.jme.devkit.jme.DebugGridState;
import fr.exratio.jme.devkit.service.JmeEngineService;
import java.awt.Dimension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmeEngineServiceImpl extends JmeEngineService {

  private static final Logger log = LoggerFactory.getLogger(JmeEngineServiceImpl.class);
  // let this service be accessed by any thread (so we can use .enqueue anywhere).
  private final long threadId = -1;
  private FilterPostProcessor fpp;
  private AwtPanel jmePanel;
  private boolean initialised;

  @Override
  public FilterPostProcessor getFilterPostProcessor() {
    return fpp;
  }

  @Override
  public void setFilterPostProcessor(FilterPostProcessor fpp) {

    if (this.fpp != null) {
      viewPort.removeProcessor(this.fpp);
    }

    this.fpp = fpp;

    if (this.fpp != null) {
      viewPort.addProcessor(this.fpp);
    }

  }

  @Override
  public void simpleInitApp() {

    if (DevKitConfig.getInstance().isShowCamRotationWidget()) {
      stateManager.attach(new CameraRotationWidgetState());
    }

    if (DevKitConfig.getInstance().isShowGrid()) {
      stateManager.attach(new DebugGridState());
    }

    MaterialDebugAppState materialDebugAppState = new MaterialDebugAppState();
    stateManager.attach(materialDebugAppState);

    applyCameraFrustumSizes();

    FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
    fpp.addFilter(new TranslucentBucketFilter());

    setFilterPostProcessor(fpp);
    AwtPanelsContext ctx = (AwtPanelsContext) getContext();
    jmePanel = ctx.createPanel(PaintMode.Accelerated);
    ctx.setInputSource(jmePanel);
    jmePanel.setMinimumSize(new Dimension(100, 500));
    jmePanel.attachTo(true, getViewPort());
    jmePanel.attachTo(true, getGuiViewPort());
    initialised = true;

  }

  /**
   * Applies any changes made to the DevKitConfig camera configuration. Used internally.
   */
  @Override
  public void applyCameraFrustumSizes() {
    
    float width, height;

    if (jmePanel != null) {
      width = jmePanel.getWidth();
      height = jmePanel.getHeight();
    } else {
      width = viewPort.getCamera().getWidth();
      height = viewPort.getCamera().getHeight();
    }

    viewPort.getCamera().setFrustumPerspective(
        DevKitConfig.getInstance().getFieldOfView(),
        width / height,
        DevKitConfig.getInstance().getFrustumNear(),
        DevKitConfig.getInstance().getFrustumFar());

  }

  @Override
  public AwtPanel getAWTPanel() {
    return jmePanel;
  }

  @Override
  public long getThreadId() {
    return threadId;
  }

  public boolean isInitialised() {
    return initialised;
  }

}
