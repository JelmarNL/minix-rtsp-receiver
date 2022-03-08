package me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.JelmarNL.minixRtspReceiver.Main;

import java.io.IOException;
import java.io.OutputStream;

public class EndpointAudio {
    
    public static class GetAudioInput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            
        }
    }

    public static class SetAudioInput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

        }
    }

    public static class GetAudioOutput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

        }
    }

    public static class SetAudioOutput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

        }
    }
    
    public static class RestartAudio implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {

        }
    }

    public static class GetAudioRepeaterStatus implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String status = Main.audioRepeater.getStatus();
            exchange.sendResponseHeaders(200, status.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(status.getBytes());
            responseBody.close();
        }
    }
}
