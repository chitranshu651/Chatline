package App;

import Misc.Iclose;
import Misc.SceneChange;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Profile implements Iclose {

    @FXML
    private JFXTextField first;

    @FXML
    private JFXTextField last;

    @FXML
    private JFXTextField email;

    @FXML
    private Label filename;

    private SceneChange changer = new SceneChange();

    @FXML
    private void back(ActionEvent click){
        changer.changeScene(".//App/Anchor.fxml", click, "Home");
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
