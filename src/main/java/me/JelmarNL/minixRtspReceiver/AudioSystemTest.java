package me.JelmarNL.minixRtspReceiver;

import javax.sound.sampled.*;
import java.util.HashMap;

public class AudioSystemTest {
    public static void main(String[] args) {
        getInputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().toString() + " - " + line.getLineInfo().toString()));
        System.out.println();
        getOutputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().toString() + " - " + line.getLineInfo().toString()));
    }
    
    private static HashMap<Mixer, Line> getInputDevices() {
        HashMap<Mixer, Line> inputLines = new HashMap<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : mixer.getSourceLineInfo()) {
                try {
                    Line sourceLine = mixer.getLine(lineInfo);
                    inputLines.put(mixer, sourceLine);
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
            System.out.println(mixerInfo.toString());
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            for (Line.Info lineInfo : mixer.getTargetLineInfo()) {
                try {
                    Line targetLine = mixer.getLine(lineInfo);
                    logType(lineInfo);
                    outputLines.put(mixer, targetLine);
                } catch (LineUnavailableException e) {
                    System.out.println("A target line is not available");
                }
            }
        }
        return outputLines;
    }
    
    private static void logType(Line.Info lineInfo) {
        System.out.print("- " + lineInfo.toString() + "(" + ((Port.Info) lineInfo).getName() + ")" + " = ");
        if (lineInfo.matches(Port.Info.LINE_OUT)) {
            System.out.println("LINE_OUT");
        } else if (lineInfo.matches(Port.Info.LINE_IN)) {
            System.out.println("LINE_IN");
        } else if (lineInfo.matches(Port.Info.HEADPHONE)) {
            System.out.println("HEADPHONE");
        } else if (lineInfo.matches(Port.Info.SPEAKER)) {
            System.out.println("SPEAKER");
        } else if (lineInfo.matches(Port.Info.MICROPHONE)) {
            System.out.println("MICROPHONE");
        } else if (lineInfo.matches(Port.Info.COMPACT_DISC)) {
            System.out.println("COMPACT_DISC");
        } else {
            System.out.println("the fuck");
        }
    }
}
