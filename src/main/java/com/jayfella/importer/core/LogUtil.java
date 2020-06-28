package com.jayfella.importer.core;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.PatternLayout;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.Arrays;

/**
 * Log utility to initialize logging and set logger levels.
 */
public class LogUtil {

    public static void initializeLogger(Level level) {
        initializeLogger(level, false);
    }

    public static void initializeLogger(Level level, boolean removeHandlers) {
        initializeLogger(level, removeHandlers, "%d{dd MMM yyyy HH:mm:ss} [ %p | %c{1} ] %m%n");
    }

    public static void initializeLogger(Level level, boolean removeHandlers, String pattern) {

        if (removeHandlers) {
            SLF4JBridgeHandler.removeHandlersForRootLogger();
        }

        SLF4JBridgeHandler.install();

        ConsoleAppender console = new ConsoleAppender(new PatternLayout(pattern));
        console.setThreshold(level);
        console.activateOptions();
        org.apache.log4j.Logger.getRootLogger().addAppender(console);
    }

    public static void setLevel(String pkg, Level level) {
        LogManager.getLogger(pkg).setLevel(level);
    }

    public static void setLevel(String[] packages, Level level) {
        Arrays.stream(packages).forEach(p -> LogManager.getLogger(p).setLevel(level));
    }

}
