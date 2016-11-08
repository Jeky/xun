package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;

/**
 * All the extending template functions have been implemented in
 * ExtendsRootNodeFactory class. This tag is simply used to show the result when
 * extend tag is not on the first line of template file.
 *
 * @see mccree.template.ast.ExtendsRootNodeFactory
 * @author Jeky
 */
public class Extends extends SimpleTemplateTag {

    public Extends() {
        super("extends");
    }

    @Override
    public EvalFunc createTagFunc() {
        return VALUE_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        ASTNode tag = nodes.pollFirst();
        if (nodes.isEmpty()) {
            LinkedList<ASTNode> dummy = new LinkedList<>();
            dummy.add(new ASTNode("\"\"", tag.getLineNum(), tag.getColNum()));
            return FACTORYS.get(Type.STRING).build(dummy);
        }

        if (!isStr(nodes.peekFirst())) {
            throw new TemplateParsingException("Tag extends should be given a string argument", tag);
        }
        ASTNode arg = FACTORYS.get(Type.STRING).build(nodes);
        return arg;
    }

}
