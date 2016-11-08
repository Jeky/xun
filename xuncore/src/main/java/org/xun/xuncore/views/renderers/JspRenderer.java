package org.xun.xuncore.views.renderers;

import java.util.Map;

/**
 *
 * @author Jeky
 */
public class JspRenderer extends Renderer {

    public JspRenderer(Map<String, Object> parameters) {
        this.path = (String) parameters.get("value");
    }

    @Override
    public void render(Object result) {
        response.renderJsp(result.toString(), path);
    }

    private final String path;
}
