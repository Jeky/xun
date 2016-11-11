
package org.xun.xundemo;

import org.xun.xuncore.views.*;


/**
 *
 * @author Jeky
 */
public class Hello {

    @View(urlPattern = "^$")
    @View(urlPattern = "^hello$")
    public void hello(Request req, Response res) {
        res.renderHtml("<h1>Hello!</h1>");
    }

}
