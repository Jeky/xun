package org.xun.xuncore.core;

import java.util.EnumSet;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author Jeky
 */
public class XunServer {

    public static void main(String[] args) throws Exception {
        DefaultSettings settings = DefaultSettings.getSettings();
        int port = settings.getSettingValue("PORT", Integer.class);
        
        Server server = new Server(port);
        ServletContextHandler handler = new ServletContextHandler(server, "/");
        handler.addFilter(Dispatcher.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        
        server.start();
        server.join();
    }
}
