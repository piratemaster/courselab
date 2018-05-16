package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
    public static Stage stage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Красно-черное дерево");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
        stage.setResizable(false);
    }



    public static void main(String[] args) {
        launch(args);
    }
}
