package sample;

import Misc.SceneChange;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class Controller {

    private SceneChange changer = new SceneChange();
    @FXML
    private void Signup(ActionEvent click){

        changer.changeScence("../Login_Signup/Signup.fxml", click, "Signup",600,800);
    }

    @FXML
    private void Login(ActionEvent click){
        changer.changeScence("../Login_Signup/Login.fxml", click, "Login");
    }
}
