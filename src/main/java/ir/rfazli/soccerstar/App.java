package ir.rfazli.soccerstar;

import ir.rfazli.soccerstar.core.ActAction;
import ir.rfazli.soccerstar.core.Detector;
import ir.rfazli.soccerstar.core.MyRobot;
import ir.rfazli.soccerstar.logic.MyTeamLogic;
import ir.rfazli.soccerstar.logic.TeamLogic;
import ir.rfazli.soccerstar.model.Action;
import ir.rfazli.soccerstar.model.Board;
import org.opencv.core.Core;

import java.awt.image.BufferedImage;

public class App {

    public static void main(String[] args) throws Exception {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Detector detector = new Detector();
        MyRobot myRobot = new MyRobot();
        TeamLogic teamLogic = new MyTeamLogic();
        ActAction actAction = new ActAction(myRobot);

        while (true) {
            BufferedImage captureScreen = myRobot.captureScreen();
            Board boardInfo = detector.getBoardInfo(captureScreen);
            myRobot.displayImage(boardInfo.getImage());
            Action action = teamLogic.play(boardInfo);
            actAction.doIt(action, boardInfo);
            Thread.sleep(3000);
        }
    }
}
