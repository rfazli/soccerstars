package ir.rfazli.soccerstar.core;

import ir.rfazli.soccerstar.model.Action;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.List;

public class ActAction {

    private MyRobot robot;

    public ActAction(MyRobot myRobot) {
        this.robot = myRobot;
    }

    public void doIt(Action action, List<Point> team, Point ball, Mat src) {

        if (action == null || team == null || ball == null)
            return;
        Point player = team.get(action.getPlayer());

        double x = Math.cos(action.getAngle()) * action.getPower();
        double y = Math.sin(action.getAngle()) * action.getPower();

        Imgproc.line(src, player, new Point(player.x + x, player.y + y), new Scalar(0, 255, 0), 3);
        robot.shoot(player, new Point(x, y));
    }
}
