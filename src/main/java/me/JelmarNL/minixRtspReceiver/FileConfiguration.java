package me.JelmarNL.minixRtspReceiver;

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
        file = new File(initDir + "/" + config + ".config");
        if (!file.exists() && file.canWrite()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Failed to create config file at " + initDir + "/" + config + ".config");
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
            System.err.println("Failed to read config file at " + initDir + "/" + config + ".config");
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
                System.err.println("Failed to write to config file at " + file.getAbsolutePath());
            }
        }
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getOrDefault(key, defaultValue);
    }
}
