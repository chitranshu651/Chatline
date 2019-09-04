package App;

import Login_Signup.User;
import Misc.SessionInfo;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Main;

import java.util.ArrayList;

public class Anchor
{

    @FXML
    private Label first;

    @FXML
    private Label last;

    @FXML
    private Label status;

    @FXML
    private ImageView avatar;

    @FXML
    private Label user;

    @FXML
    private JFXListView friend;

    public void initialize(){
        String username = SessionInfo.getUsername();
        Main.user.sendString("GetProfile");
        Main.user.sendString(username);
        User temp= (User)Main.user.recieveObject();
        first.setText(temp.getFirst());
        last.setText(temp.getLast());
        status.setText(temp.getStatus());
        user.setText(temp.getUsername());
        updateFriends(username);
    }

    private void updateFriends(String username) {
        Main.user.sendString("GetFriends");
        Main.user.sendString(username);
        ArrayList<User> friends=(ArrayList<User>) Main.user.recieveObject();
        ObservableList<User> toset= FXCollections.observableArrayList(friends);
        friend.setItems(toset);


    }


    public void close(MouseEvent click){
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.close();
    }

    public void minimize(MouseEvent click){
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.toBack();
    }

}
