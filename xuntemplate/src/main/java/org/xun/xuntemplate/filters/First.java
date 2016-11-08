package org.xun.xuntemplate.filters;

import java.util.List;
import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;
import org.xun.xuntemplate.TemplateRenderingException;

/**
 *
 * @author Jeky
 */
public class First implements TemplateFilter {
    
    @Override
    public Object process(RenderContext context, ASTNode current, Object target, Object[] args) {
        try {
            if (target instanceof List) {
                return ((List) target).get(0);
            } else if (target.getClass().isArray()) {
                return ((Object[]) target)[0];
            } else {
                throw new TemplateRenderingException("Target " + target + " is not a list or array.", current);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new TemplateRenderingException("Cannot get first from an empty list", e, current);
        }
    }
    
}
