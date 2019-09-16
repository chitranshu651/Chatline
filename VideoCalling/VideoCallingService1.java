package VideoCalling;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class VideoCallingService1 extends Thread {
    static Socket socket=new Socket();

    public static boolean exit= false;

    public void run()   {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter port:");
            int port =sc.nextInt();
            sc.close();
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started open for video calling");
            JLabel l = new JLabel();
            JFrame jFrame = new JFrame();
            jFrame.setSize(640,360);
            jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);
            l.setSize(640,480);
            jFrame.add(l);
            serverSocket.setSoTimeout(500);
            while(!socket.isConnected() && !exit) {
                socket = serverSocket.accept();
            }
            if(socket.isConnected()) {
                System.out.println("Connected");
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                l.setVisible(true);
                jFrame.setVisible(true);
                while (true) {
                    if (exit) {
                        break;
                    }
                    Thread.sleep(100);
                    l.setIcon((ImageIcon) in.readObject());
                }
                in.close();
                serverSocket.close();
            }
            else{
                serverSocket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (HeadlessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
