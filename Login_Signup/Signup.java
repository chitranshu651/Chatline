package Login_Signup;

import Misc.ConnectionClass;
import Misc.PasswordUtils;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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

    private ConnectionClass database = new ConnectionClass();
    private Connection connection = database.getconnection();


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
    private void signup(ActionEvent click) {
        if (confirm.getText().equals(pass.getText()) && checkuser()) {
            try {
                PasswordUtils passgen = new PasswordUtils();
                String salt = passgen.getSalt(30);
                String hashed = passgen.generateSecurePassword(pass.getText(), salt);
                register = new User(first.getText(), last.getText(), email.getText(), user.getText(),hashed ,salt,"Available" );
                String sql= "INSERT INTO user values(\"" + register.getFirst() + "\", \""+ register.getLast() + "\", \""+ register.getEmail() + "\", \"" + register.getUsername() + "\", \"" + register.getPassword() + "\", \"" + register.getSalt() + "\", \""+ register.getStatus() + "\");";
                Statement statement = connection.createStatement();
                statement.execute(sql);
                System.out.println("Done");
            }
            catch(Exception e){
                System.out.println(e);
            }
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
