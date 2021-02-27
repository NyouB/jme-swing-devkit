package fr.exratio.jme.devkit.plugin;

import fr.exratio.jme.devkit.config.DevKitConfig;
import fr.exratio.jme.devkit.plugin.configuration.PluginConfiguration;
import fr.exratio.jme.devkit.service.PluginService;
import fr.exratio.jme.devkit.service.ServiceManager;
import java.io.File;
import java.nio.file.Paths;

public abstract class DevKitPlugin {

  protected PluginConfiguration pluginConfiguration = new PluginConfiguration();
  private boolean enabled = false;
  private PluginLogger logger;

  public DevKitPlugin() {

  }

  /**
   * Called after the plugin has been constructed.
   */
  public void initialize() throws Exception {
    this.logger = new PluginLogger(this);

    File storageDir = Paths
        .get(DevKitConfig.pluginStorageDir.toString(), getConfiguration().getId()).toFile();
    if (!storageDir.exists()) {
      boolean created = storageDir.mkdirs();

      if (!created) {
        throw new RuntimeException("Unable to create plugin storage directory. Exiting.");
      }
    }

    onInitialize();
  }

  public abstract void onInitialize() throws Exception;

  /**
   * Called when the plugin has been successfully initialized.
   */
  public abstract void onEnabled();

  /**
   * Returns whether or not the plugin is enabled.
   *
   * @return whether or not the plugin is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Sets the plugin to enabled/disabled. Called internally.
   *
   * @param enabled sets the plugin as enabled or disabled.
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Returns the configuration of the plugin.
   *
   * @return the configuration of the plugin.
   */
  public PluginConfiguration getConfiguration() {
    return pluginConfiguration;
  }

  /**
   * Returns the plugin instances of all plugins depended on, or null if the plugin does not exist.
   *
   * @return the plugin instances of all plugins depended on, or null if the plugin does not exist.
   */
  public DevKitPlugin[] getDependencies() {

    DevKitPlugin[] dependencies = new DevKitPlugin[pluginConfiguration.getDependencies().length];

    PluginService pluginService = ServiceManager.getService(PluginService.class);

    for (int i = 0; i < dependencies.length; i++) {
      DevKitPlugin plugin = pluginService.getPlugin(pluginConfiguration.getDependencies()[i]);
      dependencies[i] = plugin;
    }

    return dependencies;

  }

  /**
   * Returns the plugin instances of all plugins softDepended on, or null if the plugin does not
   * exist.
   *
   * @return the plugin instances of all plugins softDepended on, or null if the plugin does not
   * exist.
   */
  public DevKitPlugin[] getSoftDependencies() {

    DevKitPlugin[] softDependencies = new DevKitPlugin[pluginConfiguration
        .getSoftDependencies().length];

    PluginService pluginService = ServiceManager.getService(PluginService.class);

    for (int i = 0; i < softDependencies.length; i++) {
      DevKitPlugin plugin = pluginService.getPlugin(pluginConfiguration.getDependencies()[i]);
      softDependencies[i] = plugin;
    }

    return softDependencies;

  }

  /**
   * Returns the directory used to store any data for the plugin.
   *
   * @return the directory used to store any data for the plugin.
   */
  public File getStorageDir() {
    return Paths.get(DevKitConfig.pluginStorageDir.toString(), getConfiguration().getId()).toFile();
  }

  public PluginLogger getLogger() {
    return logger;
  }

}
