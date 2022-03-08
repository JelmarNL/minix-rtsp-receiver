package me.JelmarNL.minixRtspReceiver.WebConfiguration.endpoints;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import me.JelmarNL.minixRtspReceiver.Main;

import java.io.IOException;
import java.io.OutputStream;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

public class EndpointDevice {
    public static class GetDeviceUptime implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Instant now = Instant.now();
            Duration uptime = Duration.between(Main.appStartTime, now);
            //TODO: Numberformat 01 instead of 1
            String response = uptime.toDaysPart() + ":" + String.format("%02d", uptime.toHoursPart()) + ":" + String.format("%02d", uptime.toMinutesPart()) + ":" + String.format("%02d", uptime.toSecondsPart());
            exchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        }
    }

    public static class GetDeviceConsole implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder builder = new StringBuilder();
            List<String> log = Main.webserver.getLog();
            for (String entry : log) {
                builder.append("<p>").append(entry).append("</p>\n");
            }
            String response = builder.toString();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream responseBody = exchange.getResponseBody();
            responseBody.write(response.getBytes());
            responseBody.close();
        }
    }
}
