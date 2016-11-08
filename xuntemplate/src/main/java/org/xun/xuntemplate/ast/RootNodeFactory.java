package org.xun.xuntemplate.ast;

import java.util.LinkedList;
import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;
import org.xun.xuntemplate.classhandlers.TemplateTagHandler;
import org.xun.xuntemplate.tags.TemplateTag;

/**
 *
 * @author Jeky
 */
public class RootNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode root = new ASTNode(Type.ROOT);
        root.setEvalFunc(CHILDREN_EVAL_FUNC);
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
                    root.addChild(FACTORYS.get(Type.EXP).build(subnodes));
                    break;
                case TAG_START:
                    String tagName = nodes.get(1).getValue();
                    Map<String, TemplateTag> tags = (Map<String, TemplateTag>) DefaultSettings.getSettings().getSettingValue(TemplateTagHandler.TEMPLATE_TAGS);
                    TemplateTag templateTag = tags.get(tagName);
                    if (templateTag == null) {
                        throw new TemplateParsingException("Error tag name:" + tagName, nodes.get(1));
                    }
                    root.addChild(templateTag.build(nodes));
                    break;
                case UNKNOWN:
                    root.addChild(FACTORYS.get(Type.HTML).build(nodes));
                    break;
            }
        }

        return root;
    }

}
