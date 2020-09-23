package ir.rfazli.soccerstar;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Detector {

    public void detectMember(Mat src) {
        Scalar playerColor = new Scalar(255, 0, 255);
        Scalar ballColor = new Scalar(255, 0, 0);
        Mat player = detectPlayer(src);
        Mat ball = detectBall(src);

        if (player == null)
            return;

        if (player.cols() != 10)
            System.out.println("error in detect player: " + player.cols());

        for (int x = 0; x < player.cols(); x++) {
            double[] c = player.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, playerColor, 3, 8, 0);
        }

        if (ball.cols() == 1) {
            double[] c = ball.get(0, 0);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            Imgproc.circle(src, center, radius, ballColor, 3, 8, 0);
            return;
        }

        ball:
        for (int i = 0; i < ball.cols(); i++) {

            double[] bc = ball.get(0, i);
            Point ballCenter = new Point(Math.round(bc[0]), Math.round(bc[1]));
            int radius = (int) Math.round(bc[2]);

            for (int j = 0; j < player.cols(); j++) {
                double[] pc = player.get(0, j);
                Point playerCenter = new Point(Math.round(pc[0]), Math.round(pc[1]));
                if (distance(ballCenter, playerCenter) < 10)
                    continue ball;
            }
            Imgproc.circle(src, ballCenter, radius, ballColor, 3, 8, 0);
        }
    }

    private double distance(Point a, Point b) {
        double distance = 0.0;

        if (a != null && b != null) {
            double xDiff = a.x - b.x;
            double yDiff = a.y - b.y;
            distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        }
        return distance;
    }

    private Mat detectPlayer(Mat src) {
        return this.circles(src, 100.0, 30.0, 25, 30);
    }

    private Mat detectBall(Mat src) {
        return this.circles(src, 12.0, 25.0, 10, 20);
    }

    private Mat circles(Mat src, double p1, double p2, int min, int max) {

        if (src == null)
            return null;

        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.medianBlur(gray, gray, 5);
        Mat circles = new Mat();
        Imgproc.HoughCircles(gray, circles, Imgproc.HOUGH_GRADIENT, 1,
                (double) gray.rows() / 16.0, // change this value to detect circles with different distances to each other
                p1, p2, min, max); // change the last two parameters
        return circles;
    }

    public Rect detectGameBoard(Mat src) {

        if (src == null)
            return null;

        int widthStep = (src.width() - 30) / 5;
        int heightStep = (src.height() - 30) / 6;
        Point pt1 = new Point(widthStep / 2 + 10, heightStep + 30);
        Point pt2 = new Point(src.width() - (widthStep / 2) - 50, src.height() - 17);
        Imgproc.rectangle(src, pt1, pt2, new Scalar(255, 0, 0, .8), 4);
        return new Rect(pt1, pt2);
    }
}
