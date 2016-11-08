package org.xun.xuntemplate.ast;

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
public class VarExpNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        LinkedList<ASTNode> descendants = new LinkedList<>();
        // get var node
        descendants.add(buildByNodes(nodes, Type.VAR, 1));
        // get all method|field nodes
        while (!nodes.isEmpty() && nodes.peekFirst().getType() == Type.METHOD_START) {
            ASTNode methodStart = nodes.pollFirst();

            if (nodes.size() >= 3 && nodes.get(1).getType() == Type.LB && nodes.get(2).getType() == Type.RB) { // method node
                descendants.add(buildByNodes(nodes, Type.METHOD, 3));
            } else if (nodes.size() >= 1) { // field node
                descendants.add(buildByNodes(nodes, Type.FIELD, 1));
            } else {
                throw new TemplateParsingException("Error expression", methodStart);
            }
        }

        ASTNode varexp = new ASTNode(Type.VAR_EXP);
        varexp.setEvalFunc(VAR_EXP_EVAL_FUNC);
        ASTNode p = varexp;
        while (!descendants.isEmpty()) {
            ASTNode desc = descendants.pollLast();
            p.getChildren().add(0, desc);
            p = desc;
        }

        return varexp;
    }

    private static final EvalFunc VAR_EXP_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            return current.getChildren().get(0).eval(context);
        }
    };
}
