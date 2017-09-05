package pool.utils;

import java.io.File;

public class CVLoader {
	public static void load() {
		System.load(new File("C:\\dlamarcina\\opencv\\build\\java\\x64\\opencv_java320.dll").getAbsolutePath());
	}
}
