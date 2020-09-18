package com.jayfella.devkit.service;

import com.jayfella.devkit.core.DevkitPackages;
import com.jayfella.devkit.plugin.DevKitPlugin;
import com.jayfella.devkit.plugin.configuration.PluginConfiguration;
import com.jayfella.devkit.plugin.exception.InvalidPluginConfigurationException;
import com.jayfella.devkit.plugin.exception.PluginDependencyNotFoundException;
import com.jayfella.devkit.plugin.sorter.PluginSorter;
import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class PluginService implements Service {

    private static final Logger log = Logger.getLogger(PluginService.class.getName());
    private static final Pattern ID_PATTERN = Pattern.compile("^[A-Za-z0-9 _.-]+$");

    private final List<DevKitPlugin> plugins = new ArrayList<>();

    private final long threadId;

    public PluginService() {
        threadId = Thread.currentThread().getId();
    }

    public void loadPlugins() {

        List<DevKitPlugin> loadedPlugins = new ArrayList<>();
        int pluginErrorCount = 0;

        // We're searching through the entire classpath and it will take a while.
        // We could enforce a package to drastically reduce the search time.
        // For example: "plugin.devkit.yourPackageName" and just search "plugin.devkit"

        log.info("Searching for DevKit Plugins...");
        // Reflections reflections = new Reflections(new ConfigurationBuilder().setUrls(ClasspathHelper.forJavaClassPath()));
        Reflections reflections = new Reflections(new ConfigurationBuilder().forPackages(DevkitPackages.Plugin));
        Set<Class<? extends DevKitPlugin>> classes = reflections.getSubTypesOf(DevKitPlugin.class);

        log.info("Found " + classes.size() + " classes extending DevkitPlugin.");

        for (Class<? extends DevKitPlugin> pluginClass : classes) {

            DevKitPlugin plugin = null;

            try {
                Constructor<? extends DevKitPlugin> constructor = pluginClass.getConstructor();
                plugin = constructor.newInstance();
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {

                // let the user know we couldn't create an instance of the plugin.
                log.warning("Unable to create instance of plugin: " + pluginClass.getName());
                e.printStackTrace();
                pluginErrorCount++;

            }

            if (plugin != null) {

                try {

                    // check if the configuration is valid.
                    validatePluginConfiguration(plugin.getConfiguration());

                    // fire the initialize event.
                    // we must initialize now so we can read the dependencies it requires.
                    plugin.initialize();

                    // only add the plugin to the loaded plugins if it successfully initialized.
                    loadedPlugins.add(plugin);

                } catch (Exception e) {
                    // else give the user some feedback on what happened.
                    log.warning("Unable to initialize plugin: " + pluginClass.getName());
                    e.printStackTrace();
                }

            }

        }

        // at this point the plugin is considered "good to go".
        plugins.addAll(loadedPlugins);

        // check dependencies of plugin.
        plugins.removeIf(plugin -> {

            // check for softDependencies
            // this only gives the user a log warning that functionality may be limited.
            checkSoftDependencies(plugin);

            // Check for dependencies that don't exist.
            // This will remove plugins that require a dependency that doesn't exist.
            try {
                checkDependencies(plugin);
            } catch (PluginDependencyNotFoundException e) {
                log.warning(e.getMessage());
                return true;
            }

            return false;
        });

        // Check for cyclic dependencies
        // This can potentially terminate the application if cyclic dependencies exist.
        // The user must resolve the issue or remove the offending plugin.
        PluginSorter.sort(plugins);

        if (plugins.size() > 0) {
            log.info("Loaded " + plugins.size() + " plugins.");
        }
        else {
            log.info("No plugins were loaded.");
        }

        if (pluginErrorCount > 0) {
            log.warning("Unable to load " + pluginErrorCount + " plugins.");
        }
        else if (!plugins.isEmpty() && pluginErrorCount == 0) {
            log.info("All plugins loaded successfully.");
        }

        // fire the onEnable event for all plugins.
        enablePlugins();

    }

    private void validatePluginConfiguration(PluginConfiguration pluginConfiguration) throws InvalidPluginConfigurationException {

        // ID or unique name.
        String id = pluginConfiguration.getId();

        try {

            if (!ID_PATTERN.matcher(id).matches()) {

                throw new InvalidPluginConfigurationException(
                        "Plugin ID '" + id + "' must match pattern '" + ID_PATTERN.pattern() + "'. "
                                + "It must consist of all alphanumeric characters, underscores, hyphen, and period (a-z,A-Z,0-9, _.-)"
                );
            }

            pluginConfiguration.setId(id.replace(' ', '_'));

        } catch (NullPointerException e) {
            throw new InvalidPluginConfigurationException("id is not defined", e);
        } catch (ClassCastException e) {
            throw new InvalidPluginConfigurationException("id is of wrong type", e);
        }

        // version
        String version = pluginConfiguration.getVersion();

        try {
            if (version.trim().isEmpty()) {
                throw new InvalidPluginConfigurationException("Version is not defined.");
            }

            pluginConfiguration.setVersion(version.trim());

        } catch (NullPointerException e) {
            throw new InvalidPluginConfigurationException("Version is not defined.");
        }

    }

    private void checkDependencies(DevKitPlugin plugin) throws PluginDependencyNotFoundException {

        final List<String> dependenciesNotFound = new ArrayList<>();

        for (String strDep : plugin.getConfiguration().getDependencies()) {

            DevKitPlugin dependency = getPlugin(strDep);

            if (dependency == null) {
                dependenciesNotFound.add(strDep);
            }

        }

        if (!dependenciesNotFound.isEmpty()) {

            StringBuilder message = new StringBuilder();

            String depsNotFoundString = String.join(", ", dependenciesNotFound);

            message.append("Plugin '")
                    .append(plugin.getConfiguration().getId())
                    .append("' has been unloaded because it depends on plugin that do not exist: [ ")
                    .append(depsNotFoundString)
                    .append(" ]");

            dependenciesNotFound.clear();

            throw new PluginDependencyNotFoundException(message.toString());
        }

    }

    private void checkSoftDependencies(DevKitPlugin plugin) {

        final List<String> dependenciesNotFound = new ArrayList<>();

        for (String strDep : plugin.getConfiguration().getSoftDependencies()) {

            DevKitPlugin dependency = getPlugin(strDep);

            if (dependency == null) {
                dependenciesNotFound.add(strDep);
            }

        }

        if (!dependenciesNotFound.isEmpty()) {

            StringBuilder message = new StringBuilder();

            String depsNotFoundString = String.join(", ", dependenciesNotFound);

            message.append("Plugin '")
                    .append(plugin.getConfiguration().getId())
                    .append("' may have limited functionality because it soft-depends on plugins that do not exist: [ ")
                    .append(depsNotFoundString)
                    .append(" ]");

            dependenciesNotFound.clear();

            log.warning(message.toString());
        }

    }

    private void enablePlugins() {

        for (DevKitPlugin plugin : plugins) {

            if (!plugin.isEnabled()) {
                try {
                    log.info("Enabling " + plugin.getClass().getSimpleName() + " v" + plugin.getConfiguration().getVersion());
                    plugin.onEnabled();
                    plugin.setEnabled(true);

                } catch (Throwable ex) {
                    log.warning("Error occurred while enabling " + plugin.getClass().getSimpleName());
                    ex.printStackTrace();
                }
            }

        }

    }

    /**
     * Returns the plugin with the given exact name, or null if none was found.
     * @param name the exact name of the plugin.
     * @return the plugin with the given name, or null if none was found.
     */
    public DevKitPlugin getPlugin(String name) {
        return plugins.stream()
                .filter(plugin -> plugin.getConfiguration().getId().equals(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns all currently loaded plugins.
     * @return all currently loaded plugins.
     */
    public DevKitPlugin[] getPlugins() {
        return plugins.toArray(new DevKitPlugin[0]);
    }

    public Logger getLogger() {
        return log;
    }

    @Override
    public long getThreadId() {
        return threadId;
    }

    @Override
    public void stop() {

    }
}
