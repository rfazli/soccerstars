package ir.rfazli.soccerstar.model;

public class Action {

    private int player;
    private int power;
    private int degree;

    public Action(int player, int power, int degree) {
        this.player = player;
        this.power = power;
        this.degree = degree;
    }

    public Action() {
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }
}
