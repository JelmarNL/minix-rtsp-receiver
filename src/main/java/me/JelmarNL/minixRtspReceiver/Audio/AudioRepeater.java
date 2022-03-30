package me.JelmarNL.minixRtspReceiver.Audio;

import me.JelmarNL.minixRtspReceiver.Main;
import me.JelmarNL.minixRtspReceiver.util.FileConfiguration;
import me.JelmarNL.minixRtspReceiver.util.Logger;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioRepeater extends Thread {
    private final FileConfiguration audioConfig;
    private final TargetDataLine lineIn;
    private final SourceDataLine lineOut;
    private boolean running = true;
    private volatile boolean done = false;
    private int buffer = 2048;
    private String stoppedReason = null;

    /**
     * Load audio repeater on these lines
     */
    public AudioRepeater() {
        Logger.info("AudioRepeater", "Loading audio repeater...");
        audioConfig = new FileConfiguration("audio");
        this.lineIn = AudioDevices.getInputDeviceByName(audioConfig.getProperty("inputDevice", "null"));
        this.lineOut = AudioDevices.getOutputDeviceByName(audioConfig.getProperty("outputDevice", "null"));
        this.buffer = Integer.parseInt(audioConfig.getProperty("buffer", buffer + ""));
    }

    /**
     * Starts audio repeater loop, use audioRepeater.start();
     */
    @Override
    public void run() {
        Logger.info("AudioRepeater", "Opening audio lines...");
        if (lineIn == null || lineOut == null) {
            running = false;
            done = true;
            Logger.error("AudioRepeater", "Some lines do not exist, repeater restarting... Check if a microphone and speaker are available.");
            stoppedReason = "The previously selected audio device is no longer available. Pick and save a new one or reconnect the old device.";
            //Restart to try again
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.end();
            AudioRepeater audioRepeater = new AudioRepeater();
            Main.audioRepeater = audioRepeater;
            audioRepeater.start();
            return;
        }
        byte[] data = new byte[buffer];
        try {
            lineIn.open();
            lineOut.open();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            Logger.error("AudioRepeater", "Could not open audio lines.");
            stoppedReason = "Line not available, please reboot device after selecting lines.";
            running = false;
            done = true;
            return;
            //TODO: Test when lines not supported exception occurs and fix or automatically restart
        }
        lineIn.start();
        lineOut.start();
        lineIn.flush();
        lineOut.flush();
        Logger.info("AudioRepeater", "Audio repeater ready with buffer size " + buffer);
        while (running) {
            lineIn.read(data, 0, buffer);
            lineOut.write(data, 0, data.length);
        }
        lineIn.stop();
        lineOut.stop();
        lineIn.close();
        lineOut.close();
        done = true;
    }

    /**
     * Stop the audio repeater and release the lines.
     */
    public void end() {
        Logger.info("AudioRepeater", "Stopping audio repeater...");
        running = false;
        while (!done) Thread.onSpinWait();
        Logger.info("AudioRepeater", "Stopped audio repeater");
    }
    
    public String getStatus() {
        if (running) {
            return "Running";
        } else {
            if (stoppedReason == null) {
                return "Stopped";
            } else {
                return stoppedReason;
            }
        }
    }

    public FileConfiguration getAudioConfig() {
        return audioConfig;
    }

    public int getBuffer() {
        return buffer;
    }
    public void setBuffer(int buffer) {
        this.buffer = buffer;
    }
}
