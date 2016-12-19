package template;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import org.junit.Test;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.Tokenizer;
import org.xun.xuntemplate.Type;

import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author Jeky
 */
public class TokenizerTest {

    @BeforeClass
    public static void setup() {
        DefaultSettings.getSettings();
    }

    public void testTokenize(String template, ASTNode... tokens) {
        Tokenizer tokenizer = new Tokenizer(template);
        LinkedList<ASTNode> expected = new LinkedList<>(Arrays.asList(tokens));
        
        assertEquals(tokenizer.tokenize(), expected);
    }

    @Test
    public void testHTML() {
        testTokenize("");
        testTokenize("abc", new ASTNode("abc", 0, 0));
        testTokenize("ab c", new ASTNode("ab c", 0, 0));
        testTokenize("ab\nc", new ASTNode("ab\nc", 0, 0));
    }

    @Test
    public void testComment() {
        testTokenize("{##}",
                     new ASTNode(Type.COMMENT_START, "{#", 0, 0),
                     new ASTNode(Type.COMMENT_END, "#}", 0, 2));

        testTokenize("{#abc#}",
                     new ASTNode(Type.COMMENT_START, "{#", 0, 0),
                     new ASTNode(Type.UNKNOWN, "abc", 0, 2),
                     new ASTNode(Type.COMMENT_END, "#}", 0, 5));
    }

    @Test
    public void testVariable() {
        testTokenize("{{aaa}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.EXP_END, "}}", 0, 5));

        testTokenize("{{ aaa}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 3),
                     new ASTNode(Type.EXP_END, "}}", 0, 6));

        testTokenize("{{ aaa  }}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 3),
                     new ASTNode(Type.EXP_END, "}}", 0, 8));

        testTokenize("{{aaa.bbb}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.METHOD_START, ".", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.EXP_END, "}}", 0, 9));

        testTokenize("{{aaa. bbb}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.METHOD_START, ".", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 7),
                     new ASTNode(Type.EXP_END, "}}", 0, 10));

        testTokenize("{{aaa.bbb()}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.METHOD_START, ".", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.LB, "(", 0, 9),
                     new ASTNode(Type.RB, ")", 0, 10),
                     new ASTNode(Type.EXP_END, "}}", 0, 11));

        testTokenize("{{aaa.bbb().dd}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.METHOD_START, ".", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.LB, "(", 0, 9),
                     new ASTNode(Type.RB, ")", 0, 10),
                     new ASTNode(Type.METHOD_START, ".", 0, 11),
                     new ASTNode(Type.UNKNOWN, "dd", 0, 12),
                     new ASTNode(Type.EXP_END, "}}", 0, 14));
    }

    @Test
    public void testFilter() {
        testTokenize("{{aaa|bbb}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.EXP_END, "}}", 0, 9));

        testTokenize("{{ aaa|bbb}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 3),
                     new ASTNode(Type.FILTER_START, "|", 0, 6),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 7),
                     new ASTNode(Type.EXP_END, "}}", 0, 10));

        testTokenize("{{aaa| bbb}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 7),
                     new ASTNode(Type.EXP_END, "}}", 0, 10));

        testTokenize("{{aaa |bbb}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 6),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 7),
                     new ASTNode(Type.EXP_END, "}}", 0, 10));

        testTokenize("{{aaa|bbb }}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.EXP_END, "}}", 0, 10));

        testTokenize("{{aaa|bbb:1}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.FILTER_PARAMETER_START, ":", 0, 9),
                     new ASTNode(Type.UNKNOWN, "1", 0, 10),
                     new ASTNode(Type.EXP_END, "}}", 0, 11));
        
        testTokenize("{{aaa|bbb:\".\"}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.FILTER_PARAMETER_START, ":", 0, 9),
                     new ASTNode(Type.UNKNOWN, "\".\"", 0, 10),
                     new ASTNode(Type.EXP_END, "}}", 0, 13));

        testTokenize("{{aaa|bbb:1 \"c\"}}",
                     new ASTNode(Type.EXP_START, "{{", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.FILTER_START, "|", 0, 5),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.FILTER_PARAMETER_START, ":", 0, 9),
                     new ASTNode(Type.UNKNOWN, "1", 0, 10),
                     new ASTNode(Type.UNKNOWN, "\"c\"", 0, 12),
                     new ASTNode(Type.EXP_END, "}}", 0, 15));
    }

    @Test
    public void testTag() {
        testTokenize("{%aaa%}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.TAG_END, "%}", 0, 5));
        
        testTokenize("{% aaa%}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 3),
                     new ASTNode(Type.TAG_END, "%}", 0, 6));
        
        testTokenize("{%aaa %}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.TAG_END, "%}", 0, 6));
        
        testTokenize("{%aaa bbb%}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.TAG_END, "%}", 0, 9));
        
        testTokenize("{%aaa bbb \"c\"%}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "aaa", 0, 2),
                     new ASTNode(Type.UNKNOWN, "bbb", 0, 6),
                     new ASTNode(Type.UNKNOWN, "\"c\"", 0, 10),
                     new ASTNode(Type.TAG_END, "%}", 0, 13));
        
        testTokenize("{%if a==1%}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "if", 0, 2),
                     new ASTNode(Type.UNKNOWN, "a", 0, 5),
                     new ASTNode(Type.EQ, "==", 0, 6),
                     new ASTNode(Type.UNKNOWN, "1", 0, 8),
                     new ASTNode(Type.TAG_END, "%}", 0, 9));
        
        testTokenize("{%if (a+ b)>1%}",
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "if", 0, 2),
                     new ASTNode(Type.LB, "(", 0, 5),
                     new ASTNode(Type.UNKNOWN, "a", 0, 6),
                     new ASTNode(Type.ADD, "+", 0, 7),
                     new ASTNode(Type.UNKNOWN, "b", 0, 9),
                     new ASTNode(Type.RB, ")", 0, 10),
                     new ASTNode(Type.GREATER, ">", 0, 11),
                     new ASTNode(Type.UNKNOWN, "1", 0, 12),
                     new ASTNode(Type.TAG_END, "%}", 0, 13));
        
        testTokenize("{% for name in model %}{{forloop.counter}}.{{name}}\n{%endfor%}", 
                     new ASTNode(Type.TAG_START, "{%", 0, 0),
                     new ASTNode(Type.UNKNOWN, "for", 0, 3),
                     new ASTNode(Type.UNKNOWN, "name", 0, 7),
                     new ASTNode(Type.UNKNOWN, "in", 0, 12),
                     new ASTNode(Type.UNKNOWN, "model", 0, 15),
                     new ASTNode(Type.TAG_END, "%}", 0, 21),
                     new ASTNode(Type.EXP_START, "{{", 0, 23),
                     new ASTNode(Type.UNKNOWN, "forloop", 0, 25),
                     new ASTNode(Type.METHOD_START, ".", 0, 32),
                     new ASTNode(Type.UNKNOWN, "counter", 0, 33),
                     new ASTNode(Type.EXP_END, "}}", 0, 40),
                     new ASTNode(Type.UNKNOWN, ".", 0, 42),
                     new ASTNode(Type.EXP_START, "{{", 0, 43),
                     new ASTNode(Type.UNKNOWN, "name", 0, 45),
                     new ASTNode(Type.EXP_END, "}}", 0, 49),
                     new ASTNode(Type.UNKNOWN, "\n", 0, 51),
                     new ASTNode(Type.TAG_START, "{%", 1, 0),
                     new ASTNode(Type.UNKNOWN, "endfor", 1, 2),
                     new ASTNode(Type.TAG_END, "%}", 1, 8));
    }
}
