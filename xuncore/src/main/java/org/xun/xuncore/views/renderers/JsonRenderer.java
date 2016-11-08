
package org.xun.xuncore.views.renderers;

/**
 *
 * @author Jeky
 */
public class JsonRenderer extends Renderer{

    @Override
    public void render(Object result) {
        response.renderJson(result);
    }
    
}
