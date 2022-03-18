package me.JelmarNL.minixRtspReceiver.util;

import me.JelmarNL.minixRtspReceiver.Main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void info(String module, String text) {
        String info = getTimeStamp() + "[INFO][" + module + "]: " + text;
        System.out.println(info);
        Main.addLog(info);
    }
    public static void warning(String module, String text) {
        String warning = getTimeStamp() + "[WARN][" + module + "]: " + text;
        System.out.println(warning);
        Main.addLog(warning);
    }
    public static void error(String module, String text) {
        String error = getTimeStamp() + "[ERRO][" + module + "]: " + text;
        System.out.println(error);
        Main.addLog(error);
    }
    
    public static void debug(String text) {
        String debug = getTimeStamp() + "[INFO][DEBUG]: " + text;
        System.out.println(debug);
        Main.addLog(debug);
    }
    
    private static String getTimeStamp() {
        return "[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "]";
    }
}
