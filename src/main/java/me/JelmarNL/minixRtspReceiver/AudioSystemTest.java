package me.JelmarNL.minixRtspReceiver;

import javax.sound.sampled.*;
import java.util.HashMap;

public class AudioSystemTest {
    public static void main(String[] args) {
        getInputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().toString() + " - " + line.getLineInfo().toString()));
        getOutputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().toString() + " - " + line.getLineInfo().toString()));
    }
    
    private static HashMap<Mixer, Line> getInputDevices() {
        HashMap<Mixer, Line> inputLines = new HashMap<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : mixer.getSourceLineInfo()) {
                try {
                    Line sourceLine = mixer.getLine(lineInfo);
                    if (lineInfo.matches(Port.Info.MICROPHONE) || lineInfo.matches(Port.Info.LINE_IN)) {
                        inputLines.put(mixer, sourceLine);
                    }
                } catch (LineUnavailableException e) {
                    System.out.println("A source line is not available");
                }
            }
        }
        return inputLines;
    }
    private static HashMap<Mixer, Line> getOutputDevices() {
        HashMap<Mixer, Line> outputLines = new HashMap<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {
                try {
                    Line targetLine = mixer.getLine(lineInfo);
                    if (lineInfo.matches(Port.Info.SPEAKER) || lineInfo.matches(Port.Info.HEADPHONE) || lineInfo.matches(Port.Info.LINE_OUT)) {
                        outputLines.put(mixer, targetLine);
                    }
                } catch (LineUnavailableException e) {
                    System.out.println("A target line is not available");
                }
            }
        }
        return outputLines;
    }
}
