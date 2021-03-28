package fr.exratio.jme.devkit.tool;

import fr.exratio.jme.devkit.util.GUIUtils;

public enum ViewMode {
  WINDOW {
    @Override
    void changeView(Tool toolView) {
      if (toolView.isRegistered() && WINDOW == toolView.getViewMode()) {
        return;
      }
      toolView.getZone().remove(toolView);
      GUIUtils.wrapInWindow(toolView);
    }
  },
  PIN {
    @Override
    void changeView(Tool toolView) {
      if (toolView.isRegistered() && PIN == toolView.getViewMode()) {
        return;
      }
      GUIUtils.closeToolDialog(toolView);
      toolView.getZone().add(toolView);
    }
  };


  abstract void changeView(Tool toolView);

}
