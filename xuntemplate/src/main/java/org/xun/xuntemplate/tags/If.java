package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class If extends BranchTemplateTag {

    public If() {
        super("if", new String[]{"elseif", "else"}, "endif");
    }

    @Override
    public EvalFunc createTagFunc() {
        return IF_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        return buildIfTag(nodes);
    }

    @Override
    public ASTNode buildBranch(LinkedList<ASTNode> nodes, String tagName) {
        if (tagName.equals("elseif")) {
            return buildIfTag(nodes);
        } else if (tagName.equals("else")) {
            ASTNode trueBoolean = new ASTNode(Type.BOOLEAN_EXP);
            trueBoolean.setEvalFunc(TRUE_EVAL_FUNC);

            ASTNode elseTag = nodes.pollFirst();
            elseTag.setType(Type.TAG_NAME);
            elseTag.addChild(trueBoolean);

            return elseTag;
        } else {
            throw new TemplateParsingException("Error tag name", nodes.peekFirst());
        }
    }

    private ASTNode buildIfTag(LinkedList<ASTNode> nodes) {
        ASTNode ifTag = nodes.pollFirst();
        if (nodes.isEmpty()) {
            throw new TemplateParsingException("Booolean expression in if tag is empty", ifTag);
        }
        ifTag.setType(Type.TAG_NAME);
        try {
            ifTag.addChild(FACTORYS.get(Type.BOOLEAN_EXP).build(nodes));
        } catch (Exception e) {
            throw new TemplateParsingException("Boolean expression error", e, ifTag);
        }

        return ifTag;
    }

    private static final EvalFunc IF_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            for (ASTNode n : current.getChildren()) {
                // boolean exp stored in children[0]
                ASTNode booleanExp = n.getChildren().get(0);
                if ((boolean) booleanExp.eval(context)) {
                    // value exp stored in children[1]
                    return n.getChildren().get(1).eval(context);
                }
            }

            return "";
        }
    };

    private static final EvalFunc TRUE_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            return true;
        }
    };

}
