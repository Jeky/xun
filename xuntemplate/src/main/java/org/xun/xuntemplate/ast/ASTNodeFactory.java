package org.xun.xuntemplate.ast;

import java.util.*;
import java.util.regex.Pattern;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.Type;
/**
 *
 * @author Jeky
 */
public abstract class ASTNodeFactory {

    public abstract ASTNode build(LinkedList<ASTNode> nodes);

    protected ASTNode buildByNodes(LinkedList<ASTNode> nodes, Type type, int len) {
        LinkedList<ASTNode> sub = new LinkedList<>();
        for (int i = 0; i < len; i++) {
            sub.add(nodes.pollFirst());
        }
        return FACTORYS.get(type).build(sub);
    }

    protected boolean isVar(ASTNode node) {
        return VAR_PATTERN.matcher(node.getValue()).matches();
    }

    protected boolean isInt(ASTNode node) {
        return INT_PATTERN.matcher(node.getValue()).matches();
    }

    protected boolean isFloat(LinkedList<ASTNode> nodes) {
        return nodes.size() >= 3 && isInt(nodes.peekFirst()) && nodes.get(1).getType() == Type.METHOD_START && isInt(nodes.get(2));
    }

    protected boolean isStr(ASTNode node) {
        return STR_PATTERN.matcher(node.getValue()).matches();
    }
    public static final Map<Type, ASTNodeFactory> FACTORYS = new HashMap<>();

    static {
        FACTORYS.put(Type.HTML, new HtmlNodeFactory());
        FACTORYS.put(Type.EXP, new ExpNodeFactory());
        FACTORYS.put(Type.VAR_EXP, new VarExpNodeFactory());
        FACTORYS.put(Type.METHOD, new MethodNodeFactory());
        FACTORYS.put(Type.FIELD, new FieldNodeFactory());
        FACTORYS.put(Type.VAR, new VarNodeFactory());
        FACTORYS.put(Type.FILTER, new FilterNodeFactory());
        FACTORYS.put(Type.INT, new IntNodeFactory());
        FACTORYS.put(Type.FLOAT, new FloatNodeFactory());
        FACTORYS.put(Type.STRING, new StrNodeFactory());
        FACTORYS.put(Type.BOOLEAN_EXP, new BooleanExpFactory());
    }
    protected static final EvalFunc VALUE_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            return current.getValue();
        }
    };

    protected static final EvalFunc CHILDREN_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            StringBuilder buf = new StringBuilder();
            for (ASTNode n : current.getChildren()) {
                buf.append(n.eval(context));
            }
            return buf.toString();
        }
    };
    protected static final Pattern INT_PATTERN = Pattern.compile("\\d+");
    protected static final Pattern FLOAT_PATTERN = Pattern.compile("\\d+\\.\\d+");
    protected static final Pattern STR_PATTERN = Pattern.compile("\".*\"");
    protected static final Pattern VAR_PATTERN = Pattern.compile("[A-Za-z_][A-Za-z_0-9]*");
}
