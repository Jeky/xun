package org.xun.xuntemplate.filters;

import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;
import org.xun.xuntemplate.TemplateRenderingException;

/**
 *
 * @author Jeky
 */
public class Add implements TemplateFilter {

    @Override
    public Object process(RenderContext context, ASTNode current, Object target, Object[] args) {
        if (args.length == 0) {
            throw new TemplateRenderingException("Add should be given a argument", current);
        }

        Object arg = args[0];
        if (target instanceof Integer) {
            if (arg instanceof Integer) {
                return ((int) target) + ((int) arg);
            } else if (arg instanceof Double) {
                return ((int) target) + ((double) arg);
            }
        } else if (target instanceof Double) {
            if (arg instanceof Integer) {
                return ((double) target) + ((int) arg);
            } else if (arg instanceof Double) {
                return ((double) target) + ((double) arg);
            }
        } else if (target instanceof String || arg instanceof String) {
            return target.toString() + arg.toString();
        }
        throw new TemplateRenderingException("Cannot add " + target + " and " + arg + " together", current);
    }

}
