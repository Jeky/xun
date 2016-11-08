package org.xun.xuncore.classhandlers;

import java.util.HashMap;
import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuncore.core.SettingsException;
import org.xun.xuncore.reflect.ClassHandler;
import org.xun.xuncore.views.converters.ParameterConverter;

/**
 *
 * @author Jeky
 */
public class ConverterHandler implements ClassHandler {

    public static final String PARAMETER_CONVERTER = "PARAMETER_CONVERTER";

    public ConverterHandler() {
        DefaultSettings settings = DefaultSettings.getSettings();
        if (!settings.containsKey(PARAMETER_CONVERTER)) {
            settings.setSetting(PARAMETER_CONVERTER, new HashMap<Class, ParameterConverter>());
        }
        parameterConverters = (Map<Class, ParameterConverter>) settings.getSettingValue(PARAMETER_CONVERTER);
    }

    @Override
    public boolean canHandler(Class c) {
        return ParameterConverter.class.isAssignableFrom(c) && !c.equals(ParameterConverter.class);
    }

    @Override
    public void handleClass(Class c) throws SettingsException {
        try {
            ParameterConverter converter = (ParameterConverter) c.newInstance();
            for (Class targetCls : converter.getTargetTypes()) {
                parameterConverters.put(targetCls, converter);
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SettingsException("Cannot initialize ParameterConverter: " + c, ex);
        }
    }
    
    private final Map<Class, ParameterConverter> parameterConverters;
}
