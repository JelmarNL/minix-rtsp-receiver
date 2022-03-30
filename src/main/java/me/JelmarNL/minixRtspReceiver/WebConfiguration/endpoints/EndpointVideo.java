package me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.JelmarNL.minixRtspReceiver.Main;
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
            String streamOptions = parts[1].split("=", 2)[1];
            String streamUrl = new String(Base64.getDecoder().decode(parts[2].split("=", 2)[1]));
            FileConfiguration config = new FileConfiguration("rtsp");
            config.setProperty("cameraIp", cameraIp);
            config.setProperty("streamOptions", streamOptions);
            config.setProperty("streamUrl", streamUrl);
            restartVideo();
            String responseString = "OK";
            exchange.sendResponseHeaders(200, responseString.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(responseString.getBytes());
            responseBody.close();
        }
        public static void restartVideo() {
            Logger.info("RtspStream", "Restarting RTSP player...");
            Main.rtspPlayer.restart();
            Logger.info("RtspStream", "Restarted RTSP player");
        }
    }

    public static class GetVideoConfig implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            FileConfiguration config = new FileConfiguration("rtsp");
            String cameraIp = Base64.getEncoder().encodeToString(config.getProperty("cameraIp", "0.0.0.0:554").getBytes());
            String streamOptions = config.getProperty("streamOptions", Base64.getEncoder().encodeToString(":live-caching=0\n:sout-mux-caching=10\n:network-caching=50".getBytes()));
            String streamUrl = Base64.getEncoder().encodeToString(config.getProperty("streamUrl", "rtsp://%ip%/mediainput/h264/stream_1").getBytes());
            String responseString = cameraIp + "||" + streamOptions + "||" + streamUrl;
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
