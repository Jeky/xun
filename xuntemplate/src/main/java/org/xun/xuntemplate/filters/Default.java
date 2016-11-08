package org.xun.xuntemplate.filters;

import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;

/**
 *
 * @author Jeky
 */
public class Default extends StringArgumentTemplateFilter {

    public Default() {
        super("Default");
    }

    @Override
    protected Object process(RenderContext context, ASTNode current, Object target, String arg) {
        if (target == null || target.toString().trim().equals("")) {
            return arg;
        } else {
            return target;
        }
    }

}
