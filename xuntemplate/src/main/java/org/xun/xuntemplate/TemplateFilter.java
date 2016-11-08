package org.xun.xuntemplate;

/**
 *
 * @author Jeky
 */
public interface TemplateFilter {

    Object process(RenderContext context, ASTNode current, Object target, Object[] args);
}
