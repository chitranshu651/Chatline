package sample;

import Misc.SessionInfo;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application {

    public static Server_Client.Client user= new Server_Client.Client("127.0.0.1", 5005);
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../Login_Signup/Login.fxml"));
        primaryStage.setTitle("Welcome to ChatLine");
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception{
        Main.user.sendString("Disconnect");
        Main.user.sendString(SessionInfo.getUsername());
        SessionInfo.getVideocalling().threadRun=false;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
