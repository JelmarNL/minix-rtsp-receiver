package me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.JelmarNL.minixRtspReceiver.Audio.AudioDevices;
import me.JelmarNL.minixRtspReceiver.Audio.AudioRepeater;
import me.JelmarNL.minixRtspReceiver.Main;

import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.io.OutputStream;

public class EndpointAudio {
    
    public static class GetAudioInput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder response = new StringBuilder();
            for (Mixer mixer : AudioDevices.getInputDevices().keySet()) {
                String name = mixer.getMixerInfo().getName();
                response.append("<option value=\"").append(name).append("\">").append(name).append("</option>\n");
            }
            String responseString = response.toString();
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }

    public static class SetAudioInput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String device = exchange.getRequestURI().getQuery().split("=", 2)[1];
            Main.audioRepeater.getAudioConfig().setProperty("inputDevice", device);
            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        }
    }

    public static class GetAudioOutput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder response = new StringBuilder();
            for (Mixer mixer : AudioDevices.getOutputDevices().keySet()) {
                String name = mixer.getMixerInfo().getName();
                response.append("<option value=\"").append(name).append("\">").append(name).append("</option>\n");
            }
            String responseString = response.toString();
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }

    public static class SetAudioOutput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String device = exchange.getRequestURI().getQuery().split("=", 2)[1];
            Main.audioRepeater.getAudioConfig().setProperty("outputDevice", device);
            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        }
    }
    
    public static class RestartAudio implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Main.audioRepeater.end();
            Main.audioRepeater = new AudioRepeater();
            Main.audioRepeater.start();
            String response = "OK";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
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
