package org.xun.xuntemplate.classhandlers;

import java.util.HashMap;
import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuncore.core.SettingsException;
import org.xun.xuncore.reflect.ClassHandler;
import org.xun.xuntemplate.TemplateFilter;
import org.xun.xuntemplate.filters.StringArgumentTemplateFilter;

/**
 *
 * @author Jeky
 */
public class TemplateFilterHandler implements ClassHandler {

    public static final String TEMPLATE_FILTERS = "TEMPLATE_FILTERS";

    public TemplateFilterHandler() {
        DefaultSettings settings = DefaultSettings.getSettings();
        if (!settings.containsKey(TEMPLATE_FILTERS)) {
            settings.setSetting(TEMPLATE_FILTERS, new HashMap<String, TemplateFilter>());
        }
        templateFilters = (Map<String, TemplateFilter>) settings.getSettingValue(TEMPLATE_FILTERS);
    }

    @Override
    public boolean canHandler(Class c) {
        return TemplateFilter.class.isAssignableFrom(c)
            && !c.equals(TemplateFilter.class)
            && !c.equals(StringArgumentTemplateFilter.class);
    }

    @Override
    public void handleClass(Class c) throws SettingsException {
        try {
            templateFilters.put(c.getSimpleName(), (TemplateFilter) c.newInstance());
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SettingsException("Cannot intialize new instance of " + c, ex);
        }
    }

    private final Map<String, TemplateFilter> templateFilters;

}
