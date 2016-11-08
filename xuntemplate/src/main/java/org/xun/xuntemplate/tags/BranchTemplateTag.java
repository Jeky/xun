package org.xun.xuntemplate.tags;

import java.util.LinkedList;
import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;
import org.xun.xuntemplate.classhandlers.TemplateTagHandler;

/**
 *
 * @author Jeky
 */
public abstract class BranchTemplateTag extends TemplateTag {

    public BranchTemplateTag(String startTagName, String[] branchTagNames, String endTagName) {
        super(startTagName, branchTagNames, endTagName);
    }

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode tag = buildTagNode(nodes);

        LinkedList<ASTNode> startTagNodes = collectTagNodes(nodes, startTagName);
        ASTNode startTagNode = buildStart(startTagNodes);
        tag.addChild(startTagNode);
        ASTNode current = startTagNode;

        while (!nodes.isEmpty()) {
            ASTNode n = nodes.peekFirst();
            switch (n.getType()) {
                case EXP_START:
                    ASTNode start = nodes.pollFirst();
                    LinkedList<ASTNode> subnodes = new LinkedList<>();
                    while (nodes.peekFirst().getType() != Type.EXP_END) {
                        subnodes.add(nodes.pollFirst());
                        if (subnodes.isEmpty()) {
                            throw new TemplateParsingException("Cannot find the end of variable expression", n);
                        }
                    }
                    nodes.pollFirst();
                    if (subnodes.isEmpty()) {
                        throw new TemplateParsingException("Variable expression is empty", start);
                    }
                    current.addChild(FACTORYS.get(Type.EXP).build(subnodes));
                    break;
                case TAG_START:
                    String tagName = nodes.get(1).getValue();
                    if (tagName.equals(endTagName)) {
                        LinkedList<ASTNode> ns = collectTagNodes(nodes, endTagName);
                        return tag;
                    } else if (branchTagNames.contains(tagName)) {
                        LinkedList<ASTNode> branchTagNodes = collectTagNodes(nodes, tagName);
                        ASTNode branchTagNode = buildBranch(branchTagNodes, tagName);
                        tag.addChild(branchTagNode);
                        current = branchTagNode;
                    } else {
                        Map<String, TemplateTag> tags = (Map<String, TemplateTag>) DefaultSettings.getSettings().getSettingValue(TemplateTagHandler.TEMPLATE_TAGS);
                        TemplateTag templateTag = tags.get(tagName);
                        if (templateTag == null) {
                            throw new TemplateParsingException("Error tag name:" + tagName, nodes.get(1));
                        }
                        current.addChild(templateTag.build(nodes));
                    }
                    break;
                case UNKNOWN:
                    current.addChild(FACTORYS.get(Type.HTML).build(nodes));
                    break;
            }
        }

        throw new TemplateParsingException("Cannot find the end of tag", tag);
    }
}
