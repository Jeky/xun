
package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class FloatConverter implements ParameterConverter{

    @Override
    public Object convert(String[] parameter) throws ConvertingException {
        return Float.parseFloat(parameter[0]);
    }

    @Override
    public Class[] getTargetTypes() {
        return new Class[]{Float.class, float.class};
    }
    
}
