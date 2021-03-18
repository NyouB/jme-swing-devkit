package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;

public class Pin implements ViewMode {

  private ToolLocationService toolLocationService = ServiceManager
      .getService(ToolLocationService.class);

  @Override
  public void changeView(ToolView toolView) {
    toolView.getComponentTopMenu1().getPinnedViewModeRadioButton().setSelected(true);
    toolView.getComponentTopMenu1().getWindowsViewModeRadioButton().setSelected(false);
    toolLocationService.closeToolDialog(toolView);
    toolLocationService.getMainPage()
        .addTab(toolView.getTitle(), toolView, toolView.getIcon(), toolView.getZone());
  }
}
