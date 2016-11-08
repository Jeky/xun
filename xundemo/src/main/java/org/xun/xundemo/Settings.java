package org.xun.xundemo;

import java.util.Map;
import org.xun.xuncore.core.DefaultSettings;

/**
 *
 * @author Jeky
 */
public class Settings extends DefaultSettings {

    @Override
    protected void initSettings(Map<String, Object> settings) {
        settings.put(PORT, 7070);
    }

}
