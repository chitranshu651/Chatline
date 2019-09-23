package VideoCalling;

import com.github.sarxos.webcam.Webcam;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class VideoStreamHandler extends Thread {

    private ObjectOutputStream objectOutputStream;
    public volatile boolean threadRun = true;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private DataInputStream dataInputStream;
    private ArrayList<ObjectOutputStream> objectOutputStreams = new ArrayList<>();
    private ArrayList<ObjectInputStream> objectInputStreams = new ArrayList<>();
    private ArrayList<DataInputStream> dataInputStreams = new ArrayList<>();
    private BufferedImage bf;
    private ImageIcon image;


    public void addClient(Socket socket) {
        try {
            System.out.println("add client called");
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            objectInputStreams.add(objectInputStream);
            System.out.println("Here BOI");
            objectOutputStreams.add(objectOutputStream);
            dataInputStreams.add(dataInputStream);
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("In this Exceptiion");
        }

    }

    public void run() {
        System.out.println("In run");
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(new Dimension(640, 480));
        Thread t = Thread.currentThread();
        webcam.open();
        boolean exit = false;
        while (threadRun) {
            bf = webcam.getImage();
            image = new ImageIcon(bf);
            for (ObjectOutputStream os : objectOutputStreams) {
                try {
                    os.writeObject(image);
                    // System.out.println("Write DONE");
                    t.sleep(75);
                } catch (Exception e) {
                    System.out.println(e);
                    objectOutputStreams.remove(os);
                    System.out.println(objectOutputStreams.size());
                    break;
                }
            }
            if (objectOutputStreams.size() == 0) {
                break;
            }

        }
        webcam.close();

    }
}
