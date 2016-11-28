package org.xun.xuncore.core;

import java.util.EnumSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 *
 * @author Jeky
 */
public class XunServer {

    public XunServer() {
        settings = DefaultSettings.getSettings();
    }

    public static void main(String[] args) {
        new XunServer().run();
    }

    public void run() {
        int port = settings.getSettingValue("PORT", Integer.class);

        server = new Server(port);

        // handler static resources
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(false);
        resourceHandler.setResourceBase(settings.getSettingValue(DefaultSettings.STATIC_PATH, String.class));
        ContextHandler staticURLHandler = new ContextHandler("/" + settings.getSettingValue(DefaultSettings.STATIC_URL, String.class));
        staticURLHandler.setHandler(resourceHandler);

        // handler server requests
        ServletContextHandler contextHandler = new ServletContextHandler(server, "/");
        contextHandler.addFilter(Dispatcher.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        // combine handlers
        HandlerList handlers = new HandlerList();
        handlers.addHandler(staticURLHandler);
        handlers.addHandler(contextHandler);

        server.setHandler(handlers);

        // start server
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(XunServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Server server;
    private DefaultSettings settings;

}
