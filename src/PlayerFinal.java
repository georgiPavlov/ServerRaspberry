import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 * Created by georgipavlov on 19.12.15.
 */
public class PlayerFinal extends BorderPane {
    Media media;
    MediaPlayer player;
    MediaView view;
    Pane mpane;
    MediaFinal bar;
    public  PlayerFinal(String file){
        media = new Media(file);
        player = new MediaPlayer(media);
        view = new MediaView(player);
        mpane = new Pane();
        mpane.getChildren().add(view);
        setCenter(mpane);
        bar = new MediaFinal(player);
        setBottom(bar);
        player.play();

    }

}
