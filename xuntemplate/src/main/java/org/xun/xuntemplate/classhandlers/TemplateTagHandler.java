package org.xun.xuntemplate.classhandlers;

import java.util.HashMap;
import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuncore.core.SettingsException;
import org.xun.xuncore.reflect.ClassHandler;
import org.xun.xuncore.views.converters.ParameterConverter;
import org.xun.xuntemplate.tags.BlockTemplateTag;
import org.xun.xuntemplate.tags.BranchTemplateTag;
import org.xun.xuntemplate.tags.SimpleTemplateTag;
import org.xun.xuntemplate.tags.TemplateTag;

/**
 *
 * @author Jeky
 */
public class TemplateTagHandler implements ClassHandler {

    public static final String TEMPLATE_TAGS = "TEMPLATE_TAGS";

    public TemplateTagHandler() {
        DefaultSettings settings = DefaultSettings.getSettings();
        if (!settings.containsKey(TEMPLATE_TAGS)) {
            settings.setSetting(TEMPLATE_TAGS, new HashMap<String, TemplateTag>());
        }
        templateTags = (Map<String, TemplateTag>) settings.getSettingValue(TEMPLATE_TAGS);
    }

    @Override
    public boolean canHandler(Class c) {
        return TemplateTag.class.isAssignableFrom(c)
            && !c.equals(TemplateTag.class)
            && !c.equals(SimpleTemplateTag.class)
            && !c.equals(BlockTemplateTag.class)
            && !c.equals(BranchTemplateTag.class);
    }

    @Override
    public void handleClass(Class c) throws SettingsException {
        try {
            TemplateTag tag = (TemplateTag) c.newInstance();
            templateTags.put(tag.getTagName(), tag);
        } catch (InstantiationException | IllegalAccessException ex) {
            throw new SettingsException("Cannot intialize new instance of " + c, ex);
        }
    }

    private final Map<String, TemplateTag> templateTags;
}
