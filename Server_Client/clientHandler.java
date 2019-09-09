package Server_Client;

import Login_Signup.User;
import Misc.ConnectionClass;
import Misc.PasswordUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
                    case "GetProfile":
                        ObjectOutput.writeObject(GetProfile());
                        break;
                    case "GetFriends":
                        ObjectOutput.writeObject(GetFriends());
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

    private ArrayList<User> GetFriends() {
        ArrayList<User> friends= new ArrayList<>();
        try {
            String user = dataInput.readUTF();
            System.out.println(user);
            String sql = "SELECT * from friend join user on user2=Username where `user1`=\"" + user + "\";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                String avatar = rs.getString("Avatar");
                String first = rs.getString("First");
                String last = rs.getString("Last");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String status = rs.getString("Status");
                System.out.println(username);
                User user1 = new User(avatar, first, last, email, username, "", "", status);
                friends.add(user1);
                user1=null;
            }
            System.out.println("Here");
        }
        catch(IOException  | SQLException e){
            System.out.println(e);
        }
        return friends;
    }


    private User GetProfile() {

        try {
            String user = dataInput.readUTF();
            String sql = "SELECT * from user where `Username`=\"" + user + "\";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next()) {
                String avatar = rs.getString("Avatar");
                String first = rs.getString("First");
                String last = rs.getString("Last");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String status = rs.getString("Status");
                User user1 = new User(avatar, first, last, email, username, "", "", status);

                return user1;
            }
            }
        catch(IOException  | SQLException e){
            System.out.println(e);
        }

        return null;
    }


    private ArrayList<User> Search() {
        ArrayList<User> names=new ArrayList<User>();
        try {
            String user=dataInput.readUTF();
            String sql="SELECT `Avatar`,`First`,`Last`,`Username`,`Email`,`Status` FROM `user` WHERE `Username` LIKE \'"+ user + "%\';";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while(rs.next())
            {
                String avatar=rs.getString("Avatar");
                String first=rs.getString("First");
                String last=rs.getString("Last");
                String email=rs.getString("Email");
                String username=rs.getString("Username");
                String status=rs.getString("Status");
                User user1=new User(avatar,first,last,email,username,"","",status);
                names.add(user1);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return names;

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
            return check.verifyUserPassword(password, Spassword, Salt);
        } catch (IOException | SQLException | InvalidKeySpecException | IllegalArgumentException e) {
            System.out.println(e);
        }
        return false;
    }


    private boolean SignUp() {
        try {
            User register = (User) ObjectInput.readObject();
            String sql = "INSERT INTO user values(\"" + register.getFirst() + "\", \"" + register.getLast() + "\", \"" + register.getEmail() + "\", \"" + register.getUsername() + "\", \"" + register.getPassword() + "\", \"" + register.getSalt() + "\", \"" + register.getStatus() + "\", \" " + " " + "\");";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            return true;
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }


}
