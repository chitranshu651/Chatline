package Server_Client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket = null;
    private ObjectOutputStream ObjectOutput = null;
    private ObjectInputStream ObjectInput = null;
    private DataOutputStream dataOutput = null;
    private DataInputStream dataInput = null;

    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connected");

            ObjectOutput = new ObjectOutputStream(socket.getOutputStream());
            ObjectInput = new ObjectInputStream(socket.getInputStream());
            dataInput = new DataInputStream(socket.getInputStream());
            dataOutput = new DataOutputStream(socket.getOutputStream());
        } catch (IOException d) {

            System.out.println(d);
        }
    }

    public void sendString(String toSend) {
        try {
            dataOutput.writeUTF(toSend);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public String recieveString() {
        try {
            return dataInput.readUTF();
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    public Boolean recieveBoolean() {
        try {
            return dataInput.readBoolean();
        } catch (IOException e) {
            System.out.println(e);
        }
        return false;
    }

    public void sendObject(Object ob) throws IOException {
        ObjectOutput.writeObject(ob);
    }

    public void sendInt(int a) {
        try {
            dataOutput.writeInt(a);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public Object recieveObject() {
        try {
            return ObjectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e);
            return null;
        }
    }

    public void writeBoolean(boolean bool) {
        try {
            ObjectOutput.writeBoolean(bool);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
