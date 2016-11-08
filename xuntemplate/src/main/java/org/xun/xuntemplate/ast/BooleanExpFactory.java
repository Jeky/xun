package org.xun.xuntemplate.ast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.Operators;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class BooleanExpFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode booleanExp = new ASTNode(Type.BOOLEAN_EXP);
        booleanExp.setEvalFunc(BOOLEAN_EVAL_FUNC);

        nodes = mergeVarNodes(nodes);
        booleanExp.addChild(exp(nodes));

        return booleanExp;
    }

    private LinkedList<ASTNode> mergeVarNodes(LinkedList<ASTNode> nodes) {
        LinkedList<ASTNode> result = new LinkedList<>();
        while (!nodes.isEmpty()) {
            ASTNode first = nodes.peekFirst();
            if (nodes.peekFirst().getType() == Type.UNKNOWN) {
                if (isFloat(nodes)) {
                    result.add(FACTORYS.get(Type.FLOAT).build(nodes));
                } else if (isInt(first)) {
                    result.add(FACTORYS.get(Type.INT).build(nodes));
                } else if (isStr(first)) {
                    result.add(FACTORYS.get(Type.STRING).build(nodes));
                } else {
                    result.add(FACTORYS.get(Type.VAR_EXP).build(nodes));
                }
            } else {
                result.add(nodes.pollFirst());
            }
        }

        return result;
    }

    private ASTNode exp(LinkedList<ASTNode> nodes) {
        return exp(nodes, 5);
    }

    private ASTNode exp(LinkedList<ASTNode> nodes, int level) {
        if (level == 0) {
            return param(nodes);
        } else {
            ASTNode exp = exp(nodes, level - 1);
            while (!nodes.isEmpty() && OP_LEVELS.get(level).contains(nodes.peekFirst().getType())) {
                ASTNode op = op(nodes);
                ASTNode p = exp(nodes, level - 1);

                op.addChild(exp).addChild(p);
                op.setEvalFunc(OP_EVAL_FUNC);

                exp = op;
            }
            return exp;
        }
    }

    private static List<Set<Type>> OP_LEVELS = new ArrayList<>();

    static {
        // level 0
        OP_LEVELS.add(new HashSet<>());

        HashSet<Type> level = new HashSet<>();
        level.add(Type.MUL);
        level.add(Type.DIV);
        level.add(Type.MOD);
        OP_LEVELS.add(level);

        level = new HashSet<>();
        level.add(Type.ADD);
        level.add(Type.SUB);
        OP_LEVELS.add(level);

        level = new HashSet<>();
        level.add(Type.EQ);
        level.add(Type.NEQ);
        level.add(Type.LESS);
        level.add(Type.GREATER);
        level.add(Type.LE);
        level.add(Type.GE);
        OP_LEVELS.add(level);

        level = new HashSet<>();
        level.add(Type.AND);
        OP_LEVELS.add(level);

        level = new HashSet<>();
        level.add(Type.OR);
        OP_LEVELS.add(level);
    }

    private ASTNode param(LinkedList<ASTNode> nodes) {
        switch (nodes.peekFirst().getType()) {
            case VAR_EXP:
            case INT:
            case FLOAT:
            case STRING:
                return nodes.pollFirst();
            case LB:
                LinkedList<ASTNode> subnodes = collectSubNodes(nodes);
                return exp(subnodes);
            case NOT:
                ASTNode not = nodes.pollFirst();
                not.setEvalFunc(OP_EVAL_FUNC);
                not.addChild(nodes.pollFirst());

                return not;
            case SUB:
                ASTNode sub = nodes.pollFirst();
                sub.setType(Type.NEG);
                sub.setEvalFunc(OP_EVAL_FUNC);
                sub.addChild(nodes.pollFirst());

                return sub;
            default:
                throw new TemplateParsingException();
        }
    }

    private ASTNode op(LinkedList<ASTNode> nodes) {
        ASTNode op = nodes.pollFirst();
        op.setEvalFunc(OP_EVAL_FUNC);

        return op;
    }

    private LinkedList<ASTNode> collectSubNodes(LinkedList<ASTNode> nodes) {
        int stack = 0;
        LinkedList<ASTNode> subnodes = new LinkedList<>();
        while (!nodes.isEmpty()) {
            ASTNode n = nodes.pollFirst();
            switch (n.getType()) {
                case LB:
                    stack++;
                    break;
                case RB:
                    stack--;
                    break;
            }
            subnodes.add(n);
            
            if (stack == 0) {
                subnodes.pollFirst();
                subnodes.pollLast();
                return subnodes;
            }
        }
        throw new TemplateParsingException("Brackets error in expression", nodes.peekFirst());
    }
    private static final EvalFunc BOOLEAN_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            return current.getChildren().get(0).eval(context);
        }
    };

    private static final EvalFunc OP_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            switch (current.getType()) {
                case ADD:
                case SUB:
                case MUL:
                case DIV:
                case MOD:

                case EQ:
                case NEQ:
                case LESS:
                case GREATER:
                case LE:
                case GE:

                case AND:
                case OR:
                    Object arg1 = getChildValue(context, current, 1);
                    Object arg0 = getChildValue(context, current, 0);
                    return Operators.apply(arg0, arg1, current);

                case NOT:
                case NEG:
                    return Operators.apply(getChildValue(context, current, 0), current);
                default:
                    throw new UnsupportedOperationException("Operation " + current.getType() + " is not supported");
            }
        }

        private Object getChildValue(RenderContext context, ASTNode current, int index) {
            if (current.getChildren().size() <= index) {
                throw new TemplateParsingException("This operation should be applied on " + (index + 1) + " varaibles", current);
            }
            return current.getChildren().get(index).eval(context);
        }

    };
}
