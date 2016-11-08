
package org.xun.xuncore.views.renderers;

/**
 *
 * @author Jeky
 */
public class StringRenderer extends Renderer{

    @Override
    public void render(Object result) {
        response.renderHtml(result.toString());
    }
    
}
