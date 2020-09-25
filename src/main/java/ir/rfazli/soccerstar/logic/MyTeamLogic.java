package ir.rfazli.soccerstar.logic;

import ir.rfazli.soccerstar.model.Action;
import ir.rfazli.soccerstar.model.Board;

import java.util.Random;

public class MyTeamLogic implements TeamLogic {

    @Override
    public Action play(Board board) {
        if (board.getMyTeam() == null || board.getMyTeam().size() == 0)
            return null;
        Action action = new Action();
        action.setPlayer(new Random().nextInt(board.getMyTeam().size()));
        return action;
    }
}
