package org.xun.xuncore.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xun.xuncore.reflect.ClassHandler;

/**
 * Default Settings.
 *
 * @author Jeky
 */
public class DefaultSettings {

    public static final String PORT = "PORT";
    public static final String TEMPLATE_PATH = "TEMPLATE_PATH";
    public static final String STATIC_PATH = "STATIC_PATH";
    public static final String STATIC_URL = "static";

    public static final int DEFAULT_PORT = 8000;
    public static final String DEFAULT_TEMPLATE_PATH = "template";
    public static final String DEFAULT_STATIC_PATH = "static";
    public static final String DEFAULT_STATIC_URL = "static";

    public DefaultSettings() {
        classHandlers = new LinkedList<>();
        settings = new HashMap<>();

        settings.put(PORT, DEFAULT_PORT);
        settings.put(TEMPLATE_PATH, DEFAULT_TEMPLATE_PATH);
        settings.put(STATIC_PATH, DEFAULT_STATIC_PATH);
        settings.put(STATIC_URL, DEFAULT_STATIC_URL);

        initClassHandlers(classHandlers);
        initSettings(settings);
    }

    public void discover(List<Class> classes) {
        // discover all class handlers
        for (Class c : classes) {
            if (ClassHandler.class.isAssignableFrom(c) && !c.equals(ClassHandler.class)) {
                LOGGER.log(Level.INFO, "Class Handlers: " + c);
                try {
                    classHandlers.add((ClassHandler) c.newInstance());
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new SettingsException("Cannot initalize class handler: " + c, ex);
                }
            }
        }

        // use class handlers to handle classes
        try {
            for (Class c : classes) {
                for (ClassHandler handler : classHandlers) {
                    if (handler.canHandler(c)) {
                        handler.handleClass(c);
                    }
                }
            }
        } catch (java.lang.NoClassDefFoundError e) {
        }
    }

    protected void initClassHandlers(List<ClassHandler> classHandlers) {
    }

    protected void initSettings(Map<String, Object> settings) {
    }

    public final String getBaseDir() {
        return Paths.get(".").toAbsolutePath().normalize().toString();
    }

    public void addClassHandler(ClassHandler handler) {
        classHandlers.add(handler);
    }

    public void setSetting(String key, Object value) {
        settings.put(key, value);
    }

    public <T> T getSettingValue(String key, Class<T> cls) {
        return (T) settings.get(key);
    }

    public Object getSettingValue(String key) {
        return settings.get(key);
    }

    public boolean containsKey(String key) {
        return settings.containsKey(key);
    }

    public static List<Class> scanClasses() {
        List<Class> classes = new LinkedList<>();

        try {
            ClassPath path = ClassPath.from(Thread.currentThread().getContextClassLoader());
            ImmutableSet<ClassPath.ClassInfo> classInfos = path.getAllClasses();

            for (ClassPath.ClassInfo c : classInfos) {
                try {
                    classes.add(c.load());
                } catch (java.lang.NoClassDefFoundError e) {
                }
            }
        } catch (IOException e) {
            throw new SettingsException("Cannot scan classes", e);
        }

        return classes;
    }

    public static DefaultSettings getSettings() {
        if (SETTINGS != null) {
            return SETTINGS;
        }

        List<Class> classes = scanClasses();

        try {
            Class<?> customizedSettingsCls = null;
            for (Class c : classes) {
                if (c.equals(DefaultSettings.class)) {
                    continue;
                }

                if (DefaultSettings.class.isAssignableFrom(c)) {
                    if (customizedSettingsCls != null) {
                        throw new SettingsException("Found more than one user defined settings: " + c + ", " + customizedSettingsCls);
                    }
                    customizedSettingsCls = c;
                }
            }
            if (customizedSettingsCls == null) {
                SETTINGS = (DefaultSettings) DefaultSettings.class.newInstance();
            } else {
                SETTINGS = (DefaultSettings) customizedSettingsCls.newInstance();
            }

            SETTINGS.discover(classes);
            return SETTINGS;
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SettingsException("Cannot initalize settings instance.", ex);
        }
    }

    private static DefaultSettings SETTINGS;
    public final List<ClassHandler> classHandlers;
    private final Map<String, Object> settings;
    private static final Logger LOGGER = Logger.getLogger(DefaultSettings.class.getName());
}
