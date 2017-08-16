package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {


    private static final String TITLE = "Robot Mobilny";
    private static final int WIDTH_OF_SCENE = 1000;
    private static final int HEIGHT_OF_SCENE = 800;

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(new Scene(root, WIDTH_OF_SCENE, HEIGHT_OF_SCENE));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
