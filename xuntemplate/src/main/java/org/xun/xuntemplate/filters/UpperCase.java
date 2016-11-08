package org.xun.xuntemplate.filters;

import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;


/**
 *
 * @author Jeky
 */
public class UpperCase implements TemplateFilter {

    @Override
    public Object process(RenderContext context, ASTNode current, Object target, Object[] args) {
        return target.toString().toUpperCase();
    }

}
