package Server_Client;

import Login_Signup.User;
import Misc.ConnectionClass;
import Misc.PasswordUtils;

import java.io.*;
import java.net.Socket;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.lang.System.exit;

public class clientHandler implements Runnable {

    private Socket client;

    private ObjectInputStream ObjectInput;
    private ObjectOutputStream ObjectOutput;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    private String name;
    private ConnectionClass Student = new ConnectionClass();
    private Connection connection = Student.getconnection();
    private PasswordUtils check = new PasswordUtils();


    public clientHandler(Socket client, String name, ObjectOutputStream ObjectOutput, ObjectInputStream ObjectInput, DataOutputStream dataOutput, DataInputStream dataInput) {
        this.name = name;
        this.client = client;
        this.ObjectOutput = ObjectOutput;
        this.ObjectInput = ObjectInput;
        this.dataInput = dataInput;
        this.dataOutput = dataOutput;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String recieved = dataInput.readUTF();
                if (recieved.equalsIgnoreCase("Exit")) {
                    System.out.println("Client Connection Terminating");
                    this.client.close();
                    break;
                }
                switch (recieved) {
                    case "Login":
                        dataOutput.writeBoolean(Login());
                        break;
                    case "Signup":
                        dataOutput.writeBoolean(SignUp());
                        break;
                    case "Search":
                        ObjectOutput.writeObject(Search());
                        break;

                }
            } catch (Exception e) {
                System.out.println(e);
                break;
            }

        }
        try {
            ObjectOutput.close();
            ObjectInput.close();
            dataOutput.close();
            dataInput.close();
            client.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private ArrayList<User> Search() {
        try {
            String user=dataInput.readUTF();
            String sql="SELECT `Avatar`,`First`,`Last`,`Username`,`Email`,`Status` FROM `user` WHERE `Username` LIKE \'"+ user + "%\';";
            ArrayList<User> names=new ArrayList<User>();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                String avatar=rs.getString(0);
                String first=rs.getString(1);
                String last=rs.getString(2);
                String email=rs.getString(3);
                String username=rs.getString(4);
                String status=rs.getString(5);
                User user=new User(avatar,first,last,email,username,"","",status);  //error due to avatar
                names.add(user);
            }
            return names;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }


    private boolean Login() {
        try {
            String username = dataInput.readUTF();
            String password = dataInput.readUTF();
            String sql = "SELECT `Password`, `Salt` FROM `user` WHERE `Username`=\"" + username + "\";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            String Spassword = "";
            String Salt = "A2B2";

            while (rs.next()) {
                Spassword = rs.getString("Password");
                Salt = rs.getString("Salt");
            }
            if (check.verifyUserPassword(password, Spassword, Salt)) {
                return true;
            } else return false;
        } catch (IOException | SQLException | InvalidKeySpecException | IllegalArgumentException e) {
            System.out.println(e);
        }
        return false;
    }


    private boolean SignUp() {
        try {
            User register =(User) ObjectInput.readObject();
            String sql= "INSERT INTO user values(\"" + register.getFirst() + "\", \""+ register.getLast() + "\", \""+ register.getEmail() + "\", \"" + register.getUsername() + "\", \"" + register.getPassword() + "\", \"" + register.getSalt() + "\", \""+ register.getStatus() + "\");";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (IOException | SQLException| ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }


}
