package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class IntegerConverter implements ParameterConverter {

    @Override
    public Object convert(String[] parameter) throws ConvertingException {
        return Integer.parseInt(parameter[0]);
    }

    @Override
    public Class[] getTargetTypes() {
        return new Class[]{int.class, Integer.class};
    }

}
