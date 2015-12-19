import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Created by georgipavlov on 19.12.15.
 */
public class Mp4Player extends MediaFinal implements Device{
    public Media media;
    public MediaPlayer player;
    static BorderPane b;
    public Mp4Player(MediaPlayer play,String path) {
        super(play);
        this.player = play;
    }



    public static BorderPane ret(){
        return b;
    }

    @Override
    public boolean openStream(int pos) {
        MediaPlayer.Status status  = player.getStatus();
        if(status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.HALTED
                || status == MediaPlayer.Status.STOPPED){
        player.play();
        }
        return false;
    }

    @Override
    public void pauseStream() {
        MediaPlayer.Status status  = player.getStatus();
        if(status == MediaPlayer.Status.PLAYING){
        player.pause();
        }
    }

    @Override
    public void continueStream() {
        if(!player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) {
        player.play();
        }
    }

    @Override
    public void forwardStream() {
        if(!player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) {
            player.seek(player.getCurrentTime().multiply(1.002));
        }

    }

    @Override
    public void backwardStream() {
        if(!player.getCurrentTime().greaterThanOrEqualTo(player.getTotalDuration())) {
            player.seek(player.getCurrentTime().multiply(0.998));
        }
    }
}
