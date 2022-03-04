module me.JelmarNL.minixRtspReceiver {
    requires javafx.controls;
    requires uk.co.caprica.vlcj;
    requires uk.co.caprica.vlcj.javafx;
    requires java.desktop;
    requires jdk.httpserver;
    requires org.jetbrains.annotations;

    exports me.JelmarNL.minixRtspReceiver;
    exports me.JelmarNL.minixRtspReceiver.tests;
    exports me.JelmarNL.minixRtspReceiver.util;
}
