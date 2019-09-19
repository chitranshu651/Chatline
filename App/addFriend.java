package App;

import Login_Signup.User;
import Misc.Iclose;
import Misc.SceneChange;
import Misc.SessionInfo;
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

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;

public class addFriend implements Iclose {


    @FXML
    private JFXListView friend;

    @FXML
    private JFXTextField search;

    @FXML
    private Pane intro;

    @FXML
    private Pane profile;


    @FXML
    private Circle avatar;

    @FXML
    private JFXButton add;

    @FXML
    private Label name;

    @FXML
    private Label status;

    @FXML
    private Label username;

    @FXML
    private Label email;

    private User recieved;

    private SceneChange changer = new SceneChange();

    public void initialize(){
        intro.setVisible(true);
        profile.setVisible(false);
        ArrayList a = new ArrayList();
        Main.user.sendString("FriendSuggestion");
        Main.user.sendString(SessionInfo.getUsername());
        ArrayList<User> suggestions = ((ArrayList) Main.user.recieveObject());
        updateListView(a, suggestions);

        friend.setItems(FXCollections.observableArrayList(a));
        friend.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                intro.setVisible(false);
                profile.setVisible(true);
                Main.user.sendString("GetProfile");
                String temp = ((Label) ((VBox) ((HBox) (friend.getSelectionModel().getSelectedItem())).getChildren().get(1)).getChildren().get(0)).getText();
                Main.user.sendString(temp);
                recieved = (User) Main.user.recieveObject();
                name.setText(recieved.getFirst() + " " + recieved.getLast());
                status.setText(recieved.getStatus());
                email.setText(recieved.getEmail());
                username.setText(recieved.getUsername());
                Main.user.sendString("CheckFriend");
                Main.user.sendString(SessionInfo.getUsername());
                Main.user.sendString(temp);
                if(Main.user.recieveBoolean()){
                    add.setDisable(true);
                }
                else{
                    add.setDisable(false);
                }
                try {
                    avatar.setFill(new ImagePattern(SwingFXUtils.toFXImage(ImageIO.read(recieved.getPic()), null)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateListView(ArrayList a, ArrayList<User> suggestions) {
        for (User test : suggestions) {
            HBox hbox = new HBox();
            hbox.setPadding(new Insets(10, 10, 10, 10));
            Circle cir = new Circle(30);
            hbox.setSpacing(25);
            //Image conversion from file format
            Image img = null;
            try {
                img = SwingFXUtils.toFXImage(ImageIO.read(test.getPic()), null);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    @FXML
    private void updateSearch() {
        if (search.getText().equals("")) {

        } else {
            Main.user.sendString("Search");
            Main.user.sendString(search.getText());
            Main.user.sendString(SessionInfo.getUsername());
            ArrayList<User> searchResults = (ArrayList) Main.user.recieveObject();
            ArrayList a = new ArrayList();
            if (searchResults.size() != 0) {
                updateListView(a, searchResults);
            }
            friend.setItems(FXCollections.observableArrayList(a));

        }
    }

    @FXML
    private void addFriend(ActionEvent event){
        Main.user.sendString("FriendReq");
        Main.user.sendString(SessionInfo.getUsername());
        Main.user.sendString(username.getText());
        boolean check = Main.user.recieveBoolean();
        if(check){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Success");
            alert.showAndWait();
            search.setText("");
            profile.setVisible(false);
            intro.setVisible(true);
        }
        else{
            Alert alert =new Alert(Alert.AlertType.ERROR,"An Unexpected Error Occurred");
            alert.showAndWait();
        }
    }


    @FXML
    private void goBack(ActionEvent event){
        changer.changeScene("../App/Anchor.fxml",event, "Home");
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
