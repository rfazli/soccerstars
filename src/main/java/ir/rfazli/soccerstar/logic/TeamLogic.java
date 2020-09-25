package ir.rfazli.soccerstar.logic;

import ir.rfazli.soccerstar.model.Action;
import org.opencv.core.Point;

import java.util.List;

public interface TeamLogic {

    Action play(List<Point> myTeam, List<Point> secondTeam, Point ball);
}
