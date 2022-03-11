package me.JelmarNL.minixRtspReceiver.tests;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class DeviceDiscoveryTest {
    public static void main(String[] args) throws IOException {
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        NetworkInterface networkInterface = networkInterfaces.nextElement();
//        networkInterface.
        checkHosts("192.168.0");
    }

    public static void checkHosts(String subnet) throws IOException {
        int timeout = 1000;
        for (int i = 1; i < 255; i++) {
            String host = subnet + "." + i;
            if (InetAddress.getByName(host).isReachable(timeout)) {
                System.out.println(host + " is reachable");
            }
        }
    }
}
