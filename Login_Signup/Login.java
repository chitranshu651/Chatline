package Login_Signup;

import Misc.Iclose;
import Misc.SceneChange;
import Misc.SessionInfo;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import sample.Main;

public class Login implements Iclose {

    @FXML
    private JFXTextField user;

    @FXML
    private JFXPasswordField pass;

    @FXML
    public void initialize(){
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input Required");
        user.getValidators().add(validator);
        user.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                user.validate();
        });
        user.getValidators().add(validator);
        user.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                user.validate();
        });
    }

    SceneChange changer =new SceneChange();
    @FXML
    private void Login(ActionEvent click){
        String username = user.getText();
        String password = pass.getText();
        Main.user.sendString("Login");
        Main.user.sendString(username);
        Main.user.sendString(password);
        boolean login = Main.user.recieveBoolean();
        if(login){
            System.out.println("Login Successful");
            SessionInfo.setUsername(username);
            changer.changeScene("../App/Anchor.fxml", click, "Hi");
        }
        else{
            System.out.println("Unsuccessful");
        }
    }
    @FXML
    private void Signup(MouseEvent click){
        changer.changeScene("../Login_Signup/Signup.fxml", click, "Signup");
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
