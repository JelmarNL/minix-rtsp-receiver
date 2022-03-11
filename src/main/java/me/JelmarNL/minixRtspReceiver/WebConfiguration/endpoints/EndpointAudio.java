package me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.JelmarNL.minixRtspReceiver.Audio.AudioDevices;
import me.JelmarNL.minixRtspReceiver.Audio.AudioRepeater;
import me.JelmarNL.minixRtspReceiver.Main;
import me.JelmarNL.minixRtspReceiver.util.Logger;

import javax.sound.sampled.Mixer;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EndpointAudio {
    
    public static class GetAudioInput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<String> devices = new ArrayList<>();
            //Get devices
            for (Mixer mixer : AudioDevices.getInputDevices().keySet()) {
                String name = mixer.getMixerInfo().getName();
                //Set the current one first, so it's selected in the dropdown
                if (Main.audioRepeater.getAudioConfig().getProperty("inputDevice", "null").equalsIgnoreCase(name)) {
                    devices.add(0, name);
                } else {
                    devices.add(name);
                }
            }
            String responseString = buildOptionList(devices);
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }

    public static class GetAudioOutput implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            List<String> devices = new ArrayList<>();
            //Get devices
            for (Mixer mixer : AudioDevices.getOutputDevices().keySet()) {
                String name = mixer.getMixerInfo().getName();
                //Set the current one first, so it's selected in the dropdown
                if (Main.audioRepeater.getAudioConfig().getProperty("outputDevice", "null").equalsIgnoreCase(name)) {
                    devices.add(0, name);
                } else {
                    devices.add(name);
                }
            }
            String responseString = buildOptionList(devices);
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }
    
    public static class RestartAudio implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String[] parts = exchange.getRequestURI().getQuery().split("&");
            String inputDevice = parts[0].split("=", 2)[1];
            String outputDevice = parts[1].split("=", 2)[1];
            Main.audioRepeater.getAudioConfig().setProperty("inputDevice", inputDevice);
            Main.audioRepeater.getAudioConfig().setProperty("outputDevice", outputDevice);
            Logger.info("AudioRepeater", "Set input device to: " + inputDevice);
            Logger.info("AudioRepeater", "Set output device to: " + outputDevice);
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
    
    private static String buildOptionList(List<String> devices) {
        StringBuilder response = new StringBuilder();
        for (String name : devices) {
            response.append("<option value=\"").append(name).append("\">").append(name).append("</option>\n");
        }
        return response.toString();
    }
}
