package me.JelmarNL.minixRtspReceiver.Video;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import me.JelmarNL.minixRtspReceiver.Main;
import me.JelmarNL.minixRtspReceiver.util.FileConfiguration;
import me.JelmarNL.minixRtspReceiver.util.Logger;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import static javafx.application.Application.launch;

public class RtspPlayer extends Thread {
    private String cameraIp;
    private String setupCommands; //As base64 //TODO: Implement
    private String streamUrl;
    private String state;
    private static Stage stage;
    //Check if it's the first time we start javafx cuz it complains otherwise
    private static boolean firstLaunch = true;
    
    @Override
    public void run() {
        state = "Starting";
        Logger.info("RtspPlayer", "Loading player...");
        FileConfiguration RtspConfig = new FileConfiguration("rtsp");
        cameraIp = RtspConfig.getProperty("cameraIp", "192.168.0.10:554");
        setupCommands = RtspConfig.getProperty("setupCommands", "");
        streamUrl = RtspConfig.getProperty("streamUrl", "rtsp://%ip%/mediainput/h264/stream_1");
        //Test stream:
        //streamUrl = "rtsp://demo:demo@ipvmdemo.dyndns.org:5541/onvif-media/media.amp?profile=profile_1_h264&sessiontimeout=60&streamtype=unicast";
        //streamUrl = "rtsp://localhost/";
        
        String base = RtspConfig.getProperty("libUrl", "C:/Program Files/VideoLAN/VLC/");
        try {
            System.load(base + "libvlccore.dll");
            System.load(base + "libvlc.dll");
            System.load(base + "npvlc.dll");
            System.load(base + "axvlc.dll");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            Logger.error("RtspPlayer", "Could not load vlc libraries, please make sure vlc media player (64bit) is installed at: " + base);
            state = "Crashed";
            return;
        }
        Logger.info("RtspPlayer", "Loaded player");
        if (isFirstLaunch()) {
            Platform.setImplicitExit(false);
            launch(Player.class);
        } else {
            Platform.runLater(()-> {
                try {
                    Player player = new Player();
//                    Stage newPrimaryStage = new Stage();
                    player.start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                    Logger.error("RtspPlayer", "Failed to restart gui.");
                    state = "Crashed";
                }
            });
        }
    }

    public String getStatus() {
        //If application is running; return the media player's state
        if (state.equalsIgnoreCase("Running")) {
            return Player.embeddedMediaPlayer.media().info().state().toString();
        } else {
            return state;
        }
    }
    
    private String createStreamUrl() {
        return streamUrl.replace("%ip%", cameraIp);
    }
    
    public void end() {
        Player.instance.stop();
        state = "stopped";
    }

    private static boolean isFirstLaunch() {
        return firstLaunch;
    }
    private static void setJavaFxLaunched() {
        firstLaunch = false;
    }
    
    public static class Player extends Application {
        private static EmbeddedMediaPlayer embeddedMediaPlayer;
        private static MediaPlayerFactory mediaPlayerFactory;
        private static Player instance;
        
        @Override
        public void start(Stage stage) throws Exception {
            instance = this;
            Logger.info("RtspPlayer", "Opening stream...");
            mediaPlayerFactory = new MediaPlayerFactory();
            embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
            
            ImageView videoImageView = new ImageView();
            videoImageView.setPreserveRatio(true);
            embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(videoImageView));
            
            //Create window
            BorderPane root = new BorderPane();
            root.setStyle("-fx-background-color: black;");
            
            videoImageView.fitWidthProperty().bind(root.widthProperty());
            videoImageView.fitHeightProperty().bind(root.heightProperty());
            
            root.setCenter(videoImageView);
            
            Scene scene = new Scene(root, 1920, 1080, Color.BLACK);
            stage.setTitle("Rtsp camera & usb audio player");
            stage.setScene(scene);
            stage.show();

            //Non of this seems to hide the taskbar (at least when not playing)
            //            stage.setFullScreen(false);
            //stage.setMaximized(true);
            //Fullscreen does not work after a restart:
//            if (isFirstLaunch()) {
//                stage.initStyle(StageStyle.UNDECORATED);
//            }
//            Screen screen = Screen.getPrimary();
//            stage.setX(screen.getVisualBounds().getMinX());
//            stage.setY(screen.getVisualBounds().getMinY());
//            stage.setWidth(screen.getVisualBounds().getWidth());
//            stage.setHeight(screen.getVisualBounds().getHeight());
            stage.setFullScreenExitHint("");
//            stage.requestFocus();
//            stage.toFront();
//            stage.setResizable(false);
            stage.setFullScreen(true);

            RtspPlayer.stage = stage;
            
            embeddedMediaPlayer.media().play(Main.rtspPlayer.createStreamUrl());
            Main.rtspPlayer.state = "Running";
            Logger.info("RtspPlayer", "Stream open");

            setJavaFxLaunched();
        }

        @Override
        public final void stop() {
            embeddedMediaPlayer.controls().stop();
            embeddedMediaPlayer.release();
            mediaPlayerFactory.release();
        }
    }
}
