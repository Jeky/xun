
package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class DoubleConverter implements ParameterConverter{

    @Override
    public Object convert(String[] parameter) throws ConvertingException {
        return Double.parseDouble(parameter[0]);
    }

    @Override
    public Class[] getTargetTypes() {
        return new Class[]{Double.class, double.class};
    }
    
}
