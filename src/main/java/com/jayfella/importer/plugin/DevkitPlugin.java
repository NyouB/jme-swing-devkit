package com.jayfella.importer.plugin;

import com.jayfella.importer.plugin.configuration.PluginConfiguration;
import com.jayfella.importer.service.PluginService;
import com.jayfella.importer.service.ServiceManager;

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

    public PluginLogger getLogger() {
        return logger;
    }

}
