package fr.exratio.devkit.properties.component.texture2d;

import java.io.File;
import java.io.IOException;
import javax.swing.filechooser.FileSystemView;

class DirectoryRestrictedFileSystemView extends FileSystemView {

  private final File[] rootDirectories;

  DirectoryRestrictedFileSystemView(File... rootDirectories) {
    this.rootDirectories = rootDirectories;
  }

  @Override
  public File createNewFolder(File containingDir) throws IOException {
    throw new UnsupportedOperationException("Unable to create directory");
  }

  @Override
  public File[] getRoots() {
    return rootDirectories;
  }

  @Override
  public boolean isRoot(File file) {
    for (File root : rootDirectories) {
      if (root.equals(file)) {
        return true;
      }
    }
    return false;
  }
}