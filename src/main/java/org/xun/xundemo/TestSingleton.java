
package org.xun.xundemo;

import org.xun.xuncore.reflect.Singleton;
import org.xun.xuncore.views.View;
import org.xun.xuncore.views.renderers.RenderHtml;

/**
 *
 * @author Jeky
 */
@Singleton
public class TestSingleton {

    @View(urlPattern = "^singleton$")
    @RenderHtml
    public String increase() {
        count++;
        return "" + count;
    }

    private int count = 0;
}
