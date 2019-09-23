package Misc;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneChange {

    //Change Scene With Action Event for Buttons

    public void changeScene(String Filename, ActionEvent click, String Title) {
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

    //Changes Scene with Button Action Event with dimesions specified
    public void changeScene(String Filename, ActionEvent click, String Title, int width, int height) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(Filename));
            Scene scene = new Scene(parent, width, height);

            Stage window = (Stage) ((Node) click.getSource()).getScene().getWindow();
            window.setTitle(Title);
            window.setScene(scene);
            window.show();
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    //Changes Scene wih Mouse Event for Icons and Other Elements
    public void changeScene(String Filename, MouseEvent click, String Title) {
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

}