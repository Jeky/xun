package template;

import java.io.IOException;
import java.util.LinkedList;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.Template;
import org.xun.xuntemplate.Tokenizer;
import org.xun.xuntemplate.Type;

import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jeky
 */
public class TemplateParseTest {

    @BeforeClass
    public static void setup() {
        DefaultSettings.getSettings();
    }

    public void testTemplateParsingTree(String temp, ASTNode root2) {
        System.out.println("paring: " + temp);
        LinkedList<ASTNode> nodes = new Tokenizer(temp).tokenize();
        System.out.println(nodes);
        ASTNode root1 = new Template(temp).root;

        System.out.println("?aaa");

        System.out.println("root 1:");
        printTree(root1, 0);
        System.out.println("root 2");
        printTree(root2, 0);
        compareTree(root1, root2);
    }

    private void printTree(ASTNode root, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(root);
        for (ASTNode c : root.getChildren()) {
            printTree(c, level + 1);
        }
    }

    private void compareTree(ASTNode root1, ASTNode root2) {
        assertEquals("value", root1.getValue(), root2.getValue());
        assertEquals("type", root1.getType(), root2.getType());
        assertEquals("children size of " + root1 + ", " + root2 + "\n", root1.getChildren().size(), root2.getChildren().size());

        for (int i = 0; i < root1.getChildren().size(); i++) {
            compareTree(root1.getChildren().get(i), root2.getChildren().get(i));
        }
    }

//    @Test
    public void testHtml() {
        testTemplateParsingTree("abc",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.HTML, "abc")));

        testTemplateParsingTree("ab c",
                                new ASTNode(Type.ROOT)
                                .addChild(new ASTNode(Type.HTML, "ab c")));
    }

//    @Test
    public void testExp() {
        testTemplateParsingTree("{{abc}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.VAR_EXP).addChild(
                                            new ASTNode(Type.VAR, "abc")))));

        testTemplateParsingTree("{{abc.b}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.VAR_EXP).addChild(
                                            new ASTNode(Type.FIELD, "b").addChild(
                                                new ASTNode(Type.VAR, "abc"))))));

        testTemplateParsingTree("{{abc.b()}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.VAR_EXP).addChild(
                                            new ASTNode(Type.METHOD, "b").addChild(
                                                new ASTNode(Type.VAR, "abc"))))));

        testTemplateParsingTree("{{abc.a.b()}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.VAR_EXP).addChild(
                                            new ASTNode(Type.METHOD, "b").addChild(
                                                new ASTNode(Type.FIELD, "a").addChild(
                                                    new ASTNode(Type.VAR, "abc")))))));

    }

//    @Test
    public void testFilter() {
        testTemplateParsingTree("{{abc|b}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "b").addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.VAR, "abc"))))));

        testTemplateParsingTree("{{abc|b|c}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "c").addChild(
                                            new ASTNode(Type.FILTER, "b").addChild(
                                                new ASTNode(Type.VAR_EXP).addChild(
                                                    new ASTNode(Type.VAR, "abc")))))));

        testTemplateParsingTree("{{abc|b:1}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "b").addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.VAR, "abc"))).addChild(
                                            new ASTNode(Type.INT, "1")))));

        testTemplateParsingTree("{{abc|b:\".\"}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "b").addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.VAR, "abc"))).addChild(
                                            new ASTNode(Type.STRING, "\".\"")))));

        testTemplateParsingTree("{{abc|b:1 2.2}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "b").addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.VAR, "abc"))).addChild(
                                            new ASTNode(Type.INT, "1")).addChild(
                                            new ASTNode(Type.FLOAT, "2.2")))));

        testTemplateParsingTree("{{abc|b:1 2.2 \"s\"}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "b").addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.VAR, "abc"))).addChild(
                                            new ASTNode(Type.INT, "1")).addChild(
                                            new ASTNode(Type.FLOAT, "2.2")).addChild(
                                            new ASTNode(Type.STRING, "\"s\"")))));

        testTemplateParsingTree("{{abc|b:c.d}}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.EXP).addChild(
                                        new ASTNode(Type.FILTER, "b").addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.VAR, "abc"))).addChild(
                                            new ASTNode(Type.VAR_EXP).addChild(
                                                new ASTNode(Type.FIELD, "d").addChild(
                                                    new ASTNode(Type.VAR, "c")))))));
    }

//    @Test
    public void testTag() {
        testTemplateParsingTree("{% if a.b.c() %}abc{%endif%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "if").addChild(
                                        new ASTNode(Type.TAG_NAME, "if").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP).addChild(
                                                new ASTNode(Type.VAR_EXP).addChild(
                                                    new ASTNode(Type.METHOD, "c").addChild(
                                                        new ASTNode(Type.FIELD, "b").addChild(
                                                            new ASTNode(Type.VAR, "a")))))).addChild(
                                            new ASTNode(Type.HTML, "abc")))));

        testTemplateParsingTree("{% if a %}abc{%endif%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "if").addChild(
                                        new ASTNode(Type.TAG_NAME, "if").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP).addChild(
                                                new ASTNode(Type.VAR_EXP).addChild(
                                                    new ASTNode(Type.VAR, "a")))).addChild(
                                            new ASTNode(Type.HTML, "abc")))));

        testTemplateParsingTree("{% if a == 1 %}abc{%endif%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "if").addChild(
                                        new ASTNode(Type.TAG_NAME, "if").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP).addChild(
                                                new ASTNode(Type.EQ, "==").addChild(
                                                    new ASTNode(Type.VAR_EXP).addChild(
                                                        new ASTNode(Type.VAR, "a"))).addChild(
                                                    new ASTNode(Type.INT, "1")))).addChild(
                                            new ASTNode(Type.HTML, "abc")))));

        testTemplateParsingTree("{%if model != 1%}aaa{%else%}bbb{%endif%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "if").addChild(
                                        new ASTNode(Type.TAG_NAME, "if").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP).addChild(
                                                new ASTNode(Type.NEQ, "!=").addChild(
                                                    new ASTNode(Type.VAR_EXP).addChild(
                                                        new ASTNode(Type.VAR, "model"))).addChild(
                                                    new ASTNode(Type.INT, "1")))).addChild(
                                            new ASTNode(Type.HTML, "aaa"))).addChild(
                                        new ASTNode(Type.TAG_NAME, "else").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP)).addChild(
                                            new ASTNode(Type.HTML, "bbb")))));

        testTemplateParsingTree("{% if a - 1 == 0 %}aaa{%endif%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "if").addChild(
                                        new ASTNode(Type.TAG_NAME, "if").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP).addChild(
                                                new ASTNode(Type.EQ, "==").addChild(
                                                    new ASTNode(Type.SUB, "-").addChild(
                                                        new ASTNode(Type.VAR_EXP).addChild(
                                                            new ASTNode(Type.VAR, "a"))).addChild(
                                                        new ASTNode(Type.INT, "1"))).addChild(
                                                    new ASTNode(Type.INT, "0")))).addChild(
                                            new ASTNode(Type.HTML, "aaa")))));

        testTemplateParsingTree("{% if a - 0.5 * 2 == 0 %}aaa{%endif%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "if").addChild(
                                        new ASTNode(Type.TAG_NAME, "if").addChild(
                                            new ASTNode(Type.BOOLEAN_EXP).addChild(
                                                new ASTNode(Type.EQ, "==").addChild(
                                                    new ASTNode(Type.SUB, "-").addChild(
                                                        new ASTNode(Type.VAR_EXP).addChild(
                                                            new ASTNode(Type.VAR, "a"))).addChild(
                                                        new ASTNode(Type.MUL, "*").addChild(
                                                            new ASTNode(Type.FLOAT, "0.5")).addChild(
                                                            new ASTNode(Type.INT, "2")))).addChild(
                                                    new ASTNode(Type.INT, "0")))).addChild(
                                            new ASTNode(Type.HTML, "aaa")))));
    }

//    @Test
    public void testParseAutoEscape() {
        testTemplateParsingTree("{% autoescape on%}abc{%  endautoescape%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "autoescape").addChild(
                                        new ASTNode(Type.UNKNOWN, "on").addChild(
                                            new ASTNode(Type.HTML, "abc")))));
    }

    @Test
    public void testExtends() {
        testTemplateParsingTree("{%extends \"template.html\"%}{%block title%}abc{%endblock%}",
                                new ASTNode(Type.ROOT).addChild(
                                    new ASTNode(Type.TAG, "block").addChild(
                                        new ASTNode(Type.TAG, "block").addChild(
                                            new ASTNode(Type.UNKNOWN, "title").addChild(
                                                new ASTNode(Type.HTML, "abc"))))));
    }
}
