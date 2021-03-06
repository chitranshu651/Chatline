package App;

import AudioCalling.audioClientThread;
import Login_Signup.User;
import Misc.*;
import VideoCalling.VideoCall1;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Anchor implements Iclose {

    @FXML
    private Label first;

    @FXML
    private Label last;

    @FXML
    private Label status;

    @FXML
    private Circle avatar;

    @FXML
    private Label user;

    @FXML
    private ListView friend;

    @FXML
    private Label email;

    private SceneChange changer = new SceneChange();

    @FXML
    private Pane proflie;

    @FXML
    private Pane chat;

    @FXML
    private JFXListView messageloader;

    @FXML
    private JFXTextField usermessage;

    @FXML
    private Label chatname;

    @FXML
    private Circle chatavatar;

    @FXML
    private Label chatstatus;

    @FXML
    private Circle green;

    @FXML
    private Circle red;

    @FXML
    private JFXButton abtn;

    @FXML
    private JFXButton vbtn;

    private User recieved;

    @FXML
    private void addFriend(ActionEvent click) {
        changer.changeScene("../App/addFriend.fxml", click, "Add Friend");
    }

    public void initialize() throws IOException {
        //Send Details of User to Update Profile
        proflie.setVisible(true);
        chat.setVisible(false);
        String username = SessionInfo.getUsername();
        System.out.println("Up until here");
        Main.user.sendString("GetProfile");
        Main.user.sendString(username);
        User temp = (User) Main.user.recieveObject();
        System.out.println("Object Recieved");
        Image img = SwingFXUtils.toFXImage(SessionInfo.BytetoBuff(temp.getImage()), null);
        System.out.println("Image Conversion Error");
        //Setting of User Details in Profile Page
        avatar.setFill(new ImagePattern(img));
        System.out.println("Retrieve Done");
        first.setText(temp.getFirst());
        last.setText(temp.getLast());
        status.setText(temp.getStatus());
        user.setText(temp.getUsername());
        updateFriends(username);
        System.out.println("This done");
        email.setText(temp.getEmail());
        Main.user.sendString("CountRequests");
        Main.user.sendString(SessionInfo.getUsername());
        String notification = Main.user.recieveString();
        Alert alert = new Alert(Alert.AlertType.INFORMATION, notification);
        alert.showAndWait();
        // refresh();

    }

    @FXML
    private void editprofile(ActionEvent click) {
        changer.changeScene("../App/Profile.fxml", click, "Edit Profile");
    }


    private void updateFriends(String username) throws IOException {
        //Send User Details

        Main.user.sendString("GetFriends");
        Main.user.sendString(username);
        //Recieve List of Friends
        ArrayList<User> friends = (ArrayList<User>) Main.user.recieveObject();
        friend.setItems(FXCollections.observableArrayList(friends));
        ArrayList a = new ArrayList();
        //Creating Custom List View for Displaying Friends
        if (friends.size() != 0) {
            for (User test : friends) {
                HBox hbox = new HBox();
                hbox.setPadding(new Insets(10, 10, 10, 10));
                Circle cir = new Circle(30);
                hbox.setSpacing(25);
                //Image conversion from file format
                Image img = SwingFXUtils.toFXImage(SessionInfo.BytetoBuff(test.getImage()), null);
                cir.setFill(new ImagePattern(img));
                Label lbl = new Label(test.getUsername());
                lbl.setFont(new Font(15));
                Label mess = new Label(test.getStatus());
                mess.setFont(new Font(12));
                VBox vbox = new VBox();
                vbox.getChildren().addAll(lbl, mess);
                hbox.getChildren().addAll(cir, vbox);
                a.add(hbox);
            }
        }
        friend.setItems(FXCollections.observableArrayList(a));
        friend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                proflie.setVisible(false);
                chat.setVisible(true);
                String tosend = ((Label) ((VBox) ((HBox) (friend.getSelectionModel().getSelectedItem())).getChildren().get(1)).getChildren().get(0)).getText();
                Main.user.sendString("GetProfile");
                Main.user.sendString(tosend);
                recieved = (User) Main.user.recieveObject();
                chatname.setText(recieved.getFirst() + " " + recieved.getLast());
                chatstatus.setText(recieved.getStatus());
                try {
                    chatavatar.setFill(new ImagePattern(SwingFXUtils.toFXImage(SessionInfo.BytetoBuff(recieved.getImage()), null)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Main.user.sendString("CheckOnline");
                Main.user.sendString(tosend);
                if (Main.user.recieveBoolean()) {
                    green.setVisible(true);
                    red.setVisible(false);
                    abtn.setDisable(false);
                    vbtn.setDisable(false);

                } else {
                    red.setVisible(true);
                    green.setVisible(false);
                    abtn.setDisable(true);
                    vbtn.setDisable(true);

                }
                refresh1();


            }
        });
    }

    @FXML
    private void request(ActionEvent event) {
        changer.changeScene("../App/Request.fxml", event, "See Requests");
    }

    @FXML
    private void startVideo(ActionEvent event) {
        Main.user.sendString("StartVideo");
        Main.user.sendString(recieved.getUsername());
        IPClass ipClass = (IPClass) Main.user.recieveObject();
        VideoCall1 videoCall = new VideoCall1(ipClass.getIp(), ipClass.getPort());
        videoCall.start();
        audioClientThread audt = new audioClientThread(ipClass.getIp());
        audt.start();

    }

    @FXML
    private void startAudio(ActionEvent event) {
        //TODO Audio Call
        Main.user.sendString("StartVideo");
        Main.user.sendString(recieved.getUsername());
        IPClass ipClass = (IPClass) Main.user.recieveObject();
        audioClientThread audt = new audioClientThread(ipClass.getIp());
        audt.start();
    }

    @FXML
    private void send(ActionEvent event) {
        MyMessage message = new MyMessage();
        message.setSender(SessionInfo.getUsername());
        message.setReciever(recieved.getUsername());
        message.setMessage(usermessage.getText());
        message.setTime(new Timestamp(System.currentTimeMillis()));
        Main.user.sendString("SendMessage");
        try {
            Main.user.sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void home(ActionEvent click) {
        proflie.setVisible(true);
        chat.setVisible(false);
    }

    //Window Controls
    public void close(MouseEvent click) {
        /*VideoCallingService1 video = SessionInfo.getVideocalling();
        video.exit=true;
        */
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.close();


    }

    public void minimize(MouseEvent click) {
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.toBack();
    }


    private void refresh1() {
        Main.user.sendString("GetMessages");
        Main.user.sendString(SessionInfo.getUsername());
        Main.user.sendString(recieved.getUsername());
        ArrayList<MyMessage> messages = (ArrayList) Main.user.recieveObject();
        ArrayList<String> todisplay = new ArrayList<>();
        for (MyMessage ms : messages) {
            String text = "[" + ms.getTime() + "] " + ms.getSender() + " : " + ms.getMessage();
            todisplay.add(text);
        }
        messageloader.setItems(FXCollections.observableArrayList(todisplay));
    }

    @FXML
    private void refresh(ActionEvent event) {
        Main.user.sendString("GetMessages");
        Main.user.sendString(SessionInfo.getUsername());
        Main.user.sendString(recieved.getUsername());
        ArrayList<MyMessage> messages = (ArrayList) Main.user.recieveObject();
        ArrayList<String> todisplay = new ArrayList<>();
        for (MyMessage ms : messages) {
            String text = "[" + ms.getTime() + "] " + ms.getSender() + " : " + ms.getMessage();
            todisplay.add(text);
        }
        messageloader.setItems(FXCollections.observableArrayList(todisplay));
    }

}
