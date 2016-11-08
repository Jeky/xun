package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public interface ParameterConverter {

    Object convert(String[] parameter) throws ConvertingException;

    Class[] getTargetTypes();
}
