package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.TemplateParsingException;

/**
 *
 * @author Jeky
 */
public class Block extends BlockTemplateTag {

    public Block() {
        super("block", "endblock");
    }

    @Override
    public EvalFunc createTagFunc() {
        return CHILDREN_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        ASTNode tag = nodes.pollFirst();
        if (nodes.isEmpty()) {
            throw new TemplateParsingException("Block tag needs a name", tag);
        }

        ASTNode blockName = nodes.pollFirst();
        blockName.setEvalFunc(CHILDREN_EVAL_FUNC);

        return blockName;
    }

}
