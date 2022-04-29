# minix-rtsp-receiver
Receives a RTSP stream and combines it with usb audio signal to output on hdmi.

VLCJ with Javafx for receiving a RTSP stream and the java sound api for repeating audio.

Webcontrol on port 80

## Runnable on 32bit
### Windows requirements:
- bellsoft-jdk11.0.14.1+1-windows-i586-full (msi installer) (Liberica Full JDK 11.0.14.1+1 x86 32 bit for Windows) https://bell-sw.com/pages/downloads/#/java-11-lts   
- vlc-3.0.16-win32 (exe installer)  
- A shortcut in shell:startup to the bat file which runs the jar.
- And you might want to hide the taskbar and prevent sleep and updates.
