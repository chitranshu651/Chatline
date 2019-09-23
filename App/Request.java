package App;

import Login_Signup.User;
import Misc.Iclose;
import Misc.SceneChange;
import Misc.SessionInfo;
import com.jfoenix.controls.JFXListView;
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

import java.util.ArrayList;

public class Request implements Iclose {


    @FXML
    private JFXListView friend;

    @FXML
    private Pane intro;

    @FXML
    private Pane profile;


    @FXML
    private Circle avatar;

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

    public void initialize() {
        try {
            ArrayList a = new ArrayList();
            intro.setVisible(true);
            profile.setVisible(false);
            Main.user.sendString("GetRequest");
            Main.user.sendString(SessionInfo.getUsername());
            ArrayList<User> requests = (ArrayList) Main.user.recieveObject();
            if (requests.size() != 0) {
                for (User test : requests) {
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
                        try {
                            avatar.setFill(new ImagePattern(SwingFXUtils.toFXImage(SessionInfo.BytetoBuff(recieved.getImage()), null)));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void addFriend(ActionEvent event) {
        Main.user.sendString("ReqAccept");
        Main.user.sendString(SessionInfo.getUsername());
        Main.user.sendString(username.getText());
        boolean check = Main.user.recieveBoolean();
        if (check) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Friend Added Successfully");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error Occurred");
            alert.showAndWait();
        }
    }

    @FXML
    private void goBack(ActionEvent event) {
        changer.changeScene("../App/Anchor.fxml", event, "Home");
    }

    public void close(MouseEvent click) {
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.close();
    }

    public void minimize(MouseEvent click) {
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.toBack();
    }

}
