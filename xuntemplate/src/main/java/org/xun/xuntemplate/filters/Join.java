package org.xun.xuntemplate.filters;

import java.util.Arrays;
import java.util.List;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateRenderingException;

/**
 *
 * @author Jeky
 */
public class Join extends StringArgumentTemplateFilter {

    public Join() {
        super("Join");
    }

    @Override
    protected Object process(RenderContext context, ASTNode current, Object target, String arg) {
        if (target instanceof List) {
            return String.join(arg, (List) target);
        } else if (target.getClass().isArray()) {
            Object[] arr = (Object[]) target;
            List l = Arrays.asList(arr);
            return String.join(arg, l);
        } else {
            throw new TemplateRenderingException("Target " + target + " is not a list or array.", current);
        }
    }

}
