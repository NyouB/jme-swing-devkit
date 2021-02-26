package fr.exratio.devkit.properties.component.texture2d;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import javax.swing.filechooser.FileFilter;
import org.apache.commons.io.FilenameUtils;

public class TextureFilter extends FileFilter {

  public static final List<String> imageExtensions = Lists.newArrayList(
      "jpg", "gif", "png", "dds", "hdr", "jpeg", "bmp", "tiff");

  @Override
  public boolean accept(File f) {
    String extension = FilenameUtils.getExtension(f.getName());
    return imageExtensions.contains(extension);
  }

  @Override
  public String getDescription() {
    return "JME supported texture format(\"jpg\", \"gif\", \"png\", \"dds\", \"hdr\")";
  }
}
