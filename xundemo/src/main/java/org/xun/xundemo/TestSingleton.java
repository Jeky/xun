
package org.xun.xundemo;

import org.xun.xuncore.reflect.Singleton;
import org.xun.xuncore.views.View;
import org.xun.xuncore.views.renderers.RenderString;


/**
 *
 * @author Jeky
 */
@Singleton
public class TestSingleton {

    @View(urlPattern = "^singleton$")
    @RenderString
    public String increase() {
        count++;
        return "" + count;
    }

    private int count = 0;
}
