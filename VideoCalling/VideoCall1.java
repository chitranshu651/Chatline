package VideoCalling;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VideoCall1 extends Thread {

    static Socket socket;
    static ObjectOutputStream dout;
    private String ip;
    private int port;
    public volatile boolean runThread = true;

    public VideoCall1(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public void run() {
        JLabel l = new JLabel();
        JFrame jFrame = new JFrame();
        jFrame.setSize(640, 360);
        jFrame.setDefaultCloseOperation(jFrame.DISPOSE_ON_CLOSE);
        l.setSize(640, 480);
        jFrame.add(l);
        l.setVisible(true);
        jFrame.setVisible(true);
        System.out.println(ip + ":" + port);
        try {
            System.out.println(ip + ":" + port);
            socket = new Socket(ip, port);
            System.out.println("Connected");
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            DataInputStream din = new DataInputStream(socket.getInputStream());
            while (runThread) {
                try {
                    l.setIcon((ImageIcon) in.readObject());
                    System.out.println("IN THIS ICON");
                    Thread.currentThread().sleep(75);
                } catch (ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();

                    break;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
            System.out.println("HERE NIGGA");
        }


    }

}
