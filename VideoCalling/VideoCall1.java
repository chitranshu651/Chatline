package VideoCalling;

import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VideoCall1 {

    static Socket socket;
    static ObjectOutputStream dout;
    public static void main(String args[]) throws IOException {
        JLabel l = new JLabel();
        JFrame jFrame = new JFrame();
        jFrame.setSize(640,360);
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
        l.setSize(640,480);
        jFrame.add(l);
        l.setVisible(true);
        jFrame.setVisible(true);
        socket = new Socket("localhost",22222);
        System.out.println("Connected");
        ObjectInputStream in  = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        DataInputStream din = new DataInputStream(socket.getInputStream());
        while (true){
            try {
                l.setIcon((ImageIcon) in.readObject());
                System.out.println("IN THIS ICON");
            }
            catch (ClassNotFoundException e){
                e.printStackTrace();
        }

    }



    }

}
