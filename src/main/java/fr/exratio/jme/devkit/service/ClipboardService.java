package fr.exratio.jme.devkit.service;

import fr.exratio.jme.devkit.clipboard.SpatialClipboardItem;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Controller;

/**
 * Keeps a reference of a copied item. Currently only stores a single item.
 */
@Controller
@NoArgsConstructor
public class ClipboardService {

  private SpatialClipboardItem spatialClipboardItem;

  public SpatialClipboardItem getSpatialClipboardItem() {
    return spatialClipboardItem;
  }

  public void setSpatialClipboardItem(SpatialClipboardItem spatialClipboardItem) {
    this.spatialClipboardItem = spatialClipboardItem;
  }

  public boolean hasSpatialClipboardItem() {
    return spatialClipboardItem != null;
  }

}
