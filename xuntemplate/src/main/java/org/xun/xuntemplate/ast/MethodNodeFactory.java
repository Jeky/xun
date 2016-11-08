package org.xun.xuntemplate.ast;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.EvalFunc;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateRenderingException;
import org.xun.xuntemplate.Type;

/**
 *
 * @author Jeky
 */
public class MethodNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode method = nodes.pollFirst();
        method.setEvalFunc(METHOD_EVAL_FUNC);
        method.setType(Type.METHOD);

        return method;
    }

    private static final EvalFunc METHOD_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            Object target = current.getChildren().get(0).eval(context);
            String methodName = current.getValue();
            try {
                Method method = target.getClass().getDeclaredMethod(methodName);
                return method.invoke(target);
            } catch (NoSuchMethodException | SecurityException ex) {
                throw new TemplateRenderingException("Cannot access the method: " + target.getClass() + "." + methodName + "()", current);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                throw new TemplateRenderingException("Cannot invoke the method: " + target.getClass() + "." + methodName + "()", ex, current);
            }
        }
    };
}
