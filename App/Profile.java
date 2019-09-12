package App;

import Login_Signup.User;
import Misc.Iclose;
import Misc.SceneChange;
import Misc.SessionInfo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Main;

public class Profile implements Iclose {

    @FXML
    private JFXTextField first;

    @FXML
    private JFXTextField last;

    @FXML
    private JFXTextField email;

    @FXML
    private Label filename;

    @FXML
    private JFXButton input;

    @FXML
    private JFXToggleButton tfirst;

    @FXML
    private JFXToggleButton tlast;

    @FXML
    private JFXToggleButton temail;

    @FXML
    private JFXToggleButton tavatar;

    private SceneChange changer = new SceneChange();

    @FXML
    void eavatar(ActionEvent event) {
        if(tavatar.isSelected()){
            input.setDisable(false);
        }
        else{
            input.setDisable(true);
        }
    }

    @FXML
    void eemail(ActionEvent event) {
        if(temail.isSelected()){
            email.setDisable(false);
        }
        else{
            email.setDisable(true);
        }
    }

    @FXML
    void efname(ActionEvent event) {
        if(tfirst.isSelected()){
            first.setDisable(false);
        }
        else{
            first.setDisable(true);
        }
    }

    @FXML
    void elname(ActionEvent event) {
        if(tlast.isSelected()){
            last.setDisable(false);
        }
        else{
            last.setDisable(true);
        }
    }
    @FXML
    private void initialize(){
        first.setDisable(true);
        last.setDisable(true);
        email.setDisable(true);
        input.setDisable(true);
        Main.user.sendString("GetProfile");
        Main.user.sendString(SessionInfo.getUsername());
        User user =(User) Main.user.recieveObject();
        first.setText(user.getFirst());
        last.setText(user.getLast());
        email.setText(user.getEmail());
    }

    @FXML
    private void back(ActionEvent click){
        changer.changeScene("../App/Anchor.fxml", click, "Home");
    }

    @FXML
    private void update(ActionEvent click){
        //UPDATE CODE
        changer.changeScene("../App/Anchor.fxml", click, "Home");
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
