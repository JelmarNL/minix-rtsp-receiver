package me.JelmarNL.minixRtspReceiver.Audio;

import org.jetbrains.annotations.Nullable;

import javax.sound.sampled.*;
import java.util.HashMap;
import java.util.Map;

public class AudioDevices {
    public static HashMap<Mixer, Line> getInputDevices() {
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
    
    @Nullable
    public static TargetDataLine getInputDeviceByName(String name) {
        for (Map.Entry<Mixer, Line> line : getInputDevices().entrySet()) {
            if (line.getKey().getMixerInfo().getName().equalsIgnoreCase(name)) {
                return (TargetDataLine) line.getValue();
            }
        }
        return null;
    }

    public static HashMap<Mixer, Line> getOutputDevices() {
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
    
    @Nullable
    public static SourceDataLine getOutputDeviceByName(String name) {
        for (Map.Entry<Mixer, Line> line : getOutputDevices().entrySet()) {
            if (line.getKey().getMixerInfo().getName().equalsIgnoreCase(name)) {
                return (SourceDataLine) line.getValue();
            }
        }
        return null;
    }
}
