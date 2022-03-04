package me.JelmarNL.minixRtspReceiver;

import me.JelmarNL.minixRtspReceiver.util.FileConfiguration;
import me.JelmarNL.minixRtspReceiver.util.Logger;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioRepeater extends Thread {
    private final TargetDataLine lineIn;
    private final SourceDataLine lineOut;
    private boolean running = true;
    private volatile boolean done = false;
    private int buffer = 2048;

    /**
     * Load audio repeater on these lines
     * @param lineIn
     * @param lineOut
     */
    public AudioRepeater(TargetDataLine lineIn, SourceDataLine lineOut) {
        this.lineIn = lineIn;
        this.lineOut = lineOut;
        FileConfiguration audioConfig = new FileConfiguration("audio");
        this.buffer = Integer.parseInt(audioConfig.getProperty("buffer", buffer + ""));
        
    }

    /**
     * Starts audio repeater loop, use audioRepeater.start();
     */
    @Override
    public void run() {
        byte[] data = new byte[buffer];
        try {
            lineIn.open();
            lineOut.open();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            Logger.error("AudioRepeater", "Could not open audio lines.");
            return;
        }
        lineIn.start();
        lineOut.start();
        lineIn.flush();
        lineOut.flush();
        Logger.info("AudioRepeater", "Running with buffer size " + buffer);
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
        running = false;
        while (!done) Thread.onSpinWait();
    }
}
