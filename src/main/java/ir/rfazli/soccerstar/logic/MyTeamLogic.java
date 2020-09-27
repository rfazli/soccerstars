package ir.rfazli.soccerstar.logic;

import ir.rfazli.soccerstar.core.OpenCvUtils;
import ir.rfazli.soccerstar.model.Action;
import org.opencv.core.Point;

import java.util.List;
import java.util.Random;

public class MyTeamLogic implements TeamLogic {

    private OpenCvUtils openCvUtils = new OpenCvUtils();

    @Override
    public Action play(List<Point> myTeam, List<Point> secondTeam, Point ball) {
        if (myTeam == null || myTeam.isEmpty() || ball == null)
            return null;

        Action action = new Action();
        int player = new Random().nextInt(myTeam.size());
        action.setPlayer(player);
        action.setPower(150);
        float angle = openCvUtils.getAngle(myTeam.get(player), ball);
        action.setAngle(angle);

        return action;
    }
}
