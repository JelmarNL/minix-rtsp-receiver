package me.JelmarNL.minixRtspReceiver.tests;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.javafx.videosurface.ImageViewVideoSurfaceFactory;
import uk.co.caprica.vlcj.media.InfoApi;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

import java.util.List;

public class RtspPlayerTest extends Application {
    private final MediaPlayerFactory mediaPlayerFactory;

    private final EmbeddedMediaPlayer embeddedMediaPlayer;

    private ImageView videoImageView;
    
    public static void main(String[] args) {
        String base = "C:/Program Files/VideoLAN/VLC/";
        System.load(base + "libvlccore.dll");
        System.load(base + "libvlc.dll");
        System.load(base + "npvlc.dll");
        System.load(base + "axvlc.dll");
        launch(args);
    }
    
    public RtspPlayerTest() {
        this.mediaPlayerFactory = new MediaPlayerFactory();
        this.embeddedMediaPlayer = mediaPlayerFactory.mediaPlayers().newEmbeddedMediaPlayer();
        this.embeddedMediaPlayer.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void playing(MediaPlayer mediaPlayer) {
            }

            @Override
            public void paused(MediaPlayer mediaPlayer) {
            }

            @Override
            public void stopped(MediaPlayer mediaPlayer) {
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
            }
        });
    }

    @Override
    public void init() {
        this.videoImageView = new ImageView();
        this.videoImageView.setPreserveRatio(true);
        embeddedMediaPlayer.videoSurface().set(ImageViewVideoSurfaceFactory.videoSurfaceForImageView(this.videoImageView));
    }

    public void start(Stage stage) throws Exception {
        List<String> params = getParameters().getRaw();
        if (params.size() != 1) {
            System.out.println("Specify a single MRL");
            System.exit(-1);
        }
        
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: black;");
        stage.setMaximized(true);
        stage.setFullScreen(true);
        
        System.out.println();
        for (Screen screen : Screen.getScreens()) {
            Rectangle2D visualBounds = screen.getVisualBounds();
            Rectangle2D bounds = screen.getBounds();
            System.out.println("Visual bounds: " + visualBounds.getWidth() + "x" + visualBounds.getHeight());
            System.out.println("Bounds: " + bounds.getWidth() + "x" + bounds.getHeight());
            System.out.println();
        }
        

        videoImageView.fitWidthProperty().bind(root.widthProperty());
        videoImageView.fitHeightProperty().bind(root.heightProperty());

        root.setCenter(videoImageView);

        Scene scene = new Scene(root, 100, 100, Color.BLACK);
        stage.setTitle("Rtsp camera & usb audio player");
        stage.setScene(scene);
        stage.show();

        embeddedMediaPlayer.media().play(params.get(0));

        InfoApi info = embeddedMediaPlayer.media().info();
        System.out.println("State: " + info.state());
        //info.statistics();
        
        //embeddedMediaPlayer.video().videoDimension().setSize();

        //embeddedMediaPlayer.controls().setPosition(0.4f);
    }

    @Override
    public final void stop() {
        embeddedMediaPlayer.controls().stop();
        embeddedMediaPlayer.release();
        mediaPlayerFactory.release();
    }
}
