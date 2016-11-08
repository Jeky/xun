package org.xun.xuntemplate.ast;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class HtmlNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode n = nodes.pollFirst();
        n.setEvalFunc(VALUE_EVAL_FUNC);
        n.setType(Type.HTML);
        
        return n;
    }
}
