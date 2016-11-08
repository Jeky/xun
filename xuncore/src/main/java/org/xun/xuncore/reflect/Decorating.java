package org.xun.xuncore.reflect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author Jeky
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Decorating {

    Class[] value();
}
