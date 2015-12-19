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
public class Server {
    ServerSocket server = null;
    Socket socket = null;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    public static String path;
    boolean onTheFile = false;
    Device thisDevice;

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
            }

        }
    }


    private void commandProceed(JSONObject command){
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
        }


        String[] fileFormat = it.split("[.]+");
        if(fileFormat[fileFormat.length-1].equals("mp3") && !onTheFile){
            thisDevice = new Mp3Player(path);
            onTheFile =true;
        }else if(!onTheFile) {
            //thisDevice = new Mp4Player(path);
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


}
