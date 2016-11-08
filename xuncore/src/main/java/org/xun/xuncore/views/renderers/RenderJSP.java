package org.xun.xuncore.views.renderers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.xun.xuncore.reflect.Decorating;

/**
 *
 * @author Jeky
 */
@Decorating({JspRenderer.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface RenderJSP {

    String value();
}
