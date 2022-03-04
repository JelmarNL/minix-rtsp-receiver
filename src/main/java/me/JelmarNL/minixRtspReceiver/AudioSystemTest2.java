package me.JelmarNL.minixRtspReceiver;

import javax.sound.sampled.*;

public class AudioSystemTest2 {
    public static void main(String[] args) {
        Mixer.Info[] infoMixers = AudioSystem.getMixerInfo();

        for (Mixer.Info infoMixer : infoMixers) {
            // Get mixer for each info
            Mixer mixer = AudioSystem.getMixer(infoMixer);
            String mixerName = infoMixer.getName();
            System.out.println("MIXER NAME: " + mixerName);
            System.out.println("MIXER : " + infoMixer);
            System.out.println("MIXER DESC: " + infoMixer.getDescription());

            // Check if is input device
            Line.Info[] lines = mixer.getTargetLineInfo();
            if (lines.length > 0) {
                for (Line.Info line : lines) {
                    if (line.getLineClass().equals(TargetDataLine.class)) {
                        System.out.println("TARGET LINE INFO: " + line.toString());
                    }
                }
            }
            // Check if is output device
            lines = mixer.getSourceLineInfo();
            if (lines.length > 0) {
                for (Line.Info line : lines) {
                    if (line.getLineClass().equals(SourceDataLine.class)) {
                        System.out.println("SOURCE LINE INFO: " + line.toString());
                    }
                }
            }
            System.out.println();
        }
    }
}
