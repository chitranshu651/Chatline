package Login_Signup;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import sample.Main;

public class Login {

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
        }
        else{
            System.out.println("Unsuccessful");
        }
    }
}
