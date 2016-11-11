
package org.xun.xundemo;

import org.xun.xuncore.views.Request;
import org.xun.xuncore.views.Response;
import org.xun.xuncore.views.View;


/**
 *
 * @author Jeky
 */
public class Hello {

    @View(urlPattern = "^hello$")
    public void hello(Request req, Response res) {
        res.renderString("<h1>Hello!</h1>");
    }

}
