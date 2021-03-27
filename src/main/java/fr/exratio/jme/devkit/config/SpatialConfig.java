package fr.exratio.jme.devkit.config;

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
public class SpatialConfig {
  public Point point;
  public Dimension dimension = new Dimension(250, 600);
}
