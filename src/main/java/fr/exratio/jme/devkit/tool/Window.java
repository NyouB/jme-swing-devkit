package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;

public class Window implements ViewMode {

  private ToolLocationService toolLocationService = ServiceManager
      .getService(ToolLocationService.class);


  @Override
  public void changeView(ToolView toolView) {
    toolView.getComponentTopMenu1().getPinnedViewModeRadioButton().setSelected(false);
    toolView.getComponentTopMenu1().getWindowsViewModeRadioButton().setSelected(true);

    toolLocationService.getMainPage().removeTab(toolView);
    toolLocationService.wrapInWindow(toolView);
  }
}

