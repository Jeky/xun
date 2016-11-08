package org.xun.xuntemplate.ast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
public class FieldNodeFactory extends ASTNodeFactory {

    @Override
    public ASTNode build(LinkedList<ASTNode> nodes) {
        ASTNode n = nodes.pollFirst();
        n.setEvalFunc(FIELD_EVAL_FUNC);
        n.setType(Type.FIELD);

        return n;
    }

    private static final EvalFunc FIELD_EVAL_FUNC = new EvalFunc() {
        @Override
        public Object eval(RenderContext context, ASTNode current) {
            Object target = current.getChildren().get(0).eval(context);
            String name = current.getValue();
            if (target instanceof Map) {
                Map<String, Object> model = (Map<String, Object>) target;
                return model.get(name);
            } else {
                try { // try using getter
                    String methodName = "get" + Character.toUpperCase(name.charAt(0)) + name.substring(1);
                    Method method = target.getClass().getDeclaredMethod(methodName);
                    return method.invoke(target);
                } catch (Exception ex) {
                    try { // try directly accessing to field
                        Field field = target.getClass().getDeclaredField(name);
                        field.setAccessible(true);
                        return field.get(target);
                    } catch (NoSuchFieldException | SecurityException e) {
                        throw new TemplateRenderingException("Cannot find the field: " + target.getClass() + "." + name, current);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        throw new TemplateRenderingException("Cannot access the field: " + target.getClass() + "." + name, e, current);
                    }
                }
            }
        }
    };

}
