package org.xun.xundemo;

import org.xun.xuncore.views.View;
import org.xun.xuncore.views.renderers.RenderJSP;

/**
 *
 * @author Jeky
 */
public class TestJSP {

    @View(urlPattern = "^jsp$")
    @RenderJSP("/templates/asdf.jsp")
    public String jsp() {
        return "aaa";
    }
}
