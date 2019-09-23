package Server_Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

    private static Vector<clientHandler> ar = new Vector<>();

    public static void main(String[] args) throws IOException {
        //Creates a Server on port 5005
        ServerSocket server = new ServerSocket(5005);
        int i = 0;
        while (true) {
            //Checks for Client connections
            Socket clientConnection = null;
            clientConnection = server.accept();
            //If connected Gets client's input output streams
            System.out.println("Connection Successful");
            ObjectInputStream dios = new ObjectInputStream(clientConnection.getInputStream());
            ObjectOutputStream dos = new ObjectOutputStream(clientConnection.getOutputStream());
            DataInputStream dataInput = new DataInputStream(clientConnection.getInputStream());
            DataOutputStream dataOutput = new DataOutputStream(clientConnection.getOutputStream());
            //Assigns a client handler on a new Thread
            System.out.println("Assigning Thread");
            clientHandler client = new clientHandler(clientConnection, "Client" + i, dos, dios, dataOutput, dataInput);
            i++;
            Thread t = new Thread(client);
            //Adds the client to a list of clients
            ar.add(client);
            t.start();

        }


    }
}