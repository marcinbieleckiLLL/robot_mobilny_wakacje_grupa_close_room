package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.IOException;


public class Controller {

    @FXML private TextField inputIP;
    @FXML private MediaView mediaView;
    private static boolean isCameraOn = false;
    private static String ip;

    @FXML public void initialize() {
        MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("muzeum_lotnictwa.mp4").toExternalForm()));
        player.play();

        mediaView.setMediaPlayer(player);
    }

    @FXML protected void handleSubmitButtonActionIp(ActionEvent event) throws IOException {
        ip = inputIP.getText();
        PythonClass pythonClass = new PythonClass();
        Thread t = new Thread(new PythonClass.sendData());
        t.start();
        pythonClass.runPythonScript(isCameraOn, ip);
    }

    @FXML protected void handleSubmitButtonActionCamera(ActionEvent event) throws IOException {
        isCameraOn = isCameraOn ? false : true;
        PythonClass pythonClass = new PythonClass();
        pythonClass.sendDataToPythonScript(isCameraOn, ip);
    }

    @FXML protected void handleSubmitButtonActionStream(ActionEvent event) throws IOException {
        MediaPlayer player = mediaView.getMediaPlayer();
        MediaPlayer.Status currentStatus = player.getStatus();

        if(currentStatus == MediaPlayer.Status.PLAYING)
            player.pause();
        else if(currentStatus == MediaPlayer.Status.PAUSED || currentStatus == MediaPlayer.Status.STOPPED) {
            player.play();
        }
    }
}
