package org.xun.xundemo;

import java.util.HashMap;
import java.util.Map;
import org.xun.xuncore.reflect.Param;
import org.xun.xuncore.views.View;
import org.xun.xuncore.views.renderers.RenderJson;
import org.xun.xuncore.views.renderers.RenderString;

/**
 *
 * @author Jeky
 */
public class TestPO {

    @View(urlPattern = "^testpo1$")
    @RenderString
    public String someMethodWithoutArgs() {
        return "TestPO.someMethodWithoutArgs() passed!";
    }

    @View(urlPattern = "^testpo2$")
    @RenderString
    public String someMethodWithArgs1(@Param("a") int a) {
        return "TestPO.someMethodWithArgs1(" + a + ")";
    }
 
    @View(urlPattern = "^testpo3$")
    @RenderJson
    public Map<String, Object> someMethodWithMultiArgs(@Param("a") int a, @Param("b") String b) {
        Map<String, Object> result = new HashMap<>();
        result.put("a", a);
        result.put("b", b);

        Map<String, String> inside = new HashMap<>();
        inside.put("lalala", "hahaha");

        result.put("other", inside);
        return result;
    }

    @View(urlPattern = "^testpo4/(?<name>\\w+)/(?<id>\\d+)$")
    @RenderString
    public String m4(@Param("name") String name, @Param("id") int id) {
        return "name = " + name + ", id = " + id;
    }
}
