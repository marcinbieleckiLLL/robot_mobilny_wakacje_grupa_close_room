package sample;

import Canny.FaceDetection;
import Map.Map;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import org.opencv.core.Core;
import org.opencv.videoio.VideoCapture;

import java.io.IOException;

import static sample.Connection.listOfAtributtesOfConnection;

public class Controller {

    //MY_IP_ADRESS='192.168.1.155'
    //DEVICE_IP_ADRESS='192.168.1.2'

    @FXML private TextField inputIP;
    @FXML private Label labelIP;
    @FXML private TextField orderArea;
    @FXML private StackPane stackPane;
    public static String ip;
    private Connection connection = new Connection();
    public static VideoCapture WEBSOURCE = null;
    MapAction mapAction;
    public boolean isStreamRunning = false;
    public static XconYconList xconYcon = new XconYconList();

    public enum Properties{
        TO_ROBOT("TO_ROBOT", 5000),
        FROM_ROBOT("FROM_ROBOT", 5006);

        String name;
        int port;

        Properties(String name, int port) {
            this.name = name;
            this.port = port;
        }
    }

    @FXML public void initialize() {
        inputIP.setTranslateY(-150);
        labelIP.setTranslateY(-150);
        stackPane.setTranslateX(57);
        stackPane.setTranslateY(-117);
        mapAction = new MapAction();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @FXML protected void handleSubmitButtonActionIp(ActionEvent event) throws IOException, InterruptedException {
        ip = inputIP.getText();
        new Thread(new Connection.runServer(Properties.FROM_ROBOT)).start();
        new Thread(new Connection.runClient(Properties.TO_ROBOT)).start();
    }

    @FXML protected void handleSubmitButtonActionOrder(ActionEvent event) throws IOException {
        String message = orderArea.getText();
        connection.sendMessage(message, listOfAtributtesOfConnection.getClientByName(Properties.TO_ROBOT.name).getOut());
    }

    @FXML protected void handleSubmitButtonActionStream(ActionEvent event) throws IOException {
        //WEBSOURCE = new VideoCapture("http://169.254.28.99:8080/video");
        //WEBSOURCE = new VideoCapture("http://169.254.28.99:8080/shot.jpg");
        //WEBSOURCE = new VideoCapture("http://169.254.28.99:8080/mjpg/mjpeg.cgi");
        //WEBSOURCE = new VideoCapture("http://169.254.28.99:8080/mjpeg.cgi");
        //WEBSOURCE = new VideoCapture("http://169.254.28.99:8080/mjpeg.cgi");
        //WEBSOURCE = new VideoCapture("http://169.254.28.99:8080/stream.html");
        //WEBSOURCE = new VideoCapture("pi:raspberry@169.254.28.99");
        //WEBSOURCE = new VideoCapture("pi:raspberry@169.254.28.99/video.‌​cgi?.mjpg");
        FaceDetection.DaemonThread myThread = new FaceDetection().new DaemonThread();
        if(isStreamRunning){
            myThread.runnable = false;
            WEBSOURCE.release();
            isStreamRunning = false;
        }else if(!isStreamRunning){
            WEBSOURCE = new VideoCapture(0);
            Thread t = new Thread(myThread);
            t.setDaemon(true);
            myThread.runnable = true;
            t.start();
            isStreamRunning = true;
        }

    }

    @FXML
    private RadioButton leftButtonDir;
    @FXML
    private RadioButton rightButtonDir;
    @FXML
    private RadioButton upButtonDir;
    @FXML
    private RadioButton downButtonDir;
    @FXML
    private Label label;
    @FXML
    void goButtonClicked(ActionEvent event) {
        mapAction.addOneAction("go", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        // sprawdzanie czy punkt jest na mapie i czy nie jest sciana
        if(upButtonDir.isSelected()
                && Map.getyCon() -2>=0
                && !Map.getGrid()[Map.getxCon()][Map.getyCon() -1].isWall )
            Map.setyCon(Map.getyCon()-1);
        if(downButtonDir.isSelected()
                && Map.getyCon() +2<Map.getyTiles()
                && !Map.getGrid()[Map.getxCon()][Map.getyCon() +1].isWall)
            Map.setyCon(Map.getyCon()+1);
        if(leftButtonDir.isSelected()
                && Map.getxCon()-2>=0
                && !Map.getGrid()[Map.getxCon()-1][Map.getyCon() ].isWall)
            Map.setxCon(Map.getxCon()-1);
        if(rightButtonDir.isSelected()
                && Map.getxCon()+2<Map.getxTiles()
                && !Map.getGrid()[Map.getxCon()+1][Map.getyCon() ].isWall)
            Map.setxCon(Map.getxCon()+1);
        //otwieranie nowego kafla
        Map.getGrid()[Map.getxCon()][Map.getyCon() ].open();
    }
    //-------------------------------------------------------------Robienie sciany--------------------------------------------
    public void straightButtonSelected(ActionEvent actionEvent) {
        mapAction.addOneAction("straight", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        if(upButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
        if(downButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
        if(leftButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if(rightButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
    }

    public void leftButtonSelected(ActionEvent actionEvent) {
        mapAction.addOneAction("left", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        if(upButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if(downButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
        if(leftButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
        if(rightButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
    }

    public void rightButtonSelected(ActionEvent actionEvent) {
        mapAction.addOneAction("right", checkWhatButtonIsSelected());
        xconYcon.add(Map.getxCon(), Map.getyCon());

        if(upButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
        if(downButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if(leftButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
        if(rightButtonDir.isSelected()){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
    }
//----------------------------------------------------------------------------------------------------------------------

    // obrot robota w prawo lub lewo o 90 stopni.
    public void rotateLeftButtonSelected(ActionEvent actionEvent) {
        xconYcon.add(Map.getxCon(), Map.getyCon());
            if (upButtonDir.isSelected()) {
                leftButtonDir.setSelected(true);
                label.setText("Left");
                mapAction.addOneAction(null, "leftButtonDir");
            } else if (leftButtonDir.isSelected()) {
                downButtonDir.setSelected(true);
                label.setText("Down");
                mapAction.addOneAction(null, "downButtonDir");
            } else if (downButtonDir.isSelected()) {
                rightButtonDir.setSelected(true);
                label.setText("Right");
                mapAction.addOneAction(null, "rightButtonDir");
            } else if (rightButtonDir.isSelected()) {
                upButtonDir.setSelected(true);
                label.setText("Up");
                mapAction.addOneAction(null, "upButtonDir");
            }

    }

    public void rotateRightButtonSelected(ActionEvent actionEvent) {
        xconYcon.add(Map.getxCon(), Map.getyCon());
            if (upButtonDir.isSelected()) {
                rightButtonDir.setSelected(true);
                label.setText("Right");
                mapAction.addOneAction(null, "rightButtonDir");
            } else if (rightButtonDir.isSelected()) {
                downButtonDir.setSelected(true);
                label.setText("Down");
                mapAction.addOneAction(null, "downButtonDir");
            } else if (downButtonDir.isSelected()) {
                leftButtonDir.setSelected(true);
                label.setText("Left");
                mapAction.addOneAction(null, "leftButtonDir");
            } else if (leftButtonDir.isSelected()) {
                upButtonDir.setSelected(true);
                label.setText("Up");
                mapAction.addOneAction(null, "upButtonDir");
            }

    }

    public void doGoButton(String button){
       // System.out.println("xcon: " + Map.xCon + "ycon: " + Map.yCon);
        //System.out.println("xcon1: " + xconYcon.getXcon() + "ycon1: " + xconYcon.getYcon());
        if("upButtonDir".equals(button)
                && Map.getyCon() -2>=0
                && !Map.getGrid()[Map.getxCon()][Map.getyCon() -1].isWall )
            Map.setyCon(Map.getyCon()-1);
        if("downButtonDir".equals(button)
                && Map.getyCon() +2<Map.getyTiles()
                && !Map.getGrid()[Map.getxCon()][Map.getyCon() +1].isWall)
            Map.setyCon(Map.getyCon()+1);
        if("leftButtonDir".equals(button)
                && Map.getxCon()-2>=0
                && !Map.getGrid()[Map.getxCon()-1][Map.getyCon() ].isWall)
            Map.setxCon(Map.getxCon()-1);
        if("rightButtonDir".equals(button)
                && Map.getxCon()+2<Map.getxTiles()
                && !Map.getGrid()[Map.getxCon()+1][Map.getyCon() ].isWall)
            Map.setxCon(Map.getxCon()+1);
        //otwieranie nowego kafla
        Map.getGrid()[Map.getxCon()][Map.getyCon() ].open();
    }

    public void doStraightButton(String button) {
        if ("upButtonDir".equals(button)) {
            Map.getGrid()[Map.getxCon()][Map.getyCon() - 1].makeWall();
        }
        if ("downButtonDir".equals(button)) {
            Map.getGrid()[Map.getxCon()][Map.getyCon() + 1].makeWall();
        }
        if ("leftButtonDir".equals(button)) {
            Map.getGrid()[Map.getxCon() - 1][Map.getyCon()].makeWall();
        }
        if ("rightButtonDir".equals(button)) {
            Map.getGrid()[Map.getxCon() + 1][Map.getyCon()].makeWall();
        }
    }

    public void doLeftButton(String button){
        if("upButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if("downButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
        if("leftButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
        if("rightButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
    }

    public void doRightButton(String button){
        if("upButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()+1][Map.getyCon()].makeWall();
        }
        if("downButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()-1][Map.getyCon()].makeWall();
        }
        if("leftButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()][Map.getyCon()-1].makeWall();
        }
        if("rightButtonDir".equals(button)){
            Map.getGrid()[Map.getxCon()][Map.getyCon()+1].makeWall();
        }
    }

    private String checkWhatButtonIsSelected(){
        if(upButtonDir.isSelected())
            return "upButtonDir";
        else if(downButtonDir.isSelected())
            return "downButtonDir";
        else if (rightButtonDir.isSelected())
            return "rightButtonDir";
        else if (leftButtonDir.isSelected())
            return "leftButtonDir";
        return null;
    }
}
