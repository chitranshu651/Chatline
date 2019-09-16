package VideoCalling;

import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class VideoCall1 {

    static Socket socket;
    static ObjectOutputStream dout;
    public static void main(String args[]) throws IOException {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640,480));
        webcam.open();
        ImageIcon is;
        BufferedImage bf;
        try {
            socket = new Socket("localhost",8080);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            dout = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int count=0;
        while (true){
            try {
                count++;
                Thread.sleep(100);
                dout.writeObject(new ImageIcon(webcam.getImage()));
                dout.flush();
                dout.reset();


            }catch (Exception e){

            }
        }




    }

}
