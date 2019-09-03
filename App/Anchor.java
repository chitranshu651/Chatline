package App;

import Misc.SessionInfo;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Anchor
{

    @FXML
    private Label first;

    @FXML
    private Label last;

    @FXML
    private Label status;

    @FXML
    private ImageView avatar;


    public void initialize(){
        String username = SessionInfo.getUsername();

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
