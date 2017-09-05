
package pool.tests;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import pool.app.Calibration;
import pool.utils.*;

import java.util.Arrays;

public class BallDetection {
	public static void main (String[] args) {
		CVLoader.load();
		
		ImgWindow wnd = ImgWindow.newWindow();
		Calibration calib = new Calibration(1280, 800);
		calib.setBackgroundImage(Imgcodecs.imread("screenshots/positions/background.png"));
		BallDetector detector = new BallDetector(calib);
		Mat camera = Imgcodecs.imread("screenshots/positions/camera.png");
		
		 while(true) {
			 detect(wnd, detector, camera);
		 }
	}

	private static void detect (ImgWindow wnd, BallDetector detector, Mat camera) {
		detector.detect(camera);
		
		Mat result = new Mat();
		camera.copyTo(result, detector.getMask());
		
		for(Circle ball: detector.getBalls()) {
			Imgproc.circle(result, new Point(ball.x, ball.y), (int)ball.radius, new Scalar(0, 255, 0), 2);
		}
		
		for(int i = 0; i < detector.getBallClusters().size(); i++) {
			BallCluster cluster = detector.getBallClusters().get(i);
			Imgproc.drawContours(result, Arrays.asList(cluster.getContour()), 0, new Scalar(0, 0, 255), 2);			
			for(Circle circle: cluster.getEstimatedCircles()) {
				Imgproc.circle(result, new Point(circle.x, circle.y), (int)circle.radius, new Scalar(255, 0, 255), 2);
			}
			Imgproc.putText(result, cluster.getNumBalls() + " balls", cluster.getMinRect().center, Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 0));
		}

		Imgproc.putText(result, wnd.mouseX + ", " + wnd.mouseY, new Point(20, 30), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(255, 255, 255));
		wnd.setImage(result);
	}		
}
