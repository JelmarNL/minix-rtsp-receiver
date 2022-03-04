package me.JelmarNL.minixRtspReceiver.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileConfiguration {
    private File file;
    private final HashMap<String, String> properties = new HashMap<>();
    
    public FileConfiguration(String config) {
        String initDir = System.getProperty("user.dir");
        File dir = new File(initDir + "/config/");
        file = new File(initDir + "/config/" + config + ".config");
        if (!file.exists()) {
            Logger.info("Config", "Configuration file " + config + " not found, creating it...");
            try {
                if (!dir.isDirectory()) {
                    if (dir.mkdir()) Logger.info("Config", "Created configuration directories");
                }
                boolean created = file.createNewFile();
                if (created) {
                    Logger.info("Config", "Created new config file at " + initDir + "/config/" + config + ".config");
                } else {
                    throw new IOException("Config file not created");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.error( "Config", "Failed to create config file at " + initDir + "/config/" + config + ".config");
                file = null;
                return;
            }
        }
        try {
            List<String> strings = Files.readAllLines(Path.of(file.toURI()));
            for (String string : strings) {
                String[] parts = string.split(":", 2);
                properties.put(parts[0], parts[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Logger.error("Config", "Failed to read config file at " + initDir + "/config/" + config + ".config");
            file = null;
        }
    }
    
    public void setProperty(String key, String value) {
        properties.put(key, value);
        rewrite();
    }
    
    private void rewrite() {
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                for (Map.Entry<String, String> entry : properties.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
                Logger.error("Config", "Failed to write to config file at " + file.getAbsolutePath());
            }
        }
    }
    
    public String getProperty(String key, String defaultValue) {
        String value = properties.getOrDefault(key, defaultValue);
        setProperty(key, value);
        return value;
    }
}
