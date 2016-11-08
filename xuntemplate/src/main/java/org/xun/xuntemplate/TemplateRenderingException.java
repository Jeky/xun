package org.xun.xuntemplate;

/**
 *
 * @author Jeky
 */
public class TemplateRenderingException extends RuntimeException {

    private ASTNode token;

    public ASTNode getToken() {
        return token;
    }

    public TemplateRenderingException(String msg, ASTNode token) {
        super(msg);
        this.token = token;
    }

    public TemplateRenderingException(String msg, Throwable ex, ASTNode token) {
        super(msg, ex);
        this.token = token;
    }

}
