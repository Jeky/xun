package org.xun.xuncore.reflect;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jeky
 */
public class LocalVariables {

    public LocalVariables() {
        variables = new HashMap<>();
    }

    public void putVariable(String name, Object var) {
        variables.put(name, var);
    }

    public Object getVariable(String name) {
        return variables.get(name);
    }

    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    public <T> T getVariable(String name, Class<T> cls) {
        return (T) variables.get(name);
    }

    @Override
    public String toString() {
        return variables.toString();
    }

    public static final LocalVariables EMPTY = new LocalVariables() {
        @Override
        public final void putVariable(String name, Object var) {
            throw new UnsupportedOperationException("Cannot put variables input EMPTY");
        }

    };

    private Map<String, Object> variables;
}
