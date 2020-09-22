package ir.rfazli.soccerstar;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;

public class App {
    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Detector detector = new Detector();
        MyRobot myRobot = new MyRobot();
        OpenCvUtils openCvUtils = new OpenCvUtils();

        for (int i = 0; i < 20000; i++) {
            BufferedImage captureScreen = myRobot.captureScreen();
            Mat mat = openCvUtils.getMat(captureScreen);
            detector.detectGameBoard(mat);
            detector.circles(mat);
            Mat resize = openCvUtils.resize(mat, 0.5f);
            BufferedImage image = openCvUtils.getImage(resize);
            myRobot.displayImage(image);
            Thread.sleep(1000);
        }
        System.exit(0);
    }
}
