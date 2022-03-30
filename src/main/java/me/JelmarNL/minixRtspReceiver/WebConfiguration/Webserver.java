package me.JelmarNL.minixRtspReceiver.WebConfiguration;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints.EndpointAudio;
import me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints.EndpointDevice;
import me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints.EndpointVideo;
import me.JelmarNL.minixRtspReceiver.util.Logger;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Webserver extends Thread {
    private final HttpServer server;
    
    public Webserver() throws IOException {
        server = HttpServer.create(new InetSocketAddress(80), 0);
        //Index, resources, stylesheets and js
        server.createContext("/", new ResourceHandler());
        //Device endpoints
        server.createContext("/endpoints/device/getuptime", new EndpointDevice.GetDeviceUptime());
        server.createContext("/endpoints/device/getconsole", new EndpointDevice.GetDeviceConsole());
        server.createContext("/endpoints/device/restartdevice", new EndpointDevice.RestartDevice());
        //Audio repeater endpoints
        server.createContext("/endpoints/audio/getaudioinput", new EndpointAudio.GetAudioInput());
        server.createContext("/endpoints/audio/getaudiooutput", new EndpointAudio.GetAudioOutput());
        server.createContext("/endpoints/audio/getbuffer", new EndpointAudio.GetRepeaterSize());
        server.createContext("/endpoints/audio/restartaudio", new EndpointAudio.RestartAudio());
        server.createContext("/endpoints/audio/getaudiorepeaterstatus", new EndpointAudio.GetAudioRepeaterStatus());
        //RTSP Client endpoints
        server.createContext("/endpoints/video/getvideoconfig", new EndpointVideo.GetVideoConfig());
        server.createContext("/endpoints/video/restartvideo", new EndpointVideo.RestartVideo());
        server.createContext("/endpoints/video/getvideostatus", new EndpointVideo.GetVideoStatus());
        server.createContext("/endpoints/video/getdetectedcameras", new EndpointVideo.GetNetworkScan());
        //Executor
        server.setExecutor(null); // creates a default executor
    }
    
    @Override
    public void run() {
        server.start();
    }
    
    public void end() {
        server.stop(5);
    }
    
    private static class ResourceHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equalsIgnoreCase("/")) {
                path = "/resources/index.html";
            }
            //Logger.info("HTTP", "GET: " + path);
            InputStream file = getResourceAsStream(path);
            if (file != null) {
                exchange.sendResponseHeaders(200, file.available());
                OutputStream responseBody = exchange.getResponseBody();
                file.transferTo(responseBody);
                responseBody.close();
                file.close();
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
        InputStream inputStream = Webserver.class.getResourceAsStream(path);
        if (inputStream == null) {
            Logger.info("HTTP", "Page not found: " + path);
        }
        return inputStream;
    }
    
    @Nullable
    static String getResourceAsLines(String path) {
        Logger.debug("Loading lines");
        //URL url = Webserver.class.getResource(path);
        ClassLoader classLoader = Webserver.class.getClassLoader();
        File file;
        try {
            file = new File(classLoader.getResource(path).getFile());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
        if (!file.exists()) {
            Logger.info("HTTP", "Page not found: " + path);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            List<String> strings = Files.readAllLines(Path.of(file.toURI()));
            for (String string : strings) {
                sb.append(string).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.info("HTTP", "Cannot read lines from " + path);
            return null;
        }
        return sb.toString();
    }
}
