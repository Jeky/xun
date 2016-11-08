
package org.xun.xuncore.views.converters;

/**
 *
 * @author Jeky
 */
public class StringConverter implements ParameterConverter{

    @Override
    public Object convert(String[] parameter) throws ConvertingException {
        return parameter[0];
    }

    @Override
    public Class[] getTargetTypes() {
        return new Class[]{String.class};
    }
    
}
