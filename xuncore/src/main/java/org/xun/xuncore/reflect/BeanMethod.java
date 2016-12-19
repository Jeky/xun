package org.xun.xuncore.reflect;

import java.lang.annotation.Annotation;
import java.lang.invoke.*;
import java.lang.reflect.*;
import java.util.*;
import org.xun.xuncore.core.SettingsException;

/**
 *
 * @author Jeky
 */
public class BeanMethod {

    public BeanMethod(BeanClass obj, Method m) {
        this.obj = obj;
        this.m = m;

        initArgNames();
        initDecorators();
    }

    public Object invoke(LocalVariables vars, Object... args) {
        try {
            if (this.handler == null) {
                this.handler = MethodHandles.lookup().unreflect(m);
            }
            for (int i = 0; i < decorators.size(); i++) {
                decorators.get(i).pre(vars, args);
            }
            Object result = handler.bindTo(obj.getBean()).invokeWithArguments(args);
            for (int i = decorators.size() - 1; i >= 0; i--) {
                result = decorators.get(i).post(vars, result);
            }
            return result;
        } catch (Throwable ex) {
            throw new SettingsException("Error when invoking bean method: " + this, ex);
        }
    }

    public <A extends Annotation> List<A> getAnnotationList(Class<A> c) {
        return Arrays.asList(m.getAnnotationsByType(c));
    }

    public <A extends Annotation> A getAnnotation(Class<A> c) {
        return m.getDeclaredAnnotation(c);
    }

    public BeanClass getOwner() {
        return obj;
    }

    public boolean matchArgTypes(Class... argTypes) {
        Class<?>[] parameterTypes = m.getParameterTypes();
        if (parameterTypes.length != argTypes.length) {
            return false;
        }
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!parameterTypes[i].equals(argTypes[i])) {
                return false;
            }
        }
        return true;
    }

    public boolean matchArgNames(Set<String> keySet) {
        return hasArgNames && args.keySet().equals(keySet);
    }

    public List<String> getArgNames() {
        return argNames;
    }

    public Class getArgType(String argName) {
        return args.get(argName);
    }

    public Method getMethod() {
        return m;
    }

    @Override
    public String toString() {
        if (hasArgNames) {
            return getOwner().getClass().toString() + "." + m.getName() + "(" + String.join(", ", argNames) + ")";
        } else {
            return m.toString();
        }
    }

    private void initArgNames() {
        this.args = new HashMap<>();
        this.argNames = new LinkedList<>();
        this.hasArgNames = true;

        for (Parameter p : m.getParameters()) {
            Param pname = p.getAnnotation(Param.class);
            if (pname == null) { // get parameter names failed
                args.clear();
                hasArgNames = false;
                return;
            } else {
                args.put(pname.value(), p.getType());
                argNames.add(pname.value());
            }
        }
    }

    private void addDecorator(Class dc) {
        try {
            decorators.add((Decorator) dc.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SettingsException("Cannot initialize Decorator: " + dc, ex);
        }
    }

    private void addDecorator(Class dc, Map<String, Object> parameter) {
        try {
            for(Constructor constructor: dc.getConstructors()){
                if(constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].equals(Map.class)){
                    decorators.add((Decorator) constructor.newInstance(parameter));
                    return;
                }
            }
            decorators.add((Decorator) dc.newInstance());
        } catch (Exception ex) {
            throw new SettingsException("Cannot initialize Decorator: " + dc, ex);
        }
    }

    private void initDecorators() {
        this.decorators = new ArrayList<>();

        for (Annotation anno : m.getAnnotations()) {
            // check if annotated by decorating
            Decorating decor = anno.annotationType().getDeclaredAnnotation(Decorating.class);
            if (decor != null) {
                Map<String, Object> parameters = new HashMap<>();
                for (Method m : anno.annotationType().getDeclaredMethods()) {
                    try {
                        parameters.put(m.getName(), m.invoke(anno));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        throw new SettingsException("Cannot get method value from " + m, ex);
                    }
                }
                for (Class dc : decor.value()) {
                    addDecorator(dc, parameters);
                }
            }

            // check directly decorating
            if (anno.annotationType().equals(Decorating.class)) {
                for (Class dc : ((Decorating) anno).value()) {
                    addDecorator(dc);
                }
            }

        }
    }

    private final BeanClass obj;
    private final Method m;
    private List<String> argNames;
    private Map<String, Class> args;
    private boolean hasArgNames;
    private MethodHandle handler;
    private ArrayList<Decorator> decorators;
}
