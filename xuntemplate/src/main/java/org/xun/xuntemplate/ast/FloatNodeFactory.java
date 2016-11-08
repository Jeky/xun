package org.xun.xuntemplate.ast;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class FloatNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        String value = "";
        ASTNode first = nodes.peekFirst();
        for (int i = 0; i < 3; i++) {
            value += nodes.pollFirst().getValue();
        }
        ASTNode node = new ASTNode(Type.FLOAT, value, first.getLineNum(), first.getColNum());
        final double floatVal = Double.parseDouble(value);
        node.setEvalFunc(new EvalFunc() {
            @Override
            public Object eval(RenderContext context, ASTNode current) {
                return floatVal;
            }
        });

        return node;
    }

}
