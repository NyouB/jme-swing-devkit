package com.jayfella.devkit.swing;

import com.github.weisj.darklaf.LafManager;
import com.github.weisj.darklaf.theme.Theme;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class SwingTheme {

    public static void setTheme(String className) {

        try {

            @SuppressWarnings("unchecked")
            Class<? extends Theme> themeClass = (Class<? extends Theme>) Class.forName(className);

            setTheme(themeClass);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void setTheme(Class<? extends Theme> themeClass) {

        try {

            Constructor<? extends Theme> constructor = themeClass.getConstructor();
            Theme theme = constructor.newInstance();

            LafManager.setTheme(theme);
            LafManager.install();

        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

}
