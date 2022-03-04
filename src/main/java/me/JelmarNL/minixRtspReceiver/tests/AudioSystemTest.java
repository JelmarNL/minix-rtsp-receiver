package me.JelmarNL.minixRtspReceiver.tests;

import me.JelmarNL.minixRtspReceiver.AudioRepeater;

import javax.sound.sampled.*;
import java.util.HashMap;

public class AudioSystemTest {
    public static void main(String[] args) {
        getInputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().getName()));
        System.out.println();
        getOutputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().getName()));

        TargetDataLine lineIn = (TargetDataLine) getInputDevices().values().toArray()[0];
        SourceDataLine lineOut = (SourceDataLine) getOutputDevices().values().toArray()[0];
        AudioRepeater audioRepeater = new AudioRepeater(lineIn, lineOut);
        audioRepeater.run();
    }
    
    private static HashMap<Mixer, Line> getInputDevices() {
        HashMap<Mixer, Line> inputs = new HashMap<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {
                if (lineInfo.getLineClass().equals(TargetDataLine.class)) {
                    try {
                        Line targetLine = mixer.getLine(lineInfo);
                        inputs.put(mixer, targetLine);
                    } catch (LineUnavailableException e) {
                        System.out.println("A target (mic) line is not available");
                    }
                }
            }
        }
        return inputs;
    }
    private static HashMap<Mixer, Line> getOutputDevices() {
        HashMap<Mixer, Line> outputs = new HashMap<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : mixer.getSourceLineInfo()) {
                if (lineInfo.getLineClass().equals(SourceDataLine.class)) {
                    try {
                        Line targetLine = mixer.getLine(lineInfo);
                        outputs.put(mixer, targetLine);
                    } catch (LineUnavailableException e) {
                        System.out.println("A source (speaker) line is not available");
                    }
                }
            }
        }
        return outputs;
    }
}
