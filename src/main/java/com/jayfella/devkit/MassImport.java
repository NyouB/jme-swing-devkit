package com.jayfella.devkit;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import com.jayfella.devkit.config.DevKitConfig;
import com.jayfella.devkit.core.LogUtil;
import com.simsilica.jmec.Convert;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.log4j.Level;

public class MassImport {

  private static final Logger log = Logger.getLogger(MassImport.class.getName());

  public static void main(String... args) {

    LogUtil.initializeLogger(Level.INFO, true);

    String assetRoot = DevKitConfig.getInstance().getProjectConfig().getAssetRootDir();

    Path originalsPath = Paths.get(assetRoot, "Originals");

    log.info("Scanning path for models: " + originalsPath.toString());

    if (!originalsPath.toFile().exists()) {
      log.info("No Originals directory exists. Nothing to convert.");
      return;
    }

    Convert convert = new Convert();
    convert.setTargetRoot(new File(assetRoot));

    try {

      List<Path> modelPaths = Files.walk(originalsPath, Integer.MAX_VALUE)
          .filter(Files::isRegularFile)
          .filter(p -> p.toString().toLowerCase().endsWith(".gltf") || p.toString().toLowerCase()
              .endsWith(".glb"))
          .collect(Collectors.toList());

      log.info("Found " + modelPaths.size() + " models to convert.");

      for (Path modelPath : modelPaths) {

        convert.setSourceRoot(modelPath.getParent().toFile());

        // Get the target folder:
        // - remove the original

        String targetAssetPath = modelPath.getParent().toString()
            .replace(originalsPath.toString(), "");
        targetAssetPath = targetAssetPath.replace(modelPath.getFileName().toString(), "");

        if (targetAssetPath.startsWith("\\") || targetAssetPath.startsWith("/")) {
          targetAssetPath = targetAssetPath.substring(1);
        }

        if (targetAssetPath.endsWith("\\") || targetAssetPath.endsWith("/")) {
          targetAssetPath = targetAssetPath.substring(0, targetAssetPath.length() - 1);
        }

        targetAssetPath = targetAssetPath.replace("\\", "/");

        convert.setTargetAssetPath(targetAssetPath);

        String format = "Converting: " + System.lineSeparator();
        format += "\t- Original: " + modelPath.toString() + System.lineSeparator();
        format += "\t- Target Root: " + assetRoot + System.lineSeparator();
        format += "\t- Target Path: " + targetAssetPath + System.lineSeparator();

        log.info(format);

        convert.convert(modelPath.toFile());

        // remove the .gltf or .glb extension from the name.
        Path resultFile = Paths.get(
            DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(),
            targetAssetPath,
            modelPath.getFileName() + ".j3o");

        Path extRemoved = Paths.get(
            DevKitConfig.getInstance().getProjectConfig().getAssetRootDir(),
            targetAssetPath,
            modelPath.getFileName().toString()
                .replace(".glb", "").replace(".GLB", "")
                .replace(".gltf", "").replace(".GLTF", "")
                + ".j3o"
        );

        try {
          Files.move(resultFile, extRemoved, REPLACE_EXISTING);
        } catch (IOException ex) {
          ex.printStackTrace();
        }

      }

      log.info("Conversion finished.");

    } catch (IOException e) {
      e.printStackTrace();
    }

  }


}
