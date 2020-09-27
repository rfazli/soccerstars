package ir.rfazli.soccerstar;

import ir.rfazli.soccerstar.core.ActAction;
import ir.rfazli.soccerstar.core.Detector;
import ir.rfazli.soccerstar.core.MyRobot;
import ir.rfazli.soccerstar.core.OpenCvUtils;
import ir.rfazli.soccerstar.logic.MyTeamLogic;
import ir.rfazli.soccerstar.logic.TeamLogic;
import ir.rfazli.soccerstar.model.Action;
import ir.rfazli.soccerstar.model.Board;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.awt.image.BufferedImage;
import java.util.List;

public class App {

    private static boolean turn = true;

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Detector detector = new Detector();
        MyRobot myRobot = new MyRobot();
        OpenCvUtils openCvUtils = new OpenCvUtils();
        TeamLogic team1 = new MyTeamLogic();
        TeamLogic team2 = new MyTeamLogic();
        ActAction actAction = new ActAction(myRobot);

        long c = 0;
        while (c++ < Long.MAX_VALUE) {
            turn = !turn;
            BufferedImage captureScreen = myRobot.captureScreen();
            Board boardInfo = detector.getBoardInfo(captureScreen);

            List<Point> turnTeam;
            Action action;
            if (turn) {
                turnTeam = boardInfo.getMyTeam();
                action = team1.play(turnTeam, boardInfo.getSecondTeam(), boardInfo.getBall());
            } else {
                turnTeam = boardInfo.getSecondTeam();
                action = team2.play(turnTeam, boardInfo.getMyTeam(), boardInfo.getBall());
            }
            actAction.doIt(action, turnTeam, boardInfo.getBall(), boardInfo.getImage());

            Mat resize = openCvUtils.resize(boardInfo.getImage(), 0.5f);
            BufferedImage image = openCvUtils.getImage(resize);
            myRobot.displayImage(image);

            Thread.sleep(3000);
        }
    }
}
