package VideoCalling;

import Misc.SessionInfo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class VideoCallingService1 extends Thread {
    static Socket socket = new Socket();

    public static boolean exit = false;
    private VideoStreamHandler videoStreamHandler = new VideoStreamHandler();
    public volatile boolean threadRun = true;
    private ServerSocket serverSocket;
    private int port = SessionInfo.getVideoPort();

    public void run() {
        try {
            port = SessionInfo.getVideoPort();
            System.out.println(port);
            serverSocket = new ServerSocket(port);
            serverSocket.setSoTimeout(1000);
            System.out.println("Server Started open for video calling");
            while (true) {
                if (!threadRun) {
                    videoStreamHandler.threadRun = false;
                    try {
                        videoStreamHandler.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                try {

                    try {

                        socket = serverSocket.accept();
                        videoStreamHandler.threadRun = false;
                        videoStreamHandler.addClient(socket);
                        videoStreamHandler.join();
                        videoStreamHandler.threadRun = true;
                        videoStreamHandler.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {

                }
            }
            try {
                serverSocket.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Server could not be started");
        }


    }
}
