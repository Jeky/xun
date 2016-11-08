package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;

/**
 *
 * @author Jeky
 */
public abstract class SimpleTemplateTag extends TemplateTag {

    public SimpleTemplateTag(String startTagName) {
        super(startTagName, new String[]{}, "");
    }

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode tag = buildTagNode(nodes);

        LinkedList<ASTNode> startTagNodes = collectTagNodes(nodes, startTagName);
        ASTNode startTagNode = buildStart(startTagNodes);
        tag.addChild(startTagNode);
        return tag;
    }
}
