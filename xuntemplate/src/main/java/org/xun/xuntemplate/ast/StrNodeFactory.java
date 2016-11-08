
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
public class StrNodeFactory extends ASTNodeFactory{

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode node = nodes.pollFirst();
        node.setType(Type.STRING);
        final String value = node.getValue().substring(1, node.getValue().length() - 1);
        node.setEvalFunc(new EvalFunc() {
            @Override
            public Object eval(RenderContext context, ASTNode current) {
                return value;
            }
        });
        
        return node;
    }

}
