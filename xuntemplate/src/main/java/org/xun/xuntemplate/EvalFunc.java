package org.xun.xuntemplate;


/**
 *
 * @author Jeky
 */
public interface EvalFunc {
    
    Object eval(RenderContext context, ASTNode current);
}
