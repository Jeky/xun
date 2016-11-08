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
public class Cycle extends SimpleTemplateTag {

    public Cycle() {
        super("cycle");
    }

    @Override
    public EvalFunc createTagFunc() {
        return CHILDREN_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        // poll tag name ast node
        ASTNode tag = nodes.pollFirst();
        if (nodes.isEmpty()) {
            throw new TemplateParsingException("Cycle tag should be given arguments", tag);
        }

        ASTNode cycle = new ASTNode(Type.TAG_NAME, "cycle", tag.getLineNum(), tag.getColNum());
        cycle.setEvalFunc(new CycleEvalFunc());
        for (ASTNode child : mergeVarNodes(nodes)) {
            cycle.addChild(child);
        }

        return cycle;
    }

    private LinkedList<ASTNode> mergeVarNodes(LinkedList<ASTNode> nodes) {
        LinkedList<ASTNode> result = new LinkedList<>();
        while (!nodes.isEmpty()) {
            ASTNode first = nodes.peekFirst();
            if (nodes.peekFirst().getType() == Type.UNKNOWN) {
                if (isFloat(nodes)) {
                    result.add(FACTORYS.get(Type.FLOAT).build(nodes));
                } else if (isInt(first)) {
                    result.add(FACTORYS.get(Type.INT).build(nodes));
                } else if (isStr(first)) {
                    result.add(FACTORYS.get(Type.STRING).build(nodes));
                } else {
                    result.add(FACTORYS.get(Type.VAR_EXP).build(nodes));
                }
            } else {
                result.add(nodes.pollFirst());
            }
        }

        return result;
    }

    public static class CycleEvalFunc implements EvalFunc {

        @Override
        public Object eval(RenderContext context, ASTNode current) {
            Object result = current.getChildren().get(index).eval(context);
            index = (index + 1) % current.getChildren().size();
            
            return result;
        }
        
        public void reset(){
            index = 0;
        }
        
        private int index = 0;
    }
}
