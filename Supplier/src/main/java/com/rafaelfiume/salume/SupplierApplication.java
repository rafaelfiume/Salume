package com.rafaelfiume.salume;

import com.rafaelfiume.salume.web.controllers.StatusPageController;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;

public class SupplierApplication {

    public static void main(String... args) throws Exception {

        Server server = new Server();

        String webPort = System.getenv("PORT");
        if(webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }

        ServerConnector http = new ServerConnector(server);
        http.setPort(Integer.parseInt(webPort)); // TODO Get port from resource

        // Set the connector
        server.addConnector(http);

        // Add a single handler on context "/hello"
        ContextHandler context = new ContextHandler();
        context.setContextPath("/salume/supplier/status");
        context.setHandler(new StatusPageController());

        // Handler Structure
        HandlerCollection handlers = new HandlerCollection();
        handlers.setHandlers(new Handler[]{context, new DefaultHandler()});
        server.setHandler(handlers);

        // Extra options
        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);

        // Start the server
        server.start();
//        server.join();
    }
}
