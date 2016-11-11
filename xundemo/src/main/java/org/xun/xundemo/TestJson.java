package org.xun.xundemo;

import java.util.*;
import org.xun.xuncore.views.Request;
import org.xun.xuncore.views.Response;
import org.xun.xuncore.views.View;

/**
 *
 * @author Jeky
 */
public class TestJson {
    
    @View(urlPattern = "^json$")
    public void process(Request req, Response res) {
        Map<String, Object> m = new HashMap<>();
        m.put("a", 1);
        m.put("b", 2);
        m.put("ccc", "ccc");

        Map<String, Object> m1 = new HashMap<>();
        m1.put("a1", "inside1");
        m.put("d", m1);

        res.renderJson(m);
    }

}
