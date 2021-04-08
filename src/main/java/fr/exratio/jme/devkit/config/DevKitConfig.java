package fr.exratio.jme.devkit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValueFactory;
import fr.exratio.jme.devkit.core.json.ColorRGBASerializer;
import fr.exratio.jme.devkit.core.json.QuaternionSerializer;
import fr.exratio.jme.devkit.core.json.Vector3fSerializer;
import fr.exratio.jme.devkit.main.MainPage.Zone;
import fr.exratio.jme.devkit.tool.Tool;
import fr.exratio.jme.devkit.tool.ViewMode;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DevKitConfig {

  public static final String SDK_CONFIG = "sdkConfig";

  public static final String TOOL_CONFIG = "tools";
  public static final String MAIN_SIZE = "mainSize";
  public static final String VIEWMODE = "viewMode";
  public static final String ZONE = "zone";
  public static final String TOOL_DISPLAYED = "isDisplayes";
  public static final String CAMERA = "camera";
  public static final String DIMENSION = "dimension";
  public static final String VIEWPORT = "viewport";
  public static final String COLOR = "color";
  public static final String RED = "r";
  public static final String GREEN = "g";
  public static final String BLUE = "b";
  public static final String ALPHA = "a";
  public static final String WIDTH = "width";
  public static final String HEIGHT = "height";
  public static final String FIELD_OF_VIEW = "fieldOfView";
  public static final String FRUSTUM = "frustum";
  public static final String NEAR = "near";
  public static final String FAR = "far";
  public static final String PROJECT = "project";
  public static final String LOCATION = "location";
  public static final String ASSET_DIRECTORY = "assetDirectory";
  public static final String SIZE = "size";
  public static final String GRID = "grid";
  public static final String TITLE = "title";
  public static final char DOT = '.';
  public static final char X = 'x';
  public static final char Y = 'y';
  public static final char Z = 'z';
  public static final String CAMERA_WIDTH = CAMERA + DOT + DIMENSION + DOT + WIDTH;
  public static final String CAMERA_HEIGHT = CAMERA + DOT + DIMENSION + DOT + HEIGHT;
  public static final String VIEWPORT_COLOR = CAMERA + DOT + VIEWPORT + DOT + COLOR;
  public static final String CAMERA_FOV = CAMERA + DOT + FIELD_OF_VIEW;
  public static final String CAMERA_FRUSTUM_NEAR = CAMERA + DOT + FRUSTUM + DOT + NEAR;
  public static final String CAMERA_FRUSTUM_FAR = CAMERA + DOT + FRUSTUM + DOT + FAR;
  public static final String PROJECT_ASSET_DIRECTORY = PROJECT + DOT + ASSET_DIRECTORY;
  public static final String GRID_SIZE = GRID + DOT + SIZE;
  public static final String GRID_SIZE_X = GRID_SIZE + DOT + X;
  public static final String GRID_SIZE_Y = GRID_SIZE + DOT + Y;
  public static final String GRID_SIZE_Z = GRID_SIZE + DOT + Z;
  public static final String GRID_COLOR = GRID + DOT + COLOR;
  public static final String GRID_SHOW = GRID + DOT + "show";
  public static final String GRID_LOCATION = GRID + DOT + "location";
  public static final String THEME = SDK_CONFIG + DOT + "theme";
  public static final String CAMERA_ROTATION_WIDGET = SDK_CONFIG + DOT + "showCamRotationWidget";
  public static final String DEBUG_LIGHT_WINDOW = SDK_CONFIG + DOT + "showDebugLightsWindow";
  public static final String APP_STATE_WINDOW = SDK_CONFIG + DOT + "showRunAppStateWindow";
  public static final String DEFAULT_MATERIAL = SDK_CONFIG + DOT + "defaultMaterial";
  public static final String MAIN_SIZE_X = MAIN_SIZE + DOT + X;
  public static final String MAIN_SIZE_Y = MAIN_SIZE + DOT + Y;
  public static final String MAIN_SIZE_WIDTH = MAIN_SIZE + DOT + WIDTH;
  public static final String MAIN_SIZE_HEIGHT = MAIN_SIZE + DOT + HEIGHT;
  public static final String LOCATION_X = LOCATION + DOT + X;
  public static final String LOCATION_Y = LOCATION + DOT + Y;


  // DEFAULT CAMERA VALUE
  public static final ColorRGBA DEFAULT_VIEWPORT_COLOR = new ColorRGBA(0.5f, 0.6f, 0.7f, 1.0f);
  public static final float DEFAULT_FIELD_OF_VIEW = 45.0f;
  public static final float DEFAULT_FRUSTUM_NEAR = 0.1f;
  public static final float DEFAULT_FRUSTUM_FAR = 1000.0f;
  public static final Dimension DEFAULT_CAMERA_DIMENSION = new Dimension(600, 480);

  //DEFAULT SCENE VALUE
  public static final boolean DEFAULT_SHOW_GRID = true;
  public static final Vector3f DEFAULT_GRID_SIZE = new Vector3f(200, 200,
      1.0f); // size x, y, lineWidth
  public static final ColorRGBA DEFAULT_GRID_COLOR = new ColorRGBA(.75f, 0.75f, 0.75f, 1.0f);
  public static final Vector3f DEFAULT_GRID_LOCATION = new Vector3f(-100, 0, -100);

  public static final File storageDir = new File("devkit");
  public static final File pluginStorageDir = Paths.get(storageDir.toString(), "plugins").toFile();
  private static final Logger log = Logger.getLogger(DevKitConfig.class.getName());
  private static final File configFile = Paths.get(storageDir.toString(), "devkit-config.json")
      .toFile();
  private static final ObjectMapper objectMapper = new ObjectMapper()
      .enable(SerializationFeature.INDENT_OUTPUT)
      .registerModule(new SimpleModule()
          .addSerializer(ColorRGBA.class, new ColorRGBASerializer(ColorRGBA.class)))
      .registerModule(
          new SimpleModule().addSerializer(Vector3f.class, new Vector3fSerializer(Vector3f.class)))
      .registerModule(new SimpleModule()
          .addSerializer(Quaternion.class, new QuaternionSerializer(Quaternion.class)));
  private static DevKitConfig INSTANCE;
  private Config conf = ConfigFactory.load();

  private DevKitConfig() {
  }

  public static DevKitConfig getInstance() {
    if (INSTANCE == null) {
     /* // Create the storage directory.
      if (!storageDir.exists()) {

        log.info("Creating devkit storage directory: " + storageDir);
        boolean created = storageDir.mkdir();

        if (!created) {
          throw new RuntimeException("Unable to create config directory. Exiting.");
        }
      }

      // create or load the configuration file.
      if (!configFile.exists()) {

        log.info("Creating configuration file: " + configFile);

        INSTANCE = new DevKitConfig();
        INSTANCE.save();
      } else {
        log.info("Reading configuration file: " + configFile);

        try {
          INSTANCE = objectMapper.readValue(configFile, DevKitConfig.class);
        } catch (IOException e) {
          // we can't continue if we can't read the configuration file.
          throw new RuntimeException(e);
        }

        // we save the configuration because new areas may have been added since the last save.
        if (INSTANCE != null) {
          INSTANCE.save();
        }

      }

      // create the plugins storage dir.
      if (!pluginStorageDir.exists()) {
        log.info("Creating plugins storage directory: " + pluginStorageDir);
        boolean created = pluginStorageDir.mkdirs();

        if (!created) {
          throw new RuntimeException("Unable to create plugins storage directory. Exiting.");
        }
      }
*/
      INSTANCE = new DevKitConfig();
    }

    return INSTANCE;
  }

  public void save() {

    log.finer("Saving configuration: " + configFile);

    try {
      objectMapper.writeValue(configFile, this);
      log.finer("Saved configuration: " + configFile);
    } catch (IOException ex) {
      log.log(Level.SEVERE, "Unable to write file: " + configFile, ex);
    }

  }

  public String getTheme() {
    return conf.getString(THEME);
  }

  public void setTheme(String theme) {
    conf = conf.withValue(THEME, ConfigValueFactory.fromAnyRef(theme));
  }

  public Dimension getSize() {
    return new Dimension(conf.getInt( MAIN_SIZE_WIDTH),
        conf.getInt(MAIN_SIZE_HEIGHT));
  }

  public void setSize(Dimension dimension) {
    conf = conf.withValue(MAIN_SIZE_WIDTH, ConfigValueFactory.fromAnyRef(dimension.width));
    conf = conf.withValue(MAIN_SIZE_HEIGHT, ConfigValueFactory.fromAnyRef(dimension.height));
  }

  public Point getLocation() {
    return new Point(conf.getInt(LOCATION_X),
        conf.getInt(LOCATION_Y));
  }

  public void setLocation(Point location) {
    conf = conf.withValue(LOCATION_X, ConfigValueFactory.fromAnyRef(location.x));
    conf = conf.withValue(LOCATION_Y, ConfigValueFactory.fromAnyRef(location.y));
  }

  public boolean isShowCamRotationWidget() {
    return conf.getBoolean(CAMERA_ROTATION_WIDGET);
  }

  public void setShowCamRotationWidget(boolean showCamRotationWidget) {
    conf = conf
        .withValue(CAMERA_ROTATION_WIDGET, ConfigValueFactory.fromAnyRef(showCamRotationWidget));
  }

  public boolean isShowDebugLightsWindow() {
    return conf.getBoolean(DEBUG_LIGHT_WINDOW);
  }

  public void setShowDebugLightsWindow(boolean showDebugLightsWindow) {
    conf = conf.withValue(DEBUG_LIGHT_WINDOW, ConfigValueFactory.fromAnyRef(showDebugLightsWindow));
  }

  public boolean isShowRunAppStateWindow() {
    return conf.getBoolean(APP_STATE_WINDOW);
  }

  public void setShowRunAppStateWindow(boolean showRunAppStateWindow) {
    conf = conf.withValue(APP_STATE_WINDOW, ConfigValueFactory.fromAnyRef(showRunAppStateWindow));
  }

  public String getDefaultMaterial() {
    return conf.getString(DEFAULT_MATERIAL);
  }

  public void setDefaultMaterial(String defaultMaterial) {
    conf = conf.withValue(DEFAULT_MATERIAL, ConfigValueFactory.fromAnyRef(defaultMaterial));
  }

  public void setToolConfiguration(Tool tool) {
    ToolConfiguration toolConfiguration = ToolConfigurationMapper.INSTANCE
        .toolToToolConfiguration(tool);
    String confPath = TOOL_CONFIG + DOT + tool.getId();
    conf = conf.withValue(confPath + VIEWMODE,
        ConfigValueFactory
            .fromAnyRef(toolConfiguration.viewMode));
    conf = conf.withValue(confPath + ZONE,
        ConfigValueFactory
            .fromAnyRef(toolConfiguration.zone));
    conf = conf.withValue(confPath + TOOL_DISPLAYED,
        ConfigValueFactory
            .fromAnyRef(toolConfiguration.isDisplayed));
  }

  public void setToolConfiguration(Collection<Tool> tools) {

    for (Tool tool : tools) {
      setToolConfiguration(tool);
    }
  }

  public String getTitle() {
    return conf.getString(TITLE);
  }


  @Getter
  @Setter
  @NoArgsConstructor
  public static class ToolConfiguration {

    ViewMode viewMode;
    boolean isDisplayed;
    Zone zone;
    int order;
  }

  @Mapper
  public interface ToolConfigurationMapper {

    ToolConfigurationMapper INSTANCE = Mappers.getMapper(ToolConfigurationMapper.class);

    ToolConfiguration toolToToolConfiguration(Tool tool);

    Tool toolConfigurationToTool(ToolConfiguration toolConfiguration);
  }

//CAMERA CONFIG

  public ColorRGBA getViewportColor() {
    return getColor(VIEWPORT_COLOR);
  }

  public void setViewportColor(ColorRGBA viewportColor) {
    setColor(viewportColor, VIEWPORT_COLOR);
  }

  public float getFieldOfView() {
    return (float) conf.getDouble(CAMERA_FOV);
  }

  public void setFieldOfView(float fieldOfView) {
    conf = conf.withValue(CAMERA_FOV,
        ConfigValueFactory
            .fromAnyRef(fieldOfView));
  }

  public float getFrustumNear() {
    return (float) conf.getDouble(CAMERA_FRUSTUM_NEAR);
  }

  public void setFrustumNear(float frustumNear) {
    conf = conf.withValue(CAMERA_FRUSTUM_NEAR,
        ConfigValueFactory
            .fromAnyRef(frustumNear));
  }

  public float getFrustumFar() {
    return (float) conf.getDouble(CAMERA_FRUSTUM_FAR);
  }

  public void setFrustumFar(float frustumFar) {
    conf = conf.withValue(CAMERA_FRUSTUM_NEAR,
        ConfigValueFactory
            .fromAnyRef(frustumFar));
  }

  public Dimension getCameraDimension() {
    if (conf.hasPath(CAMERA_WIDTH) && conf.hasPath(CAMERA_HEIGHT)) {
      return new Dimension(conf.getInt(CAMERA_WIDTH),
          conf.getInt(CAMERA_HEIGHT));
    }
    return DEFAULT_CAMERA_DIMENSION;
  }

  public void setCameraDimension(Dimension cameraDimension) {
    setCameraDimension(cameraDimension.width, cameraDimension.height);
  }

  public void setCameraDimension(int width, int height) {
    conf = conf.withValue(CAMERA_WIDTH,
        ConfigValueFactory
            .fromAnyRef(width));
    conf = conf.withValue(CAMERA_HEIGHT,
        ConfigValueFactory
            .fromAnyRef(height));
  }

  public String getAssetRootDir() {
    return conf.getString(PROJECT_ASSET_DIRECTORY);
  }

  public void setAssetRootDir(String assetRootDir) {
    conf = conf.withValue(PROJECT_ASSET_DIRECTORY,
        ConfigValueFactory
            .fromAnyRef(assetRootDir));
  }

  public boolean isShowGrid() {
    return conf.getBoolean(GRID_SHOW);
  }

  public void setShowGrid(boolean showGrid) {
    conf = conf.withValue(GRID_SHOW, ConfigValueFactory
        .fromAnyRef(showGrid));
  }

  public Vector3f getGridSize() {
    return getVector3f(GRID_SIZE);
  }

  public void setGridSize(Vector3f vector3f) {
    setVector3f(vector3f, GRID_SIZE);
  }

  public ColorRGBA getGridColor() {
    return getColor(GRID_COLOR);
  }

  public void setGridColor(ColorRGBA gridColor) {
    setColor(gridColor, GRID_COLOR);
  }

  public Vector3f getGridLocation() {
    return getVector3f(GRID_LOCATION);
  }

  public void setGridLocation(Vector3f gridLocation) {
    setVector3f(gridLocation, GRID_LOCATION);
  }

  private void setColor(ColorRGBA color, String name) {
    conf = conf.withValue(name + DOT + RED,
        ConfigValueFactory
            .fromAnyRef(color.r));
    conf = conf.withValue(name + DOT + GREEN,
        ConfigValueFactory
            .fromAnyRef(color.g));
    conf = conf.withValue(name + DOT + BLUE,
        ConfigValueFactory
            .fromAnyRef(color.b));
    conf = conf.withValue(name + DOT + ALPHA,
        ConfigValueFactory
            .fromAnyRef(color.a));
  }

  private ColorRGBA getColor(String name) {
    return new ColorRGBA((float) conf.getDouble(name + DOT + RED),
        (float) conf.getDouble(name + DOT + GREEN),
        (float) conf.getDouble(name + DOT + BLUE),
        (float) conf.getDouble(name + DOT + ALPHA)
    );
  }

  private void setVector3f(Vector3f vector, String name) {
    conf = conf.withValue(name + DOT + X,
        ConfigValueFactory
            .fromAnyRef(vector.x));
    conf = conf.withValue(name + DOT + Y,
        ConfigValueFactory
            .fromAnyRef(vector.y));
    conf = conf.withValue(name + DOT + Z,
        ConfigValueFactory
            .fromAnyRef(vector.z));
  }

  private Vector3f getVector3f(String name) {
    return new Vector3f((float) conf.getDouble(name + DOT + X),
        (float) conf.getDouble(name + DOT + Y),
        (float) conf.getDouble(name + DOT + Z));
  }

  @NoArgsConstructor
  public static class PathBuilder implements Builder<String> {

    private StringBuilder currentString = new StringBuilder();

    public PathBuilder(String str) {
      currentString = new StringBuilder(str);
    }

    public PathBuilder append(String str) {
      currentString = currentString.append(DOT).append(str);
      return this;
    }

    @Override
    public String build() {
      return currentString.toString();
    }
  }
}
