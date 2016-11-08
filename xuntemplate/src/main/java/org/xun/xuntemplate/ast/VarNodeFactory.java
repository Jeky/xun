package org.xun.xuntemplate.ast;

import java.util.LinkedList;
import java.util.Map;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateRenderingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class VarNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode var = nodes.pollFirst();
        var.setEvalFunc(VAR_EVAL_FUNC);
        var.setType(Type.VAR);

        return var;
    }

    private static final EvalFunc VAR_EVAL_FUNC = new EvalFunc() {

        @Override
        public Object eval(RenderContext context, ASTNode current) {
            String name = current.getValue();
            if (context.getModel() instanceof Map) {
                Map<String, Object> model = (Map<String, Object>) context.getModel();
                return model.get(name);
            } else if (name.equals("model")) {
                return context.getModel();
            } else if (name.equals("session")) {
                return context.getRequest().getSession();
            } else {
                throw new TemplateRenderingException("Model name error: " + name, current);
            }
        }
    };

}
