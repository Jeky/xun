package org.xun.xuncore.core;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.TypeToken;
import java.io.IOException;
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

    public static final int DEFAULT_PORT = 8000;

    public DefaultSettings() {
        classHandlers = new LinkedList<>();
        settings = new HashMap<>();

        settings.put(PORT, DEFAULT_PORT);

        initClassHandlers(classHandlers);
        initSettings(settings);
    }

    public void discover(List<Class> classes) {
        // discover all class handlers
        for (Class c : classes) {
            if (ClassHandler.class.isAssignableFrom(c) && !c.equals(ClassHandler.class)) {
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

        System.out.println(classHandlers);
    }

    protected void initClassHandlers(List<ClassHandler> classHandlers) {
    }

    protected void initSettings(Map<String, Object> settings) {
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

//
//    public static DefaultSettings getSettings() {
//        return SettingsHolder.INSTANCE;
//    }
//
//    private DefaultSettings() {
//        classHandlers = new LinkedList<>();
//
//        views = new HashMap<>();
//        classHandlers.add(new ViewHandler(views));
//
//        parameterConverters = new HashMap<>();
//        classHandlers.add(new ConverterHandler(parameterConverters));
////
////        templateFilters = new HashMap<>();
////        classHandlers.add(new TemplateFilterHandler(templateFilters));
////
////        templateTags = new HashMap<>();
////        classHandlers.add(new TemplateTagHandler(templateTags));
//    }
//
//    public Set<Entry<Pattern, List<BeanMethod>>> allViews() {
//        return views.entrySet();
//    }
//    public Set<Entry<String, TemplateFilter>> allFilters() {
//        return templateFilters.entrySet();
//    }
//    public ParameterConverter getConverter(Class c) {
//        return parameterConverters.get(c);
//    }
//
//    public TemplateFilter getTemplateFilter(String name) {
//        return templateFilters.get(name);
//    }
//
//    public TemplateTag getTemplateTag(String name) {
//        return templateTags.get(name);
//    }
//
//    private static class SettingsHolder {
//
//        private static final DefaultSettings INSTANCE = new DefaultSettings();
//    }
//
//    private final Map<Pattern, List<BeanMethod>> views;
//    private final Map<Class, ParameterConverter> parameterConverters;
//    private final Map<String, TemplateFilter> templateFilters;
//    private final Map<String, TemplateTag> templateTags;
    private final List<ClassHandler> classHandlers;
    private final Map<String, Object> settings;

}
