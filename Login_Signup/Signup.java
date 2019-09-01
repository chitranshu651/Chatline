package Login_Signup;

import Misc.ConnectionClass;
import Misc.PasswordUtils;
import Misc.SceneChange;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import sample.Main;

import java.io.IOException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Signup {

    private User register;

    @FXML
    private JFXTextField first;

    @FXML
    private JFXTextField last;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXTextField user;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private JFXPasswordField confirm;

    @FXML
    private Label status;

    @FXML
    private Label status2;

    private ConnectionClass database = new ConnectionClass();
    private Connection connection = database.getconnection();
    private SceneChange changer = new SceneChange();


    @FXML
    private void labelupdate() {
        if (user.getText().equals("")) {
            status.setText("Empty");
        } else {
            if (checkuser()) {
                status.setText("Ok");
            } else {
                status.setText("Taken");
            }
        }
    }

    @FXML
    private void initialize() {

        //All of the Require field validators for the TextFields
        RequiredFieldValidator validator = new RequiredFieldValidator();
        validator.setMessage("Input Required");
        first.getValidators().add(validator);
        first.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                first.validate();
        });
        last.getValidators().add(validator);
        last.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                last.validate();
        });
        email.getValidators().add(validator);
        email.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                email.validate();
        });
        user.getValidators().add(validator);
        user.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                user.validate();
        });
        pass.getValidators().add(validator);
        pass.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                pass.validate();
        });
        confirm.getValidators().add(validator);
        confirm.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal)
                confirm.validate();
        });
    }

    @FXML
    private void checkpasses(){
        if(confirm.getText().equals(pass.getText())){
            status2.setText("Match");
        }
        else{
            status2.setText("NO Match");
        }
    }


    @FXML
    private void signup(ActionEvent click) throws InvalidKeySpecException, IOException {
        if (confirm.getText().equals(pass.getText()) && checkuser()) {
                PasswordUtils passgen = new PasswordUtils();
                String salt = passgen.getSalt(30);
                String hashed = passgen.generateSecurePassword(pass.getText(), salt);
                register = new User(first.getText(), last.getText(), email.getText(), user.getText(),hashed ,salt,"Available" );
                Main.user.sendString("Signup");
                Main.user.sendObject(register);
                boolean check = Main.user.recieveBoolean();
                if(check){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Success");
                    alert.setContentText("You have been registered");
                    alert.showAndWait();
                    changer.changeScence("../Login_Signup/Login.fxml", click, "Login");
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Failure");
                    alert.setHeaderText("Failure");
                    alert.setContentText("Registration Failed");
                    alert.showAndWait();
                }

            }
            else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Password Mismatch");
            alert.setHeaderText("Error:");
            alert.setContentText("Passwords Do not match");
            alert.showAndWait();
        }
    }

    private boolean checkuser() {
        try {
            String sql = "SELECT COUNT(Username) as count FROM user WHERE Username =  '" + user.getText() + "' ;";
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql);
            while (rs.next()) {
                if (rs.getInt(1) == 0) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }
}
