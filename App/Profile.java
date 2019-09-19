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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.Main;

import java.io.File;
import java.io.IOException;

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

    @FXML
    private JFXTextField statustxt;

    @FXML
    private JFXToggleButton tstatus;


    private File file;

    private SceneChange changer = new SceneChange();

    //Functions for Toggle Button State and TextField Enabling
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
    void estatus(ActionEvent event) {
        if(tstatus.isSelected()){
            statustxt.setDisable(false);
        }
        else{
            statustxt.setDisable(true);
        }
    }
    @FXML
    private void initialize(){
        //Initializing all Textfields as disabled
        first.setDisable(true);
        last.setDisable(true);
        email.setDisable(true);
        input.setDisable(true);
        //Getting User Data to Update fields
        Main.user.sendString("GetProfile");
        Main.user.sendString(SessionInfo.getUsername());
        User user =(User) Main.user.recieveObject();
        //Setting Data
        first.setText(user.getFirst());
        last.setText(user.getLast());
        email.setText(user.getEmail());
    }

    @FXML
    private void insert(ActionEvent click){
        //Uploading New Avatar for User
        FileChooser fileChooser = new FileChooser();
        //Set Filter
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPEG Files", "*.jpg"),new FileChooser.ExtensionFilter("PNG Files", "*.png"));
        //Get file
        file = fileChooser.showOpenDialog((Stage)((Node)(click.getSource())).getScene().getWindow());
        if(!file.exists()){
            //Check for File
            Alert alert = new Alert(Alert.AlertType.ERROR, "File doesn't exist");
            alert.showAndWait();
        }
        else{
            filename.setText(file.getName());
        }
    }

    @FXML
    private void back(ActionEvent click){
        changer.changeScene("../App/Anchor.fxml", click, "Home");
    }

    //Update Profile Action
    @FXML
    private void update(ActionEvent click){
        //Sending Data to be updated with User Details
        Main.user.sendString("UpdateProfile");
        Main.user.sendString(SessionInfo.getUsername());
        Main.user.sendString(first.getText());
        Main.user.sendString(last.getText());
        Main.user.sendString(email.getText());
        try {
            System.out.println("NO error");
            Main.user.sendObject(file);
            //Check if update successful

            boolean check = Main.user.recieveBoolean();
            System.out.println("Waiting over");
            if(check) {
                //Show success alert
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Profile update successful");
                changer.changeScene("../App/Anchor.fxml", click, "Home");
            }
            else {
                //Show Failure Alert
                Alert alert = new Alert(Alert.AlertType.ERROR, "Check your details");
                alert.showAndWait();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    //Window Controls

    public void close(MouseEvent click){
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.close();
    }

    public void minimize(MouseEvent click){
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.toBack();
    }
}
