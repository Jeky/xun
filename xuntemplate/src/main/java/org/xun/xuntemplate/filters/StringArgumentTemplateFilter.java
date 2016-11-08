package org.xun.xuntemplate.filters;

import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;
import org.xun.xuntemplate.TemplateRenderingException;


/**
 *
 * @author Jeky
 */
public abstract class StringArgumentTemplateFilter implements TemplateFilter {

    public StringArgumentTemplateFilter(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public Object process(RenderContext context, ASTNode current, Object target, Object[] args) {
        if (args.length == 0) {
            throw new TemplateRenderingException(filterName + " should be given a argument", current);
        }

        if (args[0] instanceof String) {
            return process(context, current, target, (String) args[0]);
        } else {
            throw new TemplateRenderingException("The type of the argument of " + filterName + " should be String", current);
        }
    }

    protected abstract Object process(RenderContext context, ASTNode current, Object target, String arg);
    protected String filterName;
}
