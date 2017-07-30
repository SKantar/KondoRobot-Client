package raf.edu.rs.kondorobot;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.PrintStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by skantar on 25.5.17..
 */

public class MessageHandler extends Handler {
//    private Handler mainThreadHandler;

    private static Socket socket;
    private static PrintStream outputStream;
    private static BufferedReader inputStream;
    private String host;
    private int port;


    public MessageHandler(Looper looper, String host, int port) throws IOException{
        super(looper);
        Log.d("HANDLER", "RADI");
        Log.d("HANDLER", host);
        Log.d("HANDLER", "" + port);
        this.port = port;
        this.host = host;
//        mainThreadHandler = new Handler(Looper.getMainLooper());
//        this.socket = new Socket(host, port);
//        this.outputStream = new DataOutputStream(socket.getOutputStream());
//        this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    private void createSocketIfnotExist() throws IOException{
        if(this.socket == null){
            this.socket = new Socket(this.host, this.port);
            this.outputStream = new PrintStream(this.socket.getOutputStream());
            this.inputStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        }
    }

    private void removeSocket() throws IOException{
        this.socket.close();
        this.socket = null;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        try{
            createSocketIfnotExist();

            String command = "";
            command = msg.getData().getString("message") + "\n";

            System.out.println("FROM SERVER CLIENT: " + command);

            this.outputStream.print(command);

            if(command.trim().equals("CLICK")){

//                int no = this.inputStream.read(buff);
//                int n = Integer.parseInt(buff);
//                System.out.println("FROM SERVER: " + n);
            }
            else
            {
                String response = this.inputStream.readLine();
                System.out.println("FROM SERVER: " + response);
            }




        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
