package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.util.GUIUtils;

public enum ViewMode {
  WINDOW {
    @Override
    void changeView(Tool tool) {
      if (tool.isRegistered() && WINDOW == tool.getViewMode()) {
        return;
      }
      tool.getZone().remove(tool);
      GUIUtils.wrapInWindow(tool);
    }
  },
  PIN {
    @Override
    void changeView(Tool tool) {
      if (tool.isRegistered() && PIN == tool.getViewMode()) {
        return;
      }
      GUIUtils.closeToolDialog(tool);
      tool.getZone().add(tool);
    }
  };


  abstract void changeView(Tool toolView);

}
