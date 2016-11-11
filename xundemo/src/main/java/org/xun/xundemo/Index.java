
package org.xun.xundemo;

import org.xun.xuncore.views.View;
import org.xun.xuncore.views.renderers.RenderHtml;

/**
 *
 * @author Jeky
 */
public class Index {
    
    @View(urlPattern = "^$")
    @RenderHtml
    public String index(){
        return "index.html";
    }
}
