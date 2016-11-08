 package org.xun.xuncore.reflect;

/**
 *
 * @author Jeky
 */
public interface Decorator<T> {

    void pre(LocalVariables vars, Object... args);

    T post(LocalVariables vars, T resultValue);

}
