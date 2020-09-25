package ir.rfazli.soccerstar.core;

import ir.rfazli.soccerstar.model.Action;
import ir.rfazli.soccerstar.model.Board;
import org.opencv.core.Point;

import java.awt.*;

public class ActAction {

    private MyRobot robot;

    private boolean teamTurn = true;

    public ActAction(MyRobot myRobot) throws AWTException {
        this.robot = myRobot;
    }

    public void doIt(Action action, Board boardInfo) {

        if (action == null)
            return;
        if (boardInfo == null)
            return;
        teamTurn = !teamTurn;
        Point player = teamTurn ? boardInfo.getMyTeam().get(action.getPlayer()) : boardInfo.getSecondTeam().get(action.getPlayer());
        robot.shoot(player, new Point(200, 200));
    }
}
