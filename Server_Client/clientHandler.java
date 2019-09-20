package Server_Client;

import Login_Signup.User;
import Misc.ConnectionClass;
import Misc.IPClass;
import Misc.MyMessage;
import Misc.PasswordUtils;
import sample.Main;

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
                    case "GetRequest":
                        System.out.println("Get request called");
                        ObjectOutput.writeObject(GetRequest());
                        break;
                    case "GetMessages":
                        System.out.println("Get Message Called");
                        ObjectOutput.writeObject(GetMessages());
                        break;
                    case "SendMessage": System.out.println("send Message called");
                        dataOutput.writeBoolean(SendMessage());
                        break;
                    case "FriendSuggestion":
                        System.out.println("friend suggestion");
                        ObjectOutput.writeObject(GetSuggestions());
                        break;
                    case "CheckOnline":
                        System.out.println("online check");
                        dataOutput.writeBoolean(CheckOnline());
                        break;
                    case "Disconnect":
                        System.out.println("disconnect");
                        dataOutput.writeBoolean(Disconnect());
                        break;
                    case "CheckFriend":
                        System.out.println("Check Friend Called");
                        dataOutput.writeBoolean(CheckFriend());
                        break;
                    case "StartVideo":
                        System.out.println("Start video call");
                         ObjectOutput.writeObject(StartVideo());
                         break;
                    case "CountRequests":
                        System.out.println("Count Requests called");
                        dataOutput.writeUTF(CountReq());
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

    private String CountReq() {
        int m_count=0,v_count=0;
        try{
            String user=dataInput.readUTF();
            String sql1="Select COUNT(UID) as count from notifications where receiver=\""+ user +"\" AND type=\"1\";";
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(sql1);
            if(rs.next())
            m_count=rs.getInt("count");
            String sql2="select COUNT(UID) as count from notifications where receiver=\""+ user +"\" AND type=\"0\";";
            stmt=connection.createStatement();
            ResultSet rs1=stmt.executeQuery(sql1);
            if(rs1.next())
            v_count=rs1.getInt("count");
            String ret="You have "+m_count+" new messages and "+v_count+" missed video calls.";

            return ret;
        } catch(Exception e){
            e.printStackTrace();
        }
        return "";
    }

    private IPClass StartVideo() {
        try{
            String user = dataInput.readUTF();
            String sql = "Select * from online_status where Username =\"" + user + "\";";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            IPClass ipClass=null;
            if (rs.next()){
                ipClass =new IPClass();
                ipClass.setIp(rs.getString("IP"));
                ipClass.setPort(rs.getString("Port"));
                ipClass.setUsername(rs.getString("Username"));
            }
            return ipClass;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean CheckFriend() {
        try{
            boolean check =false;
            String user1=dataInput.readUTF();
            String user2=dataInput.readUTF();
            String sql = "Select * from friend where user1=\""+ user1 +"\" and user2=\""+ user2 + "\";";
            Statement statement =connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                check=true;
            }
            return check;
        }
        catch (SQLException | IOException e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean Disconnect() {
        try{
            String user=dataInput.readUTF();
            String sql="DELETE from Online_Status where username=\"" + user + "\";";
            Statement stmt=connection.createStatement();
            stmt.execute(sql);
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private boolean CheckOnline() {
        try {
            boolean flag=false;
            String user=dataInput.readUTF();
            String sql="Select Username from Online_Status where Username=\""+user+"\";";
            Statement stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(sql);
            while(rs.next())
            {
                flag=true;
            }
            if(flag==true)
                return true;
            else
                return false;
        }catch(Exception e){
            e.getStackTrace();
        }
        return false;
    }

    private ArrayList<User> GetSuggestions() {
        ArrayList<User> suggestions=new ArrayList<>();
        User user1;
        try {
            String user=dataInput.readUTF();
            String sql="SELECT * from user where Username in (SELECT DISTINCT user2 from friend where user1 in (SELECT user2 from friend where user1=\""+user+"\") AND user2 != \""+user+"\");";
            Statement stmt=connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next())
            {
                String avatar = rs.getString("Avatar");
                String first = rs.getString("First");
                String last = rs.getString("Last");
                String email = rs.getString("Email");
                String username = rs.getString("Username");
                String status = rs.getString("Status");
                System.out.println(username);
                user1 = new User(avatar, first, last, email, username, "", "", status);
                File p = new File(avatar);
                user1.setImage(BufftoByte(f));
                suggestions.add(user1);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return suggestions;
    }


    //Request Functions

    private ArrayList<User> GetRequest() {
        ArrayList<User> friends = new ArrayList<>();
        User user1;
        try {
            String user = dataInput.readUTF();
            String sql = "Select user.* from user join request on user.Username=request.firstuser where request.seconduser=\"" + user +"\";";
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
                user1 = new User(avatar, first, last, email, username, "", "", status);
                File p = new File(avatar);
                user1.setPic(p);
                friends.add(user1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return friends;

    }


    private boolean updateProfile() {
        try {
            System.out.println("HERE@");
            String username = dataInput.readUTF();
            String first = dataInput.readUTF();
            String last = dataInput.readUTF();
            String email = dataInput.readUTF();
            String status = dataInput.readUTF();
            Boolean change = ObjectInput.readBoolean();
            byte [] f = (byte []) ObjectInput.readObject();
            System.out.println(f);
            System.out.println(LocalDateTime.now().toString());
            String filename = username + ".jpg";
            String path = "src/Server_Client/Server_Files/" + filename;
            File output;
            if(change) {
                 output = new File(path);
                if (output.createNewFile()) {
                    System.out.println("File Created");
                    BufferedImage img = BytetoBuff(f);
                    ImageIO.write(img, "jpg", output);
                } else {
                    output.delete();
                    output.createNewFile();
                    BufferedImage img = BytetoBuff(f);
                    ImageIO.write(img, "jpg", output);
                }
            }
            else{
                output = new File(path);
            }
            System.out.println(output);
            String sql = "Update user set First=\"" + first + "\", Last=\"" + last + "\",Email=\"" + email + "\", Avatar=\"" + path + "\", Status=\""+ status + "\" where username=\"" + username + "\";";
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
            String sql2 = "DELETE from request where firstuser=\"" + user2 + "\" and seconduser=\"" + user1 + "\";";
            Statement stmt = connection.createStatement();
            stmt.execute(sql);
            stmt.execute(sql1);
            stmt.execute(sql2);
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
            boolean check= statement.execute(sql);
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
                user1.setImage(BufftoByte(p));
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
                user1.setImage(BufftoByte(f));
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
            String current= dataInput.readUTF();
            String sql = "SELECT `Avatar`,`First`,`Last`,`Username`,`Email`,`Status` FROM `user` WHERE `Username` LIKE \'" + user + "%\' and Username!= \""+ current+"\";";

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
                user1.setImage(BufftoByte(new File(avatar)));
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
            String port = dataInput.readUTF();
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
            if(check.verifyUserPassword(password, Spassword, Salt))
            {
                String sql1="Insert into Online_Status VALUES(\""+username+"\",\""+ client.getInetAddress().getHostAddress()+"\",\""+ port+"\");";
                statement=connection.createStatement();
                statement.execute(sql1);
                return true;
            }
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

    private ArrayList<MyMessage> GetMessages(){
        ArrayList<MyMessage> messages = new ArrayList<>();
        try{
            String user1 = dataInput.readUTF();
            String user2 = dataInput.readUTF();
            String sql = "SELECT * from messages where sender or receiver=\"" + user1 + "\" and sender or reciever =\""+ user2 + "\";";
            Statement stmt = connection.createStatement();
            ResultSet rs= stmt.executeQuery(sql);
            while(rs.next()){
                MyMessage message = new MyMessage();
                message.setMessage(rs.getString("message"));
                message.setReciever(rs.getString("reciever"));
                message.setSender(rs.getString("sender"));
                message.setTime(rs.getTimestamp("time"));
                messages.add(message);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return messages;
    }


    private boolean SendMessage(){
        try{
            boolean flag=false;
            MyMessage message = (MyMessage) ObjectInput.readObject();
            String sql1 = "Insert into messages values(NULL,\""+ message.getSender()+ "\", \""+ message.getReciever() +"\" , \"" + message.getMessage() + "\", \"" + message.getTime()+ "\");";
            Statement stmt = connection.createStatement();
            stmt.execute(sql1);
            String receiver_m=message.getReciever();
            String sql2 = "Select * from Online_Status where Username=\'"+receiver_m+"\';";
            stmt=connection.createStatement();
            ResultSet rs=stmt.executeQuery(sql2);
            while(rs.next()) {
                flag = true;
            //    receiver_t = rs.getString("status");
            }
            if(flag==false) {
                String sql3 = "Insert into Notifications VALUES(\"" + receiver_m + "\",\"" + message.getSender() + "\",\"1\");";
                stmt = connection.createStatement();
                stmt.execute(sql3);
            }
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private byte[] BufftoByte(File f){
        try {
            BufferedImage img = ImageIO.read(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(img,"jpg",baos);
            baos.flush();
            byte [] image =baos.toByteArray();
            baos.close();
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static BufferedImage BytetoBuff(byte[] img) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(img);
            BufferedImage image = ImageIO.read(bis);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
