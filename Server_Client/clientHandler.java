package Server_Client;

import Login_Signup.User;
import Misc.ConnectionClass;
import Misc.PasswordUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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
    private File f;


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
                    case "FriendReq":
                        dataOutput.writeBoolean(FriendReq());
                        break;
                    case "ReqAccept":
                        dataOutput.writeBoolean(ReqAccept());
                        break;
                    case "UpdateProfile":
                        System.out.println("Here");
                        dataOutput.writeBoolean(updateProfile());
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


    //Request Functions

    private boolean updateProfile() {
        try {
            System.out.println("HERE@");
            String username = dataInput.readUTF();
            String first = dataInput.readUTF();
            String last = dataInput.readUTF();
            String email = dataInput.readUTF();
            File f = (File) ObjectInput.readObject();
            System.out.println(LocalDateTime.now().toString());
            String filename = username + ".jpg";
            String path = "src/Server_Client/Server_Files/" + filename;
            File output = new File(path);
            if (output.createNewFile()) {
                System.out.println("File Created");
            }
            System.out.println(output);
            BufferedImage img = ImageIO.read(f);
            ImageIO.write(img, "jpg", output);
            String sql = "Update user set First=\"" + first + "\", Last=\"" + last + "\",Email=\"" + email + "\", Avatar=\"" + path + "\" where username=\"" + username + "\";";
            Statement statement = connection.createStatement();
            boolean b = statement.execute(sql);
            return true;
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean ReqAccept() {
        try {
            String user1 = dataInput.readUTF();
            String user2 = dataInput.readUTF();
            String sql = "INSERT into friend VALUES(NULL,\"" + user1 + "\",\"" + user2 + "\");";
            String sql1 = "INSERT into friend VALUES(NULL,\"" + user2 + "\",\"" + user1 + "\");";
            String sql2 = "DELETE from request where firstuser=\"" + user1 + "\" and seconduser=\"" + user2 + "\";";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSet rs1 = stmt.executeQuery(sql1);
            return true;
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean FriendReq() {
        try {
            String user1 = dataInput.readUTF();
            String user2 = dataInput.readUTF();
            String sql = "INSERT into request VALUES(NULL,\"" + user1 + "\",\"" + user2 + "\");";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            return true;
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private ArrayList<User> GetFriends() {
        ArrayList<User> friends = new ArrayList<>();
        try {
            String user = dataInput.readUTF();
            System.out.println(user);
            String sql = "SELECT * from friend join user on user2=Username where `user1`=\"" + user + "\";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String avatar = rs.getString("Avatar");
                String first = rs.getString("First");
                String last = rs.getString("Last");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String status = rs.getString("Status");
                System.out.println(username);
                User user1 = new User(avatar, first, last, email, username, "", "", status);
                File p = new File(avatar);
                user1.setPic(p);
                friends.add(user1);
                user1 = null;
            }
            System.out.println("Here");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return friends;
    }


    private User GetProfile() {

        try {
            String user = dataInput.readUTF();
            String sql = "SELECT * from user where `Username`=\"" + user + "\";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String avatar = rs.getString("Avatar");
                System.out.println(avatar);
                String first = rs.getString("First");
                String last = rs.getString("Last");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String status = rs.getString("Status");
                User user1 = new User(avatar, first, last, email, username, "", "", status);
                System.out.println(System.getProperty("user.dir"));
                f = new File(avatar);
                System.out.println(f.exists());
                user1.setPic(f);
                System.out.println("Get profile Done");
                return user1;
            }
        } catch (IOException | SQLException e) {
            System.out.println(e);
        }

        return null;
    }


    private ArrayList<User> Search() {
        ArrayList<User> names = new ArrayList<User>();
        try {
            String user = dataInput.readUTF();
            String sql = "SELECT `Avatar`,`First`,`Last`,`Username`,`Email`,`Status` FROM `user` WHERE `Username` LIKE \'" + user + "%\';";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                String avatar = rs.getString("Avatar");
                String first = rs.getString("First");
                String last = rs.getString("Last");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String status = rs.getString("Status");
                User user1 = new User(avatar, first, last, email, username, "", "", status);
                names.add(user1);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        System.out.println("Search done");
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
            System.out.println("Login Done");
            return check.verifyUserPassword(password, Spassword, Salt);
        } catch (IOException | SQLException | InvalidKeySpecException | IllegalArgumentException e) {
            System.out.println(e);
        }
        return false;
    }


    private boolean SignUp() {
        try {
            User register = (User) ObjectInput.readObject();
            String sql = "INSERT INTO user values(\"" + register.getFirst() + "\", \"" + register.getLast() + "\", \"" + register.getEmail() + "\", \"" + register.getUsername() + "\", \"" + register.getPassword() + "\", \"" + register.getSalt() + "\", \"" + register.getStatus() + "\", \"" + "src/Server_Client/Server_Files/def.jpg" + "\");";
            Statement statement = connection.createStatement();
            statement.execute(sql);
            System.out.println("Signup Done");
            return true;
        } catch (IOException | SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
        return false;
    }


}
