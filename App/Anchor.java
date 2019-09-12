package App;

import Login_Signup.User;
import Misc.Iclose;
import Misc.SceneChange;
import Misc.SessionInfo;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import sample.Main;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Anchor implements Iclose
{

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

    private SceneChange changer =new SceneChange();

    public void initialize() throws IOException {
        String username = SessionInfo.getUsername();
        Main.user.sendString("GetProfile");
        Main.user.sendString(username);
        User temp= (User)Main.user.recieveObject();
        Main.user.sendInt(1);
        File f = (File) Main.user.recieveObject();
        Image img = SwingFXUtils.toFXImage(ImageIO.read(f),null);
        avatar.setFill(new ImagePattern(img));
        first.setText(temp.getFirst());
        last.setText(temp.getLast());
        status.setText(temp.getStatus());
        user.setText(temp.getUsername());
        updateFriends(username);
        email.setText(temp.getEmail());
    }

    @FXML
    private void editprofile(ActionEvent click){
        changer.changeScene("../App/Profile.fxml",click,"Edit Profile");
    }

    private void updateFriends(String username) throws IOException {
        Main.user.sendString("GetFriends");
        Main.user.sendString(username);
        ArrayList<User> friends=(ArrayList<User>) Main.user.recieveObject();
        friend.setItems(FXCollections.observableArrayList(friends));
        ArrayList a = new ArrayList();
        for(User test : friends){
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(10,10,10,10));
            Circle cir = new Circle(30);
            hbox.setSpacing(25);
            Image img = SwingFXUtils.toFXImage(ImageIO.read(test.getPic()),null);
            cir.setFill(new ImagePattern(img));
            Label lbl = new Label(test.getUsername());
            lbl.setFont(new Font(15));
            Label mess = new Label(test.getStatus());
            mess.setFont(new Font(12));
            VBox vbox = new VBox();
            vbox.getChildren().addAll(lbl,mess);
            hbox.getChildren().addAll(cir,vbox);
            a.add(hbox);
        }
        friend.setItems(FXCollections.observableArrayList(a));
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
