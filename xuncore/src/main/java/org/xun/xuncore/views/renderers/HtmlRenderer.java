package org.xun.xuncore.views.renderers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.xun.xuncore.core.DefaultSettings;
import org.xun.xuncore.core.SettingsException;

/**
 *
 * @author Jeky
 */
public class HtmlRenderer extends Renderer {

    @Override
    public void render(Object result) {
        DefaultSettings settings = DefaultSettings.getSettings();
        Path htmlPath = Paths.get(settings.getBaseDir(),
                                  settings.getSettingValue(DefaultSettings.TEMPLATE_PATH, String.class),
                                  result.toString());

        if (Files.exists(htmlPath)) {
            try {
                String html = Files.readAllLines(htmlPath).stream().collect(Collectors.joining("\n"));
                response.renderString(html);
            } catch (IOException ex) {
                throw new SettingsException("Cannot read html file: " + result, ex);
            }
        } else {
            throw new SettingsException("Cannot find html: " + result);
        }
    }

}
