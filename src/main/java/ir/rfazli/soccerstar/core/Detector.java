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
    private MyRobot myRobot;

    public Detector(MyRobot myRobot) {
        this.openCvUtils = new OpenCvUtils();
        this.myRobot = myRobot;
    }

    public Board getBoardInfo() {
        BufferedImage captureScreen = myRobot.captureScreen();
        Mat src = openCvUtils.getMat(captureScreen);

        if (src == null)
            return null;

        Board boardInfo = this.getBoardInfo(src);
        boardInfo.setImage(src);
        return boardInfo;
    }

    private Board getBoardInfo(Mat src) {
        Board boardInfo = new Board();

        Rect gameBoardRec = this.detectGameBoard(src);
        int turn = this.detectTurn(src, gameBoardRec);
        Mat player = detectPlayer(src, gameBoardRec);
        Mat ball = detectBall(src, gameBoardRec);

        boardInfo.setTurn(turn);
        Scalar ballColor = new Scalar(0, 255, 255);
        Point boardBoardCenter = new Point(gameBoardRec.width / 2, gameBoardRec.height / 2);

        if (player == null)
            return null;

        for (int x = 0; x < player.cols(); x++) {
            double[] c = player.get(0, x);
            Point center = new Point(Math.round(c[0] + gameBoardRec.x), Math.round(c[1] + gameBoardRec.y));
            int radius = (int) Math.round(c[2]);
            Scalar playerColor = openCvUtils.detectColor(src, center, radius);

            boolean isMyTeam = true;
            if (myTeamColor == null) {
                myTeamColor = playerColor;
                boardInfo.addMyTeam(center);
            } else {
                double distance = openCvUtils.colorDistance(myTeamColor, playerColor);
                if (distance > 500) {
                    boardInfo.addSecondTeam(center);
                    isMyTeam = false;
                } else {
                    boardInfo.addMyTeam(center);
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
            Point ballCenter = new Point(Math.round(bc[0] + gameBoardRec.x), Math.round(bc[1] + gameBoardRec.y));
            boardInfo.setBall(ballCenter);
            int radius = (int) Math.round(bc[2]);
            Imgproc.circle(src, ballCenter, radius, ballColor, Imgproc.FILLED, 8, 0);
        } else {
            for (double[] bc : detectedBall) {
                Point ballCenterInBoard = new Point(Math.round(bc[0]), Math.round(bc[1]));
                int radius = (int) Math.round(bc[2]);
                if (openCvUtils.distance(ballCenterInBoard, boardBoardCenter) < 5)
                    continue;
                Point ballCenter = new Point(Math.round(bc[0] + gameBoardRec.x), Math.round(bc[1] + gameBoardRec.y));
                boardInfo.setBall(ballCenter);
                Imgproc.circle(src, ballCenter, radius, ballColor, Imgproc.FILLED, 8, 0);
            }
        }

        return boardInfo;
    }

    private Mat detectPlayer(Mat src, Rect gameBoardRec) {
        return this.circles(src, gameBoardRec, 100.0, 30.0, 25, 30);
    }

    private Mat detectBall(Mat src, Rect gameBoardRec) {
        return this.circles(src, gameBoardRec, 12.0, 25.0, 10, 20);
    }

    private Mat circles(Mat src, Rect gameBoardRec, double p1, double p2, int min, int max) {

        if (src == null)
            return null;

        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        gray = gray.submat(gameBoardRec.y, gameBoardRec.y + gameBoardRec.height, gameBoardRec.x, gameBoardRec.x + gameBoardRec.width);
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

    private int detectTurn(Mat src, Rect gameBoardRec) {
        Point pt1 = gameBoardRec.tl();
        Point pt2 = gameBoardRec.br();

        Rect t1 = new Rect(new Point(0, 0), pt1);
        Rect t2 = new Rect(new Point(pt2.x, pt1.y), new Point(pt2.x + pt1.x, 0));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BufferedImage captureScreen = myRobot.captureScreen();
        Mat src2 = openCvUtils.getMat(captureScreen);

        Mat t11 = src.submat(t1.y, t1.y + t1.height, t1.x, t1.x + t1.width);
        Mat t12 = src2.submat(t1.y, t1.y + t1.height, t1.x, t1.x + t1.width);
        Mat t21 = src.submat(t2.y, t2.y + t2.height, t2.x, t2.x + t2.width);
        Mat t22 = src2.submat(t2.y, t2.y + t2.height, t2.x, t2.x + t2.width);

        double sim1 = openCvUtils.similarity(t11, t12);
        double sim2 = openCvUtils.similarity(t21, t22);

        int turn = 0;
        if (sim1 > sim2) {
            turn = 1;
        } else if (sim2 > sim1) {
            turn = 2;
        }

        Imgproc.rectangle(src, t1.tl(), t1.br(), new Scalar(255, 0, 0, .8), turn == 1 ? Imgproc.FILLED : Imgproc.LINE_4);
        Imgproc.rectangle(src, t2.tl(), t2.br(), new Scalar(255, 0, 0, .8), turn == 2 ? Imgproc.FILLED : Imgproc.LINE_4);

        return turn;
    }
}
