package org.xun.xuncore.reflect;

import org.xun.xuncore.core.SettingsException;

/**
 *
 * @author Jeky
 */
public interface ClassHandler {

    boolean canHandler(Class c);

    void handleClass(Class c) throws SettingsException;
}
