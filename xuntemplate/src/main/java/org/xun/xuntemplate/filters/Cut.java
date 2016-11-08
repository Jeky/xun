package org.xun.xuntemplate.filters;

import org.xun.xuntemplate.ASTNode;
import org.xun.xuntemplate.RenderContext;

/**
 *
 * @author Jeky
 */
public class Cut extends StringArgumentTemplateFilter {

    public Cut() {
        super("Cut");
    }

    @Override
    protected Object process(RenderContext context, ASTNode current, Object target, String arg) {
        return target.toString().replace(arg.toString(), "");
    }

}
