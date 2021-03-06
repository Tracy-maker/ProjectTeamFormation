package PTF;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainScene.fxml"));
        primaryStage.setTitle("Team Formation");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMinHeight(660);
        primaryStage.setMinWidth(1150);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
