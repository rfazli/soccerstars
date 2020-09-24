package ir.rfazli.soccerstar.model;

import org.opencv.core.Point;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Point> myTeam;
    private List<Point> secondTeam;
    private Point ball;
    private BufferedImage image;

    public Board() {
        this.myTeam = new ArrayList<>();
        this.secondTeam = new ArrayList<>();
    }

    public void addMyTeam(Point p) {
        this.myTeam.add(p);
    }

    public void addSecondTeam(Point p) {
        this.secondTeam.add(p);
    }

    public List<Point> getMyTeam() {
        return myTeam;
    }

    public void setMyTeam(List<Point> myTeam) {
        this.myTeam = myTeam;
    }

    public List<Point> getSecondTeam() {
        return secondTeam;
    }

    public void setSecondTeam(List<Point> secondTeam) {
        this.secondTeam = secondTeam;
    }

    public Point getBall() {
        return ball;
    }

    public void setBall(Point ball) {
        this.ball = ball;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}