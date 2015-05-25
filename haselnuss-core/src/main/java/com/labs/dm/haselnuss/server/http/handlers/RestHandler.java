package com.labs.dm.haselnuss.server.http.handlers;

import com.labs.dm.haselnuss.server.ConnectionPool;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by daniel on 2015-05-23.
 */
public class RestHandler extends AbstractHttpHandler {

    private static final Logger logger = Logger.getLogger(RestHandler.class.getSimpleName());
    private final ConnectionPool pool;

    public RestHandler(ConnectionPool storage) {
        this.pool = storage;
    }

    @Override
    public void onGet(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");

        try (OutputStream responseBody = exchange.getResponseBody()) {
            String[] paths = decodeURL(exchange);
            String storage = paths[1];
            String key = paths[2];
            Serializable val = pool.get(storage).get(key);

            if (val != null) {
                exchange.sendResponseHeaders(200, 0);
                if (val instanceof String) {
                    byte[] b = ((String) val).getBytes();
                    responseBody.write(b);

                }
            } else {
                exchange.sendResponseHeaders(404, 0);
                responseBody.write("Error 404 - Not Found!".getBytes());
            }
        }
    }

    @Override
    public void onPost(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");

        String[] paths = decodeURL(exchange);
        String storage = paths[1];
        String key = paths[2];
        String value = decodeInputStream(exchange);
        pool.get(storage).put(key, value);
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write("POST OK".getBytes());
        }
        logger.info("Insert " + key + ", " + pool.get("test").get(key));
        pool.get(storage).flush();
    }

    @Override
    public void onPut(HttpExchange exchange) throws IOException {
        Headers responseHeaders = exchange.getResponseHeaders();
        responseHeaders.set("Content-Type", "text/plain");
        String[] paths = decodeURL(exchange);
        String storage = paths[1];
        String key = paths[2];
        String value = decodeInputStream(exchange);
        pool.get(storage).set(key, value);
        exchange.sendResponseHeaders(200, 0);

        try (OutputStream responseBody = exchange.getResponseBody()) {
            responseBody.write("PUT OK".getBytes());
        }

        pool.get(storage).flush();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        logger.log(Level.INFO, "[{0}] Request: {1}", new Object[]{new Date(), exchange.getRequestURI()});
        super.handle(exchange);
    }

    private String decodeInputStream(HttpExchange he) throws IOException {
        InputStream in = he.getRequestBody();
        InputStreamReader is = new InputStreamReader(in);
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(is);
        String read = br.readLine();

        while (read != null) {
            sb.append(read);
            read = br.readLine();
        }

        return sb.toString();
    }

    private String[] decodeURL(HttpExchange he) {
        String path = he.getRequestURI().getPath();
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        String[] paths = path.split("/");
        if (paths.length < 3) {
            throw new IllegalArgumentException("Rest URL is invalid");
        }

        return paths;
    }
}
