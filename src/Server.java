import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Scanner;


/**
 * Created by georgipavlov on 19.12.15.
 */
public class Server {
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
        Scanner scanner = null ;
        try {
            server = new ServerSocket(6699 );
            System.out.println("Waiting for connection");
            socket = server.accept();
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            //inputStream = new ObjectInputStream(socket.getInputStream());
            scanner = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server connected");
        boolean run = true;
        JSONObject command = null;
        String in;

        while (run) {
            try {
                //in = inputStream.readUTF();
                String resu = scanner.nextLine();
                System.out.println(resu);
                command = new JSONObject(resu);
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

    String fullnamel=null;
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
            if (it != null && !it.equals("gesture")) {
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
           // thisDevice = new Mp3Player(path);
            //onTheFile = true;
        } else if (!onTheFile) {
            //doIt = true;
           // start(primary);
           // thisDevice = new Mp4Player(player, path);
            //doIt = false;
            //onTheFile = false;
        }
        try {
            result = command.getString("gesture");
            fullnamel = command.getString("full_name");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (result != null) {
            switch (result) {
                case "left"://input start
                    r.keyPress(KeyEvent.VK_LEFT);
                    r.keyRelease(KeyEvent.VK_LEFT);
                   // thisDevice.forwardStream();
                    System.out.println("left" + it);
                    break;
                case "right"://input start
                    r.keyPress(KeyEvent.VK_RIGHT);
                    r.keyRelease(KeyEvent.VK_RIGHT);
                   // thisDevice.backwardStream();
                    System.out.println("right" + it);
                    break;
                case "up"://input start
                    r.keyPress(KeyEvent.VK_UP);
                    r.keyRelease(KeyEvent.VK_UP);
                   // thisDevice.pauseStream();
                    System.out.println("up" + it );
                    break;
                case "down"://input start
                    r.keyPress(KeyEvent.VK_DOWN);
                    r.keyRelease(KeyEvent.VK_DOWN);
                    //thisDevice.continueStream();
                    System.out.println("down" + it);
                    break;
                case "open"://input start
                    String[] res = it.split("[open]+");
                    if(fullnamel == null){
                        path = it;
                        r.keyPress(KeyEvent.VK_ENTER);
                        r.keyRelease(KeyEvent.VK_ENTER);
                        System.out.println("Му computer"+  it);
                    }else{
                        file = new File(res[1]);
                        System.out.println("Му computer 2" + it);
                    }
                    break;
                case "stop":
                    onTheFile = false;

            }
        }


    }

    public static void main(String[] args) {

        Server s = new Server();
        s.openPLayer();
        s.runServer();
        //launch(args);

    }

    boolean doIt = false;
    private Stage primary;



    public void openPLayer(){
        ProcessBuilder pb = new ProcessBuilder("bash", "-c", "omxplayer -r -o hdmi /home/georgipavlov/Raspberry/abs.mp4");
        Process p = null;
        try {
            p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer sb = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                p.getInputStream()));

        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append(line);
        while (line != null) {
            try {
                line = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
    }

}
