package me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.JelmarNL.minixRtspReceiver.Main;
import me.JelmarNL.minixRtspReceiver.Video.RtspPlayer;
import me.JelmarNL.minixRtspReceiver.util.FileConfiguration;
import me.JelmarNL.minixRtspReceiver.util.Logger;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

public class EndpointVideo {
    public static class RestartVideo implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String[] parts = exchange.getRequestURI().getQuery().split("&");
            String cameraIp = new String(Base64.getDecoder().decode(parts[0].split("=", 2)[1]));
            String setupCommands = parts[1].split("=", 2)[1];
            String streamUrl = new String(Base64.getDecoder().decode(parts[2].split("=", 2)[1]));
            Logger.info("RtspStream", "Restarting RTSP player...");
            Main.rtspPlayer.end();
            FileConfiguration config = new FileConfiguration("rtsp");
            config.setProperty("cameraIp", cameraIp);
            config.setProperty("setupCommands", setupCommands);
            config.setProperty("streamUrl", streamUrl);
            Main.rtspPlayer = new RtspPlayer();
            Main.rtspPlayer.start();
            String responseString = "OK";
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }

    public static class GetVideoConfig implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            FileConfiguration config = new FileConfiguration("rtsp");
            String cameraIp = Base64.getEncoder().encodeToString(config.getProperty("cameraIp", "0.0.0.0:554").getBytes());
            String setupCommands = config.getProperty("setupCommands", "");
            String streamUrl = Base64.getEncoder().encodeToString(config.getProperty("streamUrl", "rtsp://%ip%/mediainput/h264/stream_1").getBytes());
            String responseString = cameraIp + "||" + setupCommands + "||" + streamUrl;
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }

    public static class GetVideoStatus implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String responseString = Main.rtspPlayer.getStatus();
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }

    public static class GetNetworkScan implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            //TODO: Scan network for cameras
            String responseString = "Not implemented";
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
    }
}
