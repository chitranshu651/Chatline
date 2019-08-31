package Misc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChange {

    public void changeScence(String Filename, ActionEvent click, String Title) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(Filename));
            Scene scene = new Scene(parent);

            Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
            window.setTitle(Title);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void changeScence(String Filename, ActionEvent click, String Title, int width, int height) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(Filename));
            Scene scene = new Scene(parent,width,height);

            Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
            window.setTitle(Title);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.out.print(e);
        }
    }
}
