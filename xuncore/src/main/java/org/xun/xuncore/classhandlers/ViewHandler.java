package org.xun.xuncore.classhandlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuncore.core.SettingsException;
import org.xun.xuncore.reflect.BeanClass;
import org.xun.xuncore.reflect.BeanMethod;
import org.xun.xuncore.reflect.ClassHandler;
import org.xun.xuncore.views.View;

/**
 *
 * @author Jeky
 */
public class ViewHandler implements ClassHandler {

    public static final String VIEWS = "VIEWS";

    public ViewHandler() {
        DefaultSettings settings = DefaultSettings.getSettings();
        if (!settings.containsKey(VIEWS)) {
            settings.setSetting(VIEWS, new HashMap<Pattern, List<BeanMethod>>());
        }
        this.views = (Map<Pattern, List<BeanMethod>>) settings.getSettingValue(VIEWS);
    }

    @Override
    public boolean canHandler(Class c) {
        return true;
    }

    @Override
    public void handleClass(Class c) throws SettingsException {
        if (c.getName().startsWith("org.xun")) {
            try {
                for (Method m : c.getDeclaredMethods()) {
                    for (Annotation a : m.getAnnotations()) {
                        System.out.println("Find annotation on " + m + ": " + a);
                        if (a.annotationType().equals(View.class)) {
                            View v = (View) a;
                            Pattern p = Pattern.compile(v.urlPattern());
                            if (!views.containsKey(p)) {
                                views.put(p, new LinkedList<>());
                            }
                            views.get(p).add(new BeanMethod(new BeanClass(c), m));
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
//        BeanClass<?> obj = new BeanClass(c);
//        try {
//            for (BeanMethod m : obj.getMethods()) {
//                List<View> viewList = m.getAnnotationList(View.class);
//                for (View v : viewList) {
//                    System.out.println("checking method: " + m);
//                    System.out.println(v);
//                    Pattern p = Pattern.compile(v.urlPattern());
//                    if (!views.containsKey(p)) {
//                        views.put(p, new LinkedList<>());
//                    }
//
//                    views.get(p).add(m);
//                }
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        } 
    }

    private Map<Pattern, List<BeanMethod>> views;

}
