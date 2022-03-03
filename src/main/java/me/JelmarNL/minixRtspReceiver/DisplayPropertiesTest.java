package me.JelmarNL.minixRtspReceiver;

import java.awt.*;

public class DisplayPropertiesTest {
    public static void main(String[] args) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getDefaultScreenDevice();
        DisplayMode[] displayModes = device.getDisplayModes();
        for (DisplayMode displayMode : displayModes) {
            System.out.println(displayMode.toString());
        }
    }
}
