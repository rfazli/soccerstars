package ir.rfazli.soccerstar.core;

import ir.rfazli.soccerstar.model.Board;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Detector {

    private Scalar myTeamColor;
    private OpenCvUtils openCvUtils;

    public Detector() {
        this.openCvUtils = new OpenCvUtils();
    }

    public Board getBoardInfo(BufferedImage captureScreen) {
        Mat src = openCvUtils.getMat(captureScreen);
        Rect rect = this.detectGameBoard(src);
        src = src.submat(rect.y, rect.y + rect.height, rect.x, rect.x + rect.width);
        Board boardInfo = this.getBoardInfo(src);
        Mat resize = openCvUtils.resize(src, 0.5f);
        BufferedImage image = openCvUtils.getImage(resize);
        boardInfo.setImage(image);
        return boardInfo;
    }

    private Board getBoardInfo(Mat src) {
        Board board = new Board();
        Scalar ballColor = new Scalar(0, 255, 255);
        Mat player = detectPlayer(src);
        Mat ball = detectBall(src);
        Point boardCenter = new Point(src.cols() / 2, src.rows() / 2);

        if (player == null)
            return null;

        for (int x = 0; x < player.cols(); x++) {
            double[] c = player.get(0, x);
            Point center = new Point(Math.round(c[0]), Math.round(c[1]));
            int radius = (int) Math.round(c[2]);
            Scalar playerColor = openCvUtils.detectColor(src, center, radius);

            boolean isMyTeam = true;
            if (myTeamColor == null) {
                myTeamColor = playerColor;
                board.addMyTeam(center);
            } else {
                double distance = openCvUtils.colorDistance(myTeamColor, playerColor);
                if (distance > 500) {
                    board.addSecondTeam(center);
                    isMyTeam = false;
                } else {
                    board.addMyTeam(center);
                }
            }

            Imgproc.circle(src, center, radius, playerColor, Imgproc.FILLED, 8, 0);
            Imgproc.putText(src, isMyTeam ? "1" : "2", center, 2, 1, new Scalar(255, 255, 255));
        }

        List<double[]> detectedBall = new ArrayList<>();

        ball:
        for (int i = 0; i < ball.cols(); i++) {

            double[] bc = ball.get(0, i);
            Point ballCenter = new Point(Math.round(bc[0]), Math.round(bc[1]));

            for (int j = 0; j < player.cols(); j++) {
                double[] pc = player.get(0, j);
                Point playerCenter = new Point(Math.round(pc[0]), Math.round(pc[1]));
                if (openCvUtils.distance(ballCenter, playerCenter) < 10)
                    continue ball;
            }
            detectedBall.add(bc);
        }

        if (detectedBall.size() == 1) {
            double[] bc = detectedBall.get(0);
            Point ballCenter = new Point(Math.round(bc[0]), Math.round(bc[1]));
            board.setBall(ballCenter);
            int radius = (int) Math.round(bc[2]);
            Imgproc.circle(src, ballCenter, radius, ballColor, Imgproc.FILLED, 8, 0);
        } else {
            for (double[] bc : detectedBall) {
                Point ballCenter = new Point(Math.round(bc[0]), Math.round(bc[1]));
                int radius = (int) Math.round(bc[2]);
                if (openCvUtils.distance(ballCenter, boardCenter) < 5)
                    continue;
                board.setBall(ballCenter);
                Imgproc.circle(src, ballCenter, radius, ballColor, Imgproc.FILLED, 8, 0);
            }
        }

        return board;
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

    private Rect detectGameBoard(Mat src) {

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
