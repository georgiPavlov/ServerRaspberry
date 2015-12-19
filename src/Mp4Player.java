import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;

/**
 * Created by georgipavlov on 19.12.15.
 */
public class Mp4Player extends Application  {
    public String nameF;

   // public Mp4Player(String nameFile) {
      //  this.nameF= nameFile;
   // }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(final Stage stage) throws Exception {
        stage.setTitle("Movie Player");
        Group root = new Group();
        File file = new File("");

       // String trailer = getHostServices().getDocumentBase()+"SampleVideo_1080x720_10mb.mp4";
        Media media = new Media("/home/georgipavlov/Raspberry/SampleVideo_1080x720_10mb.mp4");//
//        Media media = new Media("file:///home/paul/MoviePlayer/trailers/sintel.mp4");
        final MediaPlayer player = new MediaPlayer(media);
        MediaView view = new MediaView(player);

//        System.out.println("media.width: "+media.getWidth());

        final Timeline slideIn = new Timeline();
        final Timeline slideOut = new Timeline();
        root.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideOut.play();
            }
        });
        root.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                slideIn.play();
            }
        });
        final VBox vbox = new VBox();
        final Slider slider = new Slider();
        vbox.getChildren().add(slider);

        final HBox hbox = new HBox(2);
        final int bands = player.getAudioSpectrumNumBands();
        final Rectangle[] rects = new Rectangle[bands];
        for (int i=0; i<rects.length; i++) {
            rects[i] = new Rectangle();
            rects[i].setFill(Color.GREENYELLOW);
            hbox.getChildren().add(rects[i]);
        }
        vbox.getChildren().add(hbox);

        root.getChildren().add(view);
        root.getChildren().add(vbox);

        Scene scene = new Scene(root, 400, 400, Color.BLACK);
        stage.setScene(scene);
        stage.show();

        player.play();
        player.setOnReady(new Runnable() {
            @Override
            public void run() {
                int w = player.getMedia().getWidth();
                int h = player.getMedia().getHeight();

                hbox.setMinWidth(w);
                int bandWidth = w/rects.length;
                for (Rectangle r:rects) {
                    r.setWidth(bandWidth);
                    r.setHeight(2);
                }

                stage.setMinWidth(w);
                stage.setMinHeight(h);

                vbox.setMinSize(w, 100);
                vbox.setTranslateY(h - 100);

                slider.setMin(0.0);
                slider.setValue(0.0);
                slider.setMax(player.getTotalDuration().toSeconds());

                slideOut.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.translateYProperty(), h-100),
                                new KeyValue(vbox.opacityProperty(), 0.9)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.translateYProperty(), h),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        )
                );
                slideIn.getKeyFrames().addAll(
                        new KeyFrame(new Duration(0),
                                new KeyValue(vbox.translateYProperty(), h),
                                new KeyValue(vbox.opacityProperty(), 0.0)
                        ),
                        new KeyFrame(new Duration(300),
                                new KeyValue(vbox.translateYProperty(), h-100),
                                new KeyValue(vbox.opacityProperty(), 0.9)
                        )
                );
            }
        });

    }




}
