package fr.exratio.jme.devkit.config;

import fr.exratio.jme.devkit.forms.MainPage.Zone;
import fr.exratio.jme.devkit.forms.ToolView.ViewMode;
import java.awt.Dimension;
import java.awt.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToolsConfig {

  Zone zone = Zone.LEFT_TOP;
  ViewMode viewMode = ViewMode.PINNED;
  SpatialConfig spatialConfig = new SpatialConfig();

}
