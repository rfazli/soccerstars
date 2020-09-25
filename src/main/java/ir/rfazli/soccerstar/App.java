package ir.rfazli.soccerstar;

import ir.rfazli.soccerstar.core.ActAction;
import ir.rfazli.soccerstar.core.Detector;
import ir.rfazli.soccerstar.core.MyRobot;
import ir.rfazli.soccerstar.logic.MyTeamLogic;
import ir.rfazli.soccerstar.logic.TeamLogic;
import ir.rfazli.soccerstar.model.Action;
import ir.rfazli.soccerstar.model.Board;
import org.opencv.core.Core;
import org.opencv.core.Point;

import java.awt.image.BufferedImage;
import java.util.List;

public class App {

    private static boolean turn = true;

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Detector detector = new Detector();
        MyRobot myRobot = new MyRobot();
        TeamLogic team1 = new MyTeamLogic();
        TeamLogic team2 = new MyTeamLogic();
        ActAction actAction = new ActAction(myRobot);

        while (true) {
            turn = !turn;
            BufferedImage captureScreen = myRobot.captureScreen();
            Board boardInfo = detector.getBoardInfo(captureScreen);
            myRobot.displayImage(boardInfo.getImage());
            List<Point> turnTeam;
            Action action;
            boolean direction;
            if (turn) {
                direction = true;
                turnTeam = boardInfo.getMyTeam();
                action = team1.play(turnTeam, boardInfo.getSecondTeam(), boardInfo.getBall());
            } else {
                direction = false;
                turnTeam = boardInfo.getSecondTeam();
                action = team2.play(turnTeam, boardInfo.getMyTeam(), boardInfo.getBall());
            }

            actAction.doIt(action, turnTeam, boardInfo.getBall(), direction);
            Thread.sleep(6000);
        }
    }
}
