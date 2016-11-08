package org.xun.xuntemplate.ast;

import java.util.*;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class ExpNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {        
        LinkedList<ASTNode> descendants = new LinkedList<>();
        int i = 0;
        for (ASTNode n : nodes) {
            if (n.getType() == Type.FILTER_START) {
                break;
            }
            i++;
        }
        descendants.add(buildByNodes(nodes, Type.VAR_EXP, i));

        // get all filter nodes 
        while (!nodes.isEmpty()) {
            ASTNode filterStart = nodes.pollFirst();
            if (filterStart.getType() != Type.FILTER_START) {
                throw new TemplateParsingException("Error expression", filterStart);
            }

            LinkedList<ASTNode> filterNodes = new LinkedList<>();
            while (!nodes.isEmpty() && nodes.peekFirst().getType() != Type.FILTER_START) {
                filterNodes.add(nodes.pollFirst());
            }
            if (filterNodes.isEmpty()) {
                throw new TemplateParsingException("Error expression", filterStart);
            }
            descendants.add(FACTORYS.get(Type.FILTER).build(filterNodes));
        }

        // construct ast node
        ASTNode exp = new ASTNode(Type.EXP);
        exp.setEvalFunc(CHILDREN_EVAL_FUNC);
        ASTNode p = exp;
        while (!descendants.isEmpty()) {
            ASTNode desc = descendants.pollLast();
            p.getChildren().add(0, desc);
            p = desc;
        }

        return exp;
    }
    
}
