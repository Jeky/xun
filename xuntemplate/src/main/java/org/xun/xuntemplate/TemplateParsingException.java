package org.xun.xuntemplate;

/**
 *
 * @author Jeky
 */
public class TemplateParsingException extends RuntimeException {

    private ASTNode t;

    public TemplateParsingException(String msg) {
        super(msg);
    }

    public TemplateParsingException(String msg, ASTNode t) {
        super(msg);
        this.t = t;
    }

    public TemplateParsingException(String msg, Throwable ex, ASTNode t) {
        super(msg, ex);
        this.t = t;
    }

    public TemplateParsingException() {
    }

    public ASTNode getT() {
        return t;
    }

}
