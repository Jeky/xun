package template;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.Template;
import org.xun.xuntemplate.Type;

import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jeky
 */
public class TemplateEvalTest {

    @BeforeClass
    public static void setup() {
        DefaultSettings.getSettings();
    }

    public void testEval(String templateStr, Object model, String expected) {
        Template template = new Template(templateStr);
        RenderContext context = new RenderContext(model);

        for (int i = 0; i < 10; i++) {
            String actual = template.render(context);
            assertEquals(expected, actual);
        }
    }

    @Test
    public void testHtml() {
        testEval("abc", "aaa", "abc");
        testEval("ab\nc", "aaa", "ab\nc");
    }

    @Test
    public void testVarExp() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "123");

        Map<String, Object> submap = new HashMap<>();
        submap.put("ccc", "jeky");
        map.put("b", submap);

        ASTNode node = new ASTNode(Type.UNKNOWN, "jeky");

        testEval("{{model}}", "abc", "abc");
        testEval("{{a}}", map, "123");
        testEval("{{model.length()}}", "abc", "3");
        testEval("{{a.length()}}", map, "3");
        testEval("{{b.ccc}}", map, "jeky");
        testEval("{{model.value}}", node, "jeky");
        testEval("{{model.type}}", node, "UNKNOWN");
        testEval("{{model.value.length()}}", node, "4");
    }

    @Test
    public void testFilter() {
        testEval("{{model|UpperCase}}", "Jeky", "JEKY");
        testEval("{{model|LowerCase}}", "Jeky", "jeky");

        testEval("{{model|CapFirst}}", "Jeky", "Jeky");
        testEval("{{model|CapFirst}}", "jeky", "Jeky");
        testEval("{{model|CapFirst}}", "", "");

        testEval("{{model|Cut:\"a\"}}", "abcd Aa", "bcd A");
        testEval("{{model|Cut:\".\"}}", "abcd.Aa", "abcdAa");
        testEval("{{model|Cut:\"\"}}", "abcd.Aa", "abcd.Aa");

        testEval("{{model| Add:111}}", 123, "234");
        testEval("{{model| Add:0.2}}", 0.2, "0.4");
        testEval("{{model| Add:0.2}}", 1, "1.2");
        testEval("{{model| Add:1}}", 0.3, "1.3");

        LocalDateTime now = LocalDateTime.now();
        testEval("{{model|Format:\"yyyy MM dd\"}}", now, now.format(DateTimeFormatter.ofPattern("yyyy MM dd")));

        testEval("{{model|Default:\"abc\"}}", null, "abc");
        testEval("{{model|Default:\"abc\"}}", "", "abc");
        testEval("{{model|Default:\"abc\"}}", "aaa", "aaa");

        testEval("{{model|First}}", new String[]{"a", "b", "c"}, "a");
        testEval("{{model|First}}", Arrays.asList("a", "b", "c"), "a");

        testEval("{{model|Last}}", new String[]{"a", "b", "c"}, "c");
        testEval("{{model|Last}}", Arrays.asList("a", "b", "c"), "c");

        testEval("{{model|Join:\"+\"}}", new String[]{"a", "b", "c"}, "a+b+c");
        testEval("{{model|Join:\"+\"}}", Arrays.asList("a", "b", "c"), "a+b+c");
        testEval("{{model|Join:\"+\"}}", new String[0], "");
    }

    @Test
    public void testTag() {
        LocalDateTime now = LocalDateTime.now();
        testEval("{% now \"yyyy MM dd\"%}", "", now.format(DateTimeFormatter.ofPattern("yyyy MM dd")));
        testEval("{% now %}", "", now.format(DateTimeFormatter.ofPattern("yyyy/MM/dd kk:mm:ss")));

        testForloop(new String[]{"Jim", "John", "Smith"});
        testForloop(Arrays.asList(new String[]{"Jim", "John", "Smith"}));
        testDoubleForLoop();
    }

    public void testDoubleForLoop() {
        String[][] model = new String[][]{
            {"a", "b", "c"},
            {"d", "e", "f"}
        };
        testEval("{%for names in model%}{% for name in names %}{{forloop.parentloop.counter}}.{{forloop.counter}}.{{name}}\n{% endfor%}{%endfor%}", model,
                 "1.1.a\n1.2.b\n1.3.c\n2.1.d\n2.2.e\n2.3.f\n");
    }

    public void testForloop(Object model) {
        testEval("{% for name in model %}{{name}}\n{%endfor%}", model, "Jim\nJohn\nSmith\n");
        testEval("{% for name in model %}{{forloop.counter}}.{{name}}\n{%endfor%}", model,
                 "1.Jim\n2.John\n3.Smith\n");
        testEval("{% for name in model %}{{forloop.counter0}}.{{name}}\n{%endfor%}", model,
                 "0.Jim\n1.John\n2.Smith\n");
        testEval("{% for name in model %}{{forloop.revcounter}}.{{name}}\n{%endfor%}", model,
                 "3.Jim\n2.John\n1.Smith\n");
        testEval("{% for name in model %}{{forloop.revcounter0}}.{{name}}\n{%endfor%}", model,
                 "2.Jim\n1.John\n0.Smith\n");
        testEval("{% for name in model %}{{name}} - {{forloop.first}} - {{forloop.last}}\n{%endfor%}", model,
                 "Jim - true - false\nJohn - false - false\nSmith - false - true\n");
    }

    @Test
    public void testIf() {
        testEval("{%if model == 1%}aaa{%endif%}", 1, "aaa");
        testEval("{%if model - 1 == 0%}aaa{%endif%}", 1, "aaa");
        testEval("{%if model - 1 * 2 == 0%}aaa{%endif%}", 2, "aaa");
        testEval("{%if model - 0.5 * 2 == 0%}aaa{%endif%}", 1, "aaa");
        testEval("{%if (model - 0.5) * 2 == 0%}aaa{%endif%}", 0.5, "aaa");
        testEval("{%if model > 1 && model < 2%}aaa{%endif%}", 1.5, "aaa");
        testEval("{%if model != 1%}aaa{%else%}bbb{%endif%}", 1, "bbb");
    }

    @Test
    public void testAutoEscape() {
        testEval("{% autoescape on%} {%  endautoescape%}", "", "&nbsp;");
        testEval("{% autoescape on%}abc{%  endautoescape%}", "", "abc");
        testEval("{% autoescape off%}abc{%  endautoescape%}", "", "abc");
        testEval("{% autoescape on%}<abc>{%  endautoescape%}", "", "&lt;abc&gt;");
        testEval("{% autoescape off%}<abc>{%  endautoescape%}", "", "<abc>");
        testEval("{% autoescape on%}<html> \"jeky's\" </html>{%  endautoescape%}", "",
                 "&lt;html&gt;&nbsp;&quot;jeky&#x27;s&quot;&nbsp;&lt;&#x2F;html&gt;");
    }

    @Test
    public void testTemplateInheritance() {
        testEval("{%block title%}abc{%endblock%}", "", "abc");
        testEval("{%extends \"template.html\"%}{%block title%}abc{%endblock%}", "", "abc");
    }

    @Test
    public void testCycle() {
        testEval("{%for i in model%}{{i}}.{%cycle \"a\" \"b\"%},{%endfor%}", new String[]{"1", "2", "3"}, "1.a,2.b,3.a,");
    }

    @Test
    public void testEscape() {
        testEval("{%escape \"{{\"%}", "", "{{");
    }
}
