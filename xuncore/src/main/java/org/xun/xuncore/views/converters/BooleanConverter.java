package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class BooleanConverter implements ParameterConverter {

    @Override
    public Object convert(String[] parameter) throws ConvertingException {
        return Boolean.parseBoolean(parameter[0]);
    }

    @Override
    public Class[] getTargetTypes() {
        return new Class[]{Boolean.class, boolean.class};
    }

}
