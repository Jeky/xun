package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateParsingException;

/**
 *
 * @author Jeky
 */
public class AutoEscape extends BlockTemplateTag {

    public AutoEscape() {
        super("autoescape", "endautoescape");
    }

    @Override
    public EvalFunc createTagFunc() {
        return CHILDREN_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        ASTNode tag = nodes.pollFirst();

        if (nodes.isEmpty()) {
            throw new TemplateParsingException("Either on or off should be given to autoescape tag", tag);
        }

        ASTNode switchNode = nodes.pollFirst();
        switch (switchNode.getValue()) {
            case "on":
                switchNode.setEvalFunc(ESCAPE_FUNC);
                return switchNode;
            case "off":
                switchNode.setEvalFunc(CHILDREN_EVAL_FUNC);
                return switchNode;
            default:
                throw new TemplateParsingException("Either on or off should be given to autoescape tag", tag);
        }
    }

    private static final EvalFunc ESCAPE_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            StringBuilder content = new StringBuilder();
            for(ASTNode n : current.getChildren()){
                content.append(n.eval(context));
            }

            StringBuilder buf = new StringBuilder();
            for (int i = 0; i < content.length(); i++) {
                switch (content.charAt(i)) {
                    case '&':
                        buf.append("&amp;");
                        break;
                    case '<':
                        buf.append("&lt;");
                        break;
                    case '>':
                        buf.append("&gt;");
                        break;
                    case '"':
                        buf.append("&quot;");
                        break;
                    case '\'':
                        buf.append("&#x27;");
                        break;
                    case '/':
                        buf.append("&#x2F;");
                        break;
                    case ' ':
                        buf.append("&nbsp;");
                        break;
                    default:
                        buf.append(content.charAt(i));
                }
            }
            return buf.toString();
        }
    };
}
