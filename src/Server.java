import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;



/**
 * Created by georgipavlov on 19.12.15.
 */
public class Server extends Application{
    ServerSocket server = null;
    Socket socket = null;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    public static String path;
    boolean onTheFile = false;
    Device thisDevice;
    Media media;
    MediaPlayer player;
    MediaView view;
    Pane mpane;
    MediaFinal bar;

    public void runServer(){
        try {
            server = new ServerSocket(6666);
            socket = server.accept();
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server started");
        boolean run  =true;
        JSONObject command  =null;
        String in;

        while (run){
            try {
                in = inputStream.readUTF();
                command = new JSONObject(in);
                commandProceed(command);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    private void commandProceed(JSONObject command) throws Exception {
        String result=null;
        Iterator<?> keys = command.keys();
        String it=null;


        while( keys.hasNext() ) {
            String key = (String)keys.next();
            try {
                it = (String) command.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (it != null && !it.equals("command")  ) {
                break;
            }
        }
        if(it != null && onTheFile){
            path += "/" + it;
        }else if(path.equals(it)){
            onTheFile =true;
        }


        String[] fileFormat = it.split("[.]+");
        if(fileFormat[fileFormat.length-1].equals("mp3") && !onTheFile){
            thisDevice = new Mp3Player(path);
            onTheFile =true;
        }else if(!onTheFile) {
            start(primary);
            thisDevice = new Mp4Player(player,path);
            doIt =true;
            onTheFile =false;
        }
        try {
               result = command.getString("command");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(result != null){
                switch (result){
                    case "forward"://input start
                        thisDevice.forwardStream();
                        break;
                    case "backward"://input start
                        thisDevice.backwardStream();
                        break;
                    case "pause"://input start
                        thisDevice.pauseStream();
                        break;
                    case "continue"://input start
                        thisDevice.continueStream();
                        break;
                    case "open"://input start
                        thisDevice.openStream(0);
                        break;
                    case "stop":
                        onTheFile = false;

                }
            }



    }

    public static void main(String[] args) {
        Server s= new Server();
        s.runServer();
        launch(args);

    }

     boolean doIt = false;
    private  Stage primary;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primary = primaryStage;
        if(doIt){
        BorderPane l= new BorderPane();
        media = new Media(path);
        player = new MediaPlayer(media);
        view = new MediaView(player);
        mpane = new Pane();
        mpane.getChildren().add(view);
        l.setCenter(mpane);
        bar = new MediaFinal(player);
        l.setBottom(bar);
        player.play();
        PlayerFinal f= new PlayerFinal("non");
        Scene scene = new Scene(f,720,480, Color.BLACK);
        primaryStage.setScene(scene);
        primaryStage.show();
        }
    }
}
