package sample;

import Map.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;

import static Map.Map.*;

public class Main1 extends Application {

    private static final String TITLE = "Robot Mobilny";
    public static int WIDTH_OF_SCENE;
    public static int HEIGHT_OF_SCENE;

    public static double FIRST_WIDTH;
    public static double FIRST_HEIGHT;

    public static ImageView IMAGE_VIEW;
    public static Pane MAP_PANE;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Map map = new Map();
        GridPane root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        countScreenSize();
         MAP_PANE = map.createContent();
        IMAGE_VIEW = new ImageView();
        translateMapPane();
        drawImage();

        root.getChildren().addAll(IMAGE_VIEW);
        root.getChildren().addAll(MAP_PANE);

        primaryStage.setTitle(TITLE);
        primaryStage.setScene(createScene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void countScreenSize(){
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        WIDTH_OF_SCENE = (int) primaryScreenBounds.getWidth();
        HEIGHT_OF_SCENE = (int) primaryScreenBounds.getHeight();
        FIRST_WIDTH = WIDTH_OF_SCENE;
        FIRST_HEIGHT = HEIGHT_OF_SCENE;
    }

    private void drawImage(){
        File file = new File("src/dark.jpg");
        Image image = new Image(file.toURI().toString());
        IMAGE_VIEW.setImage(image);

        IMAGE_VIEW.maxHeight(HEIGHT_OF_SCENE/2);
        IMAGE_VIEW.maxWidth((WIDTH_OF_SCENE/2) - 100);
        IMAGE_VIEW.setTranslateX(-(WIDTH_OF_SCENE/4) + 50);
        IMAGE_VIEW.setTranslateY((HEIGHT_OF_SCENE/4) - 50);
    }

    private void translateMapPane(){
        MAP_PANE.setTranslateX(WIDTH_OF_SCENE/3 + 100);
        MAP_PANE.setTranslateY(HEIGHT_OF_SCENE/3 - 150);
    }

    private void redrawMapPane(GridPane root, double newValue, double oldValue){
        Map map = new Map();
        double w = W * (newValue / oldValue);
        TILE_SIZE = (int) (w / X_TILES);

        root.getChildren().remove(MAP_PANE);
        MAP_PANE = map.createContent();
        drawPastStatesOfMap();
        translateMapPane();

        root.getChildren().addAll(MAP_PANE);
        root.requestLayout();
    }

    private Scene createScene(GridPane root){
        Scene scene = new Scene(root, WIDTH_OF_SCENE, HEIGHT_OF_SCENE);
        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            redrawMapPane(root, newSceneWidth.doubleValue(), FIRST_WIDTH);
        });

        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) ->{
            //redrawMapPane(root, newSceneHeight.doubleValue(), FIRST_HEIGHT);
        });
        return scene;
    }

    private void drawPastStatesOfMap(){
            for (int i = 0; i < MapAction.getListOfSelectedButtons().size(); i++) {
                Map.setxCon(Controller.xconYcon.getXconYconById(i).getXcon());
                Map.setyCon(Controller.xconYcon.getXconYconById(i).getYcon());
                drawPastStates(MapAction.getListOfSelectedButtons().get(i));
            }
    }

    private void drawPastStates(ButtonsNames buttonsNames){
        Controller controller = new Controller();
        if(buttonsNames.getNameOfButton() != null){
            if(onePastStatesIf("go", buttonsNames))
                controller.doGoButton(buttonsNames.getRadioButton());
            if(onePastStatesIf("straight", buttonsNames))
                controller.doStraightButton(buttonsNames.getRadioButton());
            if(onePastStatesIf("left", buttonsNames))
                controller.doLeftButton(buttonsNames.getRadioButton());
            if(onePastStatesIf("right", buttonsNames))
                controller.doRightButton(buttonsNames.getRadioButton());
        }
    }

    private boolean onePastStatesIf(String finalNameOfButton, ButtonsNames buttonsNames){
        String nameOfButton = buttonsNames.getNameOfButton();
        if(nameOfButton.equalsIgnoreCase(finalNameOfButton))
            return true;
        return false;
    }
}
