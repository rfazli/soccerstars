package ir.rfazli.soccerstar.logic;

import ir.rfazli.soccerstar.core.OpenCvUtils;
import ir.rfazli.soccerstar.model.Action;
import org.opencv.core.Point;

import java.util.List;

public class SecondTeamLogic implements TeamLogic {

    private OpenCvUtils openCvUtils = new OpenCvUtils();

    @Override
    public Action play(List<Point> myTeam, List<Point> secondTeam, Point ball) {
        return null;
    }
}
