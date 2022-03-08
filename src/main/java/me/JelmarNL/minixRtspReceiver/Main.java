package me.JelmarNL.minixRtspReceiver;

import me.JelmarNL.minixRtspReceiver.Audio.AudioRepeater;
import me.JelmarNL.minixRtspReceiver.WebConfiguration.Webserver;
import me.JelmarNL.minixRtspReceiver.util.Logger;

import java.io.IOException;

public class Main {
    private static Webserver webserver;
    public static AudioRepeater audioRepeater;
    
    public static void main(String[] args) {
        //Start webserver
        try {
            webserver = new Webserver();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Main", "Failed to start webserver, exiting application.");
            return;
        }
        webserver.start();
        //Start audio repeater
        audioRepeater = new AudioRepeater();
        audioRepeater.start();
        //Start stream client
        
        //Run console or wait on web interface
    }
    
    private void reboot() {
        Logger.info("Main", "Stopping services...");
        webserver.end();
        audioRepeater.end();
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
