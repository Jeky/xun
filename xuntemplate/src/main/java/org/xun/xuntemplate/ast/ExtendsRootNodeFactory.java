package org.xun.xuntemplate.ast;

import java.util.*;
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
public class ExtendsRootNodeFactory extends ASTNodeFactory {

    public ExtendsRootNodeFactory(ASTNode root) {
        this.root = root;
        this.blockNodes = new HashMap<>();

        root.visit((ASTNode n) -> {
            if (n.getType() == Type.TAG && n.getValue().endsWith("block")) {
                String blockName = n.getChildren().get(0).getValue();
                if (blockNodes.containsKey(blockName)) {
                    throw new TemplateParsingException("Block name duplicated", n);
                } else {
                    blockNodes.put(blockName, n);
                }
            }
        });
    }

    private void printTree(ASTNode root, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(root);
        for (ASTNode c : root.getChildren()) {
            printTree(c, level + 1);
        }
    }

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        while (nodes.size() > 1) {
            ASTNode n = nodes.peekFirst();
            if (n.getType() == Type.TAG_START && nodes.get(1).getValue().equals("block")) {
                Map<String, TemplateTag> tags = (Map<String, TemplateTag>) DefaultSettings.getSettings().getSettingValue(TemplateTagHandler.TEMPLATE_TAGS);
                TemplateTag block = tags.get("block");
                ASTNode blockNode = block.build(nodes);

                String blockName = blockNode.getChildren().get(0).getValue();
                blockNodes.get(blockName).getChildren().set(0, blockNode);
            } else {
                nodes.pollFirst();
            }
        }

        return root;
    }

    private ASTNode root;
    private Map<String, ASTNode> blockNodes;
}
