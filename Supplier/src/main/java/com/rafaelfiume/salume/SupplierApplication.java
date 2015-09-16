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

        ServerConnector http = new ServerConnector(server);
        http.setHost("localhost");
        http.setPort(8080); // TODO Get port from resource
        http.setIdleTimeout(30000); // TODO Get idleTimeout from resource

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
