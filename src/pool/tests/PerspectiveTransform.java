
package pool.tests;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import pool.utils.CVLoader;

import java.util.ArrayList;
import java.util.List;

public class PerspectiveTransform {
	static List<Point> corners = new ArrayList<Point>();
	static List<Point> target = new ArrayList<Point>();
	static Mat img;
	static Mat img2;
	static Mat proj;
	public static Mat trans;
	static Mat invTrans;
    public static boolean HougLinesPossible = true;
	MatOfByte mem = new MatOfByte();

	  public PerspectiveTransform (int p) {
		CVLoader.load();
		img = Imgcodecs.imread("transformationPhotos\\"+p+".jpg");
		if(p==1)
		img2 = Imgcodecs.imread("transformationPhotos\\"+p+".jpg");
		target.add(new Point(0, 0));
		target.add(new Point(img.cols(), 0));
		target.add(new Point(img.cols(), img.rows()));
		target.add(new Point(0, img.rows()));
//-------------------------------------------- PARAMETR PRZY ZMIANIE PERSPEKTYWY ( przeksztalcenie perspektywistyczne bierze trapez wzgledem ktorego przestawia i ma on ksztalt /\
// i parametr perspectiveDistance mowi i tym ile pikseli jest oddalony gorny lewy rog trapezu od gornego lewego rogu prostokatnego obrazka (to sie nastroi do kamerki)
		int perspectiveDistance=150;
//-------------------------------------------------------------------------------------------------------------
		// lewy gorny rog
		corners.add(new Point(perspectiveDistance,0));
		// prawy gorny rog
		corners.add(new Point(img.cols()-perspectiveDistance,0));
		//prawy dolny
		corners.add(new Point(img.cols(), img.rows()));
		//lewy dolny
		corners.add(new Point(0, img.rows()));
		doProjection();
		Imgcodecs.imwrite("transformationPhotos\\"+p+"transformated.jpg",proj);
		if(!img.empty())
			new HoughLines(p);
		HougLinesPossible = true;
		target.clear();
		corners.clear();
		}

	private static void doProjection () {
		if(corners.size() == 4) {
			Mat cornersMat = Converters.vector_Point2f_to_Mat(corners);
			Mat targetMat = Converters.vector_Point2f_to_Mat(target);
			trans = Imgproc.getPerspectiveTransform(cornersMat, targetMat);
			invTrans = Imgproc.getPerspectiveTransform(targetMat, cornersMat);
			proj = new Mat();
			if(!img.empty())
				Imgproc.warpPerspective(img, proj, trans, new Size(img.cols(), img.rows()));
		}
	}
}
