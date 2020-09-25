package ir.rfazli.soccerstar.model;

public class Action {

    private int player;
    private int power;
    private float angle;

    public Action(int player, int power, float angle) {
        this.player = player;
        this.power = power;
        this.angle = angle;
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

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
