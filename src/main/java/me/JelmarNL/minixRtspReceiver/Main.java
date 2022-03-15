package me.JelmarNL.minixRtspReceiver;

import javafx.application.Platform;
import me.JelmarNL.minixRtspReceiver.Audio.AudioRepeater;
import me.JelmarNL.minixRtspReceiver.Video.RtspPlayer;
import me.JelmarNL.minixRtspReceiver.WebConfiguration.Webserver;
import me.JelmarNL.minixRtspReceiver.util.Logger;

import java.io.IOException;
import java.time.Instant;

public class Main {
    public static Webserver webserver;
    public static AudioRepeater audioRepeater;
    public static RtspPlayer rtspPlayer;
    public static Instant appStartTime;
    
    public static void main(String[] args) {
        System.out.println("[--:--:--][INFO][Main]: Loading application...");
        //Start time
        appStartTime = Instant.now();
        //Start webserver
        try {
            webserver = new Webserver();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Main", "Failed to start webserver, exiting application.");
            return;
        }
        //Start webserver
        webserver.start();
        //Start audio repeater
        audioRepeater = new AudioRepeater();
        audioRepeater.start();
        //Start stream client
        rtspPlayer = new RtspPlayer();
        rtspPlayer.start();
        //Run console or wait on web interface
        Logger.info("Main", "Application loaded");
    }
    
    public static void reboot() {
        Logger.info("Main", "Stopping services...");
        //Webserver
        webserver.end();
        //AudioRepeater
        audioRepeater.end();
        //Stream client
        rtspPlayer.end();
        Platform.exit();
        Logger.info("Main", "Services stopped.");
        Runtime r = Runtime.getRuntime();
        try {
            r.exec("shutdown -r -f -t 0");
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Main", "Could not reboot pc");
        }
    }
}
