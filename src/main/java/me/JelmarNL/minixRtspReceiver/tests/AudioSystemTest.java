package me.JelmarNL.minixRtspReceiver.tests;

import me.JelmarNL.minixRtspReceiver.Audio.AudioDevices;
import me.JelmarNL.minixRtspReceiver.Audio.AudioRepeater;

public class AudioSystemTest {
    public static void main(String[] args) {
        AudioDevices.getInputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().getName()));
        System.out.println();
        AudioDevices.getOutputDevices().forEach((mixer, line) -> System.out.println(mixer.getMixerInfo().getName()));
        
        //Test with devices in config
        AudioRepeater audioRepeater = new AudioRepeater();
        //Not in a new thread, just for test
        audioRepeater.run();
    }
}
