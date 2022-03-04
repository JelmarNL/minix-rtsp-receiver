package me.JelmarNL.minixRtspReceiver.tests;

import java.awt.*;

public class DisplayPropertiesTest {
    public static void main(String[] args) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        //GraphicsDevice device = env.getDefaultScreenDevice();
        //env.getScreenDevices(), might want to enumerate too although the minix should only have one screen
        for (GraphicsDevice device : env.getScreenDevices()) {
            System.out.println(device.getIDstring() + " " + device.getType());
            DisplayMode[] displayModes = device.getDisplayModes();
            for (DisplayMode displayMode : displayModes) {
                System.out.println(displayMode.toString());
            }
            System.out.println();
        }
    }
}
