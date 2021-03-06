package com.labs.dm.haselnuss.server.http;

import com.labs.dm.haselnuss.utils.Utils;
import com.labs.dm.haselnuss.server.ConnectionPool;
import com.labs.dm.haselnuss.server.http.handlers.AdminHandler;
import com.labs.dm.haselnuss.server.http.handlers.MainHandler;
import com.labs.dm.haselnuss.server.http.handlers.RestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author daniel
 */
public class RestServer {

    private static final Logger logger = Logger.getLogger(RestServer.class.getSimpleName());
    private final ConnectionPool pool = new ConnectionPool();
    private HttpServer server;
    private int port;

    public RestServer(int port) {
        this.port = port;
    }

    public static void main(String[] args) throws IOException {
        new RestServer(8081).start();
    }

    public void start() throws IOException {
        InetSocketAddress addr = new InetSocketAddress(port);
        server = HttpServer.create(addr, 0);
        server.createContext("/", new MainHandler());
        server.createContext("/admin", new AdminHandler());
        server.createContext("/rest", new RestHandler(pool));
        server.createContext("/storage", new RestHandler(pool));
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        logger.info("Server is listening on port " + port);
        logger.info("Usage: http://localhost:" + port + "/rest/key");
        logger.log(Level.INFO, "PID: {0}", Utils.pid());
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }

    }

}

