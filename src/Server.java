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

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;


/**
 * Created by georgipavlov on 19.12.15.
 */
public class Server extends Application {
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

    public void runServer() {
        try {
            server = new ServerSocket(6656);
            System.out.println("Waiting for connection");
            socket = server.accept();
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server connected");
        boolean run = true;
        JSONObject command = null;
        String in;

        while (run) {
            try {
                in = inputStream.readUTF();
                System.out.println(in);
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
        String result = null;
        Iterator<?> keys = command.keys();
        String it = null;
        File file = new File("/home/georgipavlov");
        Robot r = new Robot();



        while (keys.hasNext()) {
            String key = (String) keys.next();
            try {
                it = (String) command.get(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (it != null && !it.equals("v")) {
                break;
            }
        }
        if (it != null && !onTheFile) {
            path += "/" + it;
        } else if (path.equals(it)) {
            onTheFile = true;
        }


        String[] fileFormat = it.split("[.]+");
        if (fileFormat[fileFormat.length - 1].equals("mp3") && !onTheFile) {
            thisDevice = new Mp3Player(path);
            onTheFile = true;
        } else if (!onTheFile) {
            doIt = true;
            start(primary);
            thisDevice = new Mp4Player(player, path);
            doIt = false;
            onTheFile = false;
        }
        try {
            result = command.getString("gesture");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null) {
            switch (result) {
                case "left"://input start
                    r.keyPress(KeyEvent.VK_LEFT);
                    r.keyRelease(KeyEvent.VK_LEFT);
                   // thisDevice.forwardStream();
                    break;
                case "right"://input start
                    r.keyPress(KeyEvent.VK_RIGHT);
                    r.keyRelease(KeyEvent.VK_RIGHT);
                   // thisDevice.backwardStream();
                    break;
                case "up"://input start
                    r.keyPress(KeyEvent.VK_UP);
                    r.keyRelease(KeyEvent.VK_UP);
                   // thisDevice.pauseStream();
                    break;
                case "down"://input start
                    r.keyPress(KeyEvent.VK_DOWN);
                    r.keyRelease(KeyEvent.VK_DOWN);
                    //thisDevice.continueStream();
                    break;
                case "open"://input start
                    String[] res = path.split("[open]+");
                    if(res[1] == ""){
                        path = it;
                        r.keyPress(KeyEvent.VK_ENTER);
                        r.keyRelease(KeyEvent.VK_ENTER);
                    }else if(!res[1].equals("My Computer")){
                        file = new File(res[1]);
                    }
                    break;
                case "stop":
                    onTheFile = false;

            }
        }


    }

    public static void main(String[] args) {
        Server s = new Server();
        s.runServer();
        launch(args);

    }

    boolean doIt = false;
    private Stage primary;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primary = primaryStage;
        if (doIt) {
            BorderPane l = new BorderPane();
            media = new Media(path);
            player = new MediaPlayer(media);
            view = new MediaView(player);
            mpane = new Pane();
            mpane.getChildren().add(view);
            l.setCenter(mpane);
            bar = new MediaFinal(player);
            l.setBottom(bar);
            player.play();
            Scene scene = new Scene(l, 720, 480, Color.BLACK);
            primaryStage.setScene(scene);
            primaryStage.show();
        }
    }
}
