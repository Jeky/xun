package org.xun.xuntemplate.tags;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;
import org.xun.xuntemplate.ast.ASTNodeFactory;

/**
 *
 * @author Jeky
 */
public abstract class TemplateTag extends ASTNodeFactory {

    public TemplateTag(String startTagName, String[] branchTagNames, String endTagName) {
        this.startTagName = startTagName;
        this.branchTagNames = new HashSet<>(Arrays.asList(branchTagNames));
        this.endTagName = endTagName;
    }

    protected LinkedList<ASTNode> collectTagNodes(LinkedList<ASTNode> nodes, String expectedTagName) {
        ASTNode start = nodes.peekFirst();
        if (start.getType() != Type.TAG_START) {
            throw new TemplateParsingException("Wrong tag expression", start);
        }

        // poll tag start
        nodes.pollFirst();
        if (nodes.isEmpty()) {
            throw new TemplateParsingException("Incomplete tag expression", start);
        }
        String tagName = nodes.peekFirst().getValue();
        if (!tagName.equals(expectedTagName)) {
            throw new TemplateParsingException("Wrong tag expression", nodes.peekFirst());
        }

        LinkedList<ASTNode> tagNodes = new LinkedList<>();
        while (nodes.peekFirst().getType() != Type.TAG_END) {
            tagNodes.add(nodes.poll());
            if (nodes.isEmpty()) {
                throw new TemplateParsingException("Incomplete tag expression", start);
            }
        }
        // poll tag end
        nodes.pollFirst();

        return tagNodes;
    }

    public ASTNode buildTagNode(LinkedList<ASTNode> nodes) {
        ASTNode start = nodes.peekFirst();
        ASTNode tag = new ASTNode(Type.TAG, startTagName, start.getLineNum(), start.getColNum());
        tag.setEvalFunc(createTagFunc());
        return tag;
    }

    public abstract EvalFunc createTagFunc();

    public abstract ASTNode buildStart(LinkedList<ASTNode> nodes);

    public ASTNode buildBranch(LinkedList<ASTNode> nodes, String tagName) {
        throw new UnsupportedOperationException("Tag " + startTagName + " doesn't have any branch");
    }

    public String getTagName() {
        return startTagName;
    }

    protected String startTagName;
    protected Set<String> branchTagNames;
    protected String endTagName;

}
