package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class EscapeTemplateTag extends SimpleTemplateTag {

    public EscapeTemplateTag() {
        super("escape");
    }

    @Override
    public EvalFunc createTagFunc() {
        return CHILDREN_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        ASTNode tag = nodes.pollFirst();
        if (nodes.isEmpty()) {
            throw new TemplateParsingException("Escape tag should be given an argument", tag);
        }

        if (!isStr(nodes.peekFirst())) {
            throw new TemplateParsingException("Escape argument should be string", tag);
        }

        return FACTORYS.get(Type.STRING).build(nodes);
    }

}
