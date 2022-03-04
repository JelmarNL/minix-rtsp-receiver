package me.JelmarNL.minixRtspReceiver.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
    public static void info(String module, String text) {
        System.out.println(getTimeStamp() + "[INFO][" + module + "]: " + text);
    }
    public static void warning(String module, String text) {
        System.out.println(getTimeStamp() + "[WARN][" + module + "]: " + text);
    }
    public static void error(String module, String text) {
        System.out.println(getTimeStamp() + "[ERRO][" + module + "]: " + text);
    }
    
    public static void debug(String text) {
        System.out.println(getTimeStamp() + "[INFO][DEBUG]: " + text);
    }
    
    private static String getTimeStamp() {
        return "[" + new SimpleDateFormat("HH.mm.ss").format(new Date()) + "]";
    }
}
