package Misc;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public interface Iclose {

    default void close(MouseEvent click) {
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.close();
    }

    default void minimize(MouseEvent click) {
        Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
        window.hide();
    }
}
