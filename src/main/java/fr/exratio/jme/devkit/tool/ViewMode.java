package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.service.ServiceManager;
import fr.exratio.jme.devkit.service.ToolLocationService;

public enum ViewMode {
  WINDOW {
    @Override
    void changeView(Tool toolView) {
      if (toolLocationService.isToolRegistered(toolView) && WINDOW == toolView.getViewMode()) {
        return;
      }
      toolView.getZone().remove(toolView);
      toolLocationService.wrapInWindow(toolView);
    }
  },
  PIN {
    @Override
    void changeView(Tool toolView) {
      if (toolLocationService.isToolRegistered(toolView) && PIN == toolView.getViewMode()) {
        return;
      }
      toolLocationService.closeToolDialog(toolView);
      toolView.getZone().add(toolView);
    }
  };

  ToolLocationService toolLocationService = ServiceManager.getService(ToolLocationService.class);

  abstract void changeView(Tool toolView);

}
