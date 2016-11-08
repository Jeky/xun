package org.xun.xuncore.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.xun.xuncore.core.SettingsException;

/**
 *
 * @author Jeky
 */
public class BeanClass<T> {

    public BeanClass(Class<T> targetClass) {
        this.cls = targetClass;
    }

    public <A extends Annotation> List<A> getAnnotationList(Class<A> c) {
        return Arrays.asList(cls.getAnnotationsByType(c));
    }

    public <A extends Annotation> A getAnnotation(Class<A> c) {
        return cls.getDeclaredAnnotation(c);
    }

    public BeanMethod getMethod(String name, Class... argTypes)
        throws NoSuchMethodException {
        Method m = cls.getDeclaredMethod(name, argTypes);
        return new BeanMethod(this, m);
    }

    public List<BeanMethod> getMethods() {
        List<BeanMethod> methods = new ArrayList<>();
        for (Method m : cls.getDeclaredMethods()) {
            methods.add(new BeanMethod(this, m));
        }
        return methods;
    }

    public T newInstance() throws InstantiationException,
                                  IllegalAccessException {
        return cls.newInstance();
    }

    public T getBean() {
        if (cls.getAnnotation(Singleton.class) == null) { // not a singleton
            try {
                return newInstance();
            } catch (InstantiationException | IllegalAccessException ex) {
                throw new SettingsException("Cannot initialize instance: " + cls, ex);
            }
        } else {
            if (bean == null) {
                try {
                    bean = newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new SettingsException("Cannot initialize instance: " + cls, ex);
                }
            }
            return bean;
        }
    }

    private T bean;
    private final Class<T> cls;

}
