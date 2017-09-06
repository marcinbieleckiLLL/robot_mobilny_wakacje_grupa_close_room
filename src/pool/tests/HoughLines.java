package pool.tests;

import javafx.scene.image.Image;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import pool.utils.CVLoader;
import Canny.RobotOrders;

import java.io.ByteArrayInputStream;

import static sample.Main1.IMAGE_VIEW;

public class HoughLines {

	public static volatile Image IMAGE;

	public static boolean nextFrameLines = true;
	public static boolean see = false;
 	public static boolean seen = false;
	 HoughLines (int k ) {
		int goLeft = 0;
		int goRight = 0;
		int goAhead = 0;
		int progKata = 70;
		 int przechyl = 15;
		CVLoader.load();
		seen = see;
		see = false;
		nextFrameLines = true;
		// load the image
		Mat img = Imgcodecs.imread("transformationPhotos\\"+k+"transformated.jpg");

		double prog = 0.9 *  img.rows(); // do ustawienia
		
		// generate gray scale and blur
		Mat gray = new Mat();
		Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
		Imgproc.blur(gray, gray, new Size(1, 1));
		
		// detect the edges
		Mat edges = new Mat();
		int lowThreshold = 50;
		int ratio = 3;
		Imgproc.Canny(gray, edges, lowThreshold, lowThreshold * ratio);
		Mat lines = new Mat();
		Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 50, 50, 10);

		for(int i = 0; i < lines.rows(); i++) {
			double[] val = lines.get(i, 0);

			Imgproc.line(img, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 10);

			double kat = obliczAlfa(val[0],val[1],val[2],val[3]);
			// bierze pod uwage linie na ksztalt \/ zeby nie pomylic z fugami poziomymi
			if (Math.abs(kat)<progKata){
				if(Math.abs(kat)<15){
					goAhead++;
				}
				if(kat<-15 && kat>-progKata){
					goRight++;
				}
				if(kat>15 && kat<progKata){
					goLeft++;
				}
			}
			if (Math.abs(kat)>progKata && (val[1] > prog || val[3] > prog)){ //prog do ustawienia
				see = true;
				//Imgproc.line(img, new Point(val[0], val[1]), new Point(val[2], val[3]), new Scalar(0, 0, 255), 10);
			}
		}
		 if (goRight >= goAhead && goRight >= goLeft && goRight != 0) {
			 RobotOrders.sendOrder("turnRight");
		 }
		 if (goAhead > goRight && goAhead > goLeft) {
			 RobotOrders.sendOrder("goAhead");
		 }
		 if (goLeft >= goAhead && goLeft >= goRight && goLeft != 0) {
			 RobotOrders.sendOrder("turnLeft");
		 }
		 if (!see && true)
			 RobotOrders.sendOrder("nextTile");

		 MatOfByte byteMat = new MatOfByte();
		 Imgcodecs.imencode(".bmp", img, byteMat);
		 IMAGE = new Image(new ByteArrayInputStream(byteMat.toArray()));
		 IMAGE_VIEW.setImage(IMAGE);
	}

	public static double obliczAlfa(double x1,double y1,double x2,double y2)
	{
		double dy = y2-y1;
		double dx = x2-x1;
		double alfa = Math.atan(dx/dy);
		return alfa*180/3.14;
	}

}
