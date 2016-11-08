package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class ConvertingException extends RuntimeException {

    public ConvertingException(String message) {
        super(message);
    }

    public ConvertingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConvertingException(Throwable cause) {
        super(cause);
    }

}
