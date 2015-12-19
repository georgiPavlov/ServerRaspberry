import javazoom.jl.player.Player;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.FileInputStream;


/**
 * Created by georgipavlov on 19.12.15.
 */
public class Mp3Player implements Device {

    private Player player;
    private FileInputStream FIS;
    private BufferedInputStream BIS;
    private boolean canResume;
    private String path;
    private int total;
    private int stopped;
    private boolean valid;




    public Mp3Player(String nameFile) {
        this.path= nameFile;
        player = null;
        FIS = null;
        valid = false;
        BIS = null;
        total = 0;
        stopped = 0;
        canResume = false;
    }

    public boolean canResume(){
        return canResume;
    }


    public boolean openStream(int pos) {
        valid = true;
        canResume = false;
        try{
            FIS = new FileInputStream(path);
            total = FIS.available();
            if(pos > -1) FIS.skip(pos);
            BIS = new BufferedInputStream(FIS);
            player = new Player(BIS);
            new Thread(
                    new Runnable(){
                        public void run(){
                            try{
                                player.play();
                            }catch(Exception e){
                                JOptionPane.showMessageDialog(null, "Error playing mp3 file");
                                valid = false;
                            }
                        }
                    }
            ).start();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error playing mp3 file");
            valid = false;
        }
        return valid;
    }

    @Override
    public void pauseStream() {
        try{
            stopped = FIS.available();
            player.close();
            FIS = null;
            BIS = null;
            player = null;
            if(valid) canResume = true;
        }catch(Exception e){

        }
    }


    public void continueStream() {
        if(!canResume) return;
        if(openStream(total-stopped)) canResume = false;
    }

    @Override
    public void forwardStream() {
        pauseStream();
        int next = stopped +10;
        if(next > total){
            return;
        }
        openStream(next);


    }

    @Override
    public void backwardStream() {
        pauseStream();
        int next = stopped -10;
        if(next > total){
            return;
        }
        openStream(next);
    }
}
