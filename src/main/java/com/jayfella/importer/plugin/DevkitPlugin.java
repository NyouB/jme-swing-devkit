package com.jayfella.importer.plugin;

import com.jayfella.importer.config.DevKitConfig;
import com.jayfella.importer.plugin.configuration.PluginConfiguration;
import com.jayfella.importer.service.PluginService;
import com.jayfella.importer.service.ServiceManager;

import java.io.File;
import java.nio.file.Paths;

public abstract class DevkitPlugin {

    protected PluginConfiguration pluginConfiguration = new PluginConfiguration();
    private boolean enabled = false;
    private PluginLogger logger;

    public DevkitPlugin() {

    }

    /**
     * Called after the plugin has been constructed.
     */
    public void initialize() throws Exception {
        this.logger = new PluginLogger(this);

        File storageDir = Paths.get(DevKitConfig.pluginStorageDir.toString(), getConfiguration().getId()).toFile();
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
     * @return whether or not the plugin is enabled.
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets the plugin to enabled/disabled. Called internally.
     * @param enabled sets the plugin as enabled or disabled.
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns the configuration of the plugin.
     * @return the configuration of the plugin.
     */
    public PluginConfiguration getConfiguration() {
        return pluginConfiguration;
    }

    public DevkitPlugin[] getDependencies() {

        DevkitPlugin[] dependencies = new DevkitPlugin[pluginConfiguration.getDependencies().length];

        PluginService pluginService = ServiceManager.getService(PluginService.class);

        for (int i = 0; i < dependencies.length; i++) {
            DevkitPlugin plugin = pluginService.getPlugin(pluginConfiguration.getDependencies()[i]);
            dependencies[i] = plugin;
        }

        return dependencies;

    }

    /**
     * Returns the directory used to store any data for the plugin.
     * @return the directory used to store any data for the plugin.
     */
    public File getStorageDir() {
        return Paths.get(DevKitConfig.pluginStorageDir.toString(), getConfiguration().getId()).toFile();
    }

    public PluginLogger getLogger() {
        return logger;
    }

}
