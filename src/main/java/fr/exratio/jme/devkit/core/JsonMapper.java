package fr.exratio.jme.devkit.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import fr.exratio.jme.devkit.core.json.ColorRGBASerializer;
import fr.exratio.jme.devkit.core.json.QuaternionSerializer;
import fr.exratio.jme.devkit.core.json.Vector3fSerializer;
import java.io.File;
import java.io.IOException;

public class JsonMapper {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper()
        .enable(SerializationFeature.INDENT_OUTPUT)
        .registerModule(new SimpleModule()
            .addSerializer(ColorRGBA.class, new ColorRGBASerializer(ColorRGBA.class)))
        .registerModule(new SimpleModule()
            .addSerializer(Vector3f.class, new Vector3fSerializer(Vector3f.class)))
        .registerModule(new SimpleModule()
            .addSerializer(Quaternion.class, new QuaternionSerializer(Quaternion.class)));
  }

  public static <T> T loadFile(String file, Class<T> clazz) {
    return loadFile(new File(file), clazz);
  }

  public static <T> T loadFile(File file, Class<T> clazz) {

    try {
      return objectMapper.readValue(file, clazz);
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public static void saveFile(String file, Object object) {
    saveFile(new File(file), object);
  }

  public static void saveFile(File file, Object object) {
    try {
      objectMapper.writeValue(file, object);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
