package org.xun.xundemo;

import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuncore.core.XunServer;

/**
 *
 * @author Jeky
 */
public class Main {

    public static void main(String[] args) {
        DefaultSettings settings = DefaultSettings.getSettings();
        System.out.println(settings.getSettingValue("VIEWS"));
        XunServer.main(args);
    }
}
