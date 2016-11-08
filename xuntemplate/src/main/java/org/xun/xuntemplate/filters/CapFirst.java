package org.xun.xuntemplate.filters;

import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;
import org.xun.xuntemplate.TemplateFilter;


/**
 *
 * @author Jeky
 */
public class CapFirst implements TemplateFilter {

    @Override
    public Object process(RenderContext context, ASTNode current, Object target, Object[] args) {
        String s = target.toString();
        if (s.isEmpty()) {
            return s;
        } else {
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
        }
    }

}
