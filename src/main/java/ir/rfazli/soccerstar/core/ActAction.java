package ir.rfazli.soccerstar.core;

import ir.rfazli.soccerstar.model.Action;
import org.opencv.core.Point;

import java.util.List;

public class ActAction {

    private MyRobot robot;

    public ActAction(MyRobot myRobot) {
        this.robot = myRobot;
    }

    public void doIt(Action action, List<Point> team, Point ball, boolean direction) {

        if (action == null || team == null || ball == null)
            return;
        Point player = team.get(action.getPlayer());

        action.setAngle(action.getAngle() * (direction ? 1 : -1));
        double x = Math.cos(action.getAngle()) * action.getPower();
        double y = Math.sin(action.getAngle()) * action.getPower();

        robot.shoot(player, new Point(x, y));
    }
}
