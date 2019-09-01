package Server_Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

   private static Vector<clientHandler> ar= new Vector<>();
    public static void main(String [] args) throws IOException {
        ServerSocket server = new ServerSocket(5005);
        int i = 0;
        while (true) {
            Socket clientConnection = null;
            clientConnection = server.accept();
            System.out.println("Connection Successful");
            ObjectInputStream dios = new ObjectInputStream(clientConnection.getInputStream());
            ObjectOutputStream dos = new ObjectOutputStream(clientConnection.getOutputStream());
            DataInputStream dataInput =new DataInputStream(clientConnection.getInputStream());
            DataOutputStream dataOutput= new DataOutputStream(clientConnection.getOutputStream());
            System.out.println("Assigning Thread");
            clientHandler client = new clientHandler(clientConnection, "Client" + i, dos, dios, dataOutput, dataInput);
            i++;
            Thread t = new Thread(client);
            ar.add(client);
            t.start();

        }


    }
}