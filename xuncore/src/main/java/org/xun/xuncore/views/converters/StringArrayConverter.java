package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class StringArrayConverter implements ParameterConverter {

    @Override
    public Object convert(String[] parameter) throws ConvertingException {
        return parameter;
    }

    @Override
    public Class[] getTargetTypes() {
        return new Class[]{String[].class};
    }

}
