package org.xun.xuntemplate.tags;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateParsingException;
import org.xun.xuntemplate.TemplateRenderingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class For extends BlockTemplateTag {

    public For() {
        super("for", "endfor");
    }

    @Override
    public EvalFunc createTagFunc() {
        return FOR_TAG_EVAL_FUNC;
    }

    @Override
    public ASTNode buildStart(LinkedList<ASTNode> nodes) {
        ASTNode tagName = nodes.pollFirst();
        if (nodes.size() < 3) {
            throw new TemplateParsingException("Incomplete for loop expression", tagName);
        }
        tagName.setType(Type.TAG_NAME);
        tagName.setEvalFunc(FOR_LOOP_EVAL_FUNC);

        ASTNode varName = nodes.pollFirst();
        varName.setEvalFunc(VALUE_EVAL_FUNC);
        tagName.addChild(varName);

        ASTNode inNode = nodes.pollFirst();
        if (!inNode.getValue().equals("in")) {
            throw new TemplateParsingException("for loop expression error:", inNode);
        }

        ASTNode target = FACTORYS.get(Type.VAR_EXP).build(nodes);
        tagName.addChild(target);

        return tagName;
    }

    private static final EvalFunc FOR_TAG_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            return current.getChildren().get(0).eval(context);
        }
    };

    private static final EvalFunc FOR_LOOP_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            String varName = current.getChildren().get(0).getValue();
            Object loopTarget = current.getChildren().get(1).eval(context);
            // copy context to ignore variable overlaying
            Map<String, Object> mapModel;
            Map<String, Object> forloopMap = new HashMap<>();
            Object model = context.getModel();
            if (model instanceof Map) {
                mapModel = new HashMap<>((Map) model);
            } else {
                mapModel = new HashMap<>();
                mapModel.put("modal", model);
            }
            StringBuilder buf = new StringBuilder();
            Collection collection = null;
            if (loopTarget.getClass().isArray()) {
                collection = Arrays.asList((Object[]) loopTarget);
            } else if (loopTarget instanceof Collection) {
                collection = (Collection) loopTarget;
            } else {
                throw new TemplateRenderingException("Cannot loop on target: " + loopTarget, current);
            }

            Object parentLoop = mapModel.get("forloop");

            int i = 0;
            for (Object elem : collection) {
                forloopMap.put("counter", i + 1);
                forloopMap.put("counter0", i);
                forloopMap.put("revcounter", collection.size() - i);
                forloopMap.put("revcounter0", collection.size() - i - 1);
                forloopMap.put("first", i == 0);
                forloopMap.put("last", i == collection.size() - 1);
                forloopMap.put("parentloop", parentLoop);

                mapModel.put("forloop", forloopMap);
                mapModel.put(varName, elem);
                for (int j = 2; j < current.getChildren().size(); j++) {
                    buf.append(current.getChildren().get(j).eval(
                        new RenderContext(context.getRequest(), context.getResponse(), mapModel)));
                }
                i++;
            }

            current.visit(n -> {
                if (n.getType() == Type.TAG_NAME && n.getValue().equals("cycle")) {
                    ((Cycle.CycleEvalFunc) n.getEvalFunc()).reset();
                }
            });

            return buf.toString();
        }
    };
}
