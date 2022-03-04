package me.JelmarNL.minixRtspReceiver;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Webserver {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/", new IndexHandler());
        server.createContext("/resources/", new ResourceHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }
    
    private static class IndexHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            ClassLoader classLoader = getClass().getClassLoader();
            URI uri;
            try {
                uri = classLoader.getResource("index.html").toURI();
            } catch (URISyntaxException | NullPointerException e) {
                String error = "Page not available";
                exchange.sendResponseHeaders(400, error.length());
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(error.getBytes());
                responseBody.close();
                return;
            }
            List<String> strings = Files.readAllLines(Path.of(uri));
            int length = 0;
            for (String line : strings) {
                length += line.length();
            }
            exchange.sendResponseHeaders(200, length);
            OutputStream responseBody = exchange.getResponseBody();
            for (String line : strings) {
                responseBody.write(line.getBytes());
            }
            responseBody.close();
        }
    }
    
    private static class ResourceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            InputStream file = getResourceAsStream(path);
            if (file != null) {
                exchange.sendResponseHeaders(200, file.available());
                OutputStream responseBody = exchange.getResponseBody();
                file.transferTo(responseBody);
                responseBody.close();
            } else {
                String message = "<h1>File not found</h1>";
                exchange.sendResponseHeaders(404, message.length());
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(message.getBytes());
                responseBody.close();
            }
        }
    }
    
    @Nullable
    static InputStream getResourceAsStream(String path) {
        return Webserver.class.getResourceAsStream(path);
    }
    
    @Nullable
    static String getResourceAsLines(String path) {
        URL url = Webserver.class.getResource(path);
        if (url == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            List<String> strings = Files.readAllLines(Path.of(url.toURI()));
            for (String string : strings) {
                sb.append(string).append("\n");
            }
        } catch (URISyntaxException | IOException e) {
            return null;
        }
        return sb.toString();
    }
}
