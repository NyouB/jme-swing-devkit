package com.jayfella.importer.plugin;

import com.jayfella.importer.service.PluginService;
import com.jayfella.importer.service.ServiceManager;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * The PluginLogger class is a modified {@link Logger} that prepends all
 * logging calls with the name of the plugin doing the logging. The API for
 * PluginLogger is exactly the same as {@link Logger}.
 *
 * @see Logger
 */
public class PluginLogger extends Logger {

    private final String pluginName;

    /**
     * Creates a new PluginLogger that extracts the name from a plugin.
     *
     * @param context A reference to the plugin
     */
    public PluginLogger(DevKitPlugin context) {
        super(context.getClass().getCanonicalName(), null);
        String prefix = context.getConfiguration().getPrefix();
        pluginName = prefix != null ? "[" + prefix + "] " : "[" + context.getConfiguration().getId() + "] ";
        setParent(ServiceManager.getService(PluginService.class).getLogger());
        setLevel(Level.ALL);
    }

    @Override
    public void log(LogRecord logRecord) {
        logRecord.setMessage(pluginName + logRecord.getMessage());
        super.log(logRecord);
    }

}
