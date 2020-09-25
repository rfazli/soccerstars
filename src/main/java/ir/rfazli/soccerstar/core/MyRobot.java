package ir.rfazli.soccerstar.core;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;
import org.opencv.core.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.util.List;

public class MyRobot {
    private JFrame frame;
    private JLabel lbl;
    private Rectangle windowsPosition;
    private Robot robot;

    public MyRobot() throws AWTException {
        robot = new Robot();
    }

    public BufferedImage captureScreen() {
        Rectangle windows = getWindows();
        if (windows != null)
            return robot.createScreenCapture(windows);
        else
            return null;
    }

    public void displayImage(Image image) {

        if (image == null)
            return;

        if (frame == null) {
            init(image);
        } else {
            ImageIcon icon = new ImageIcon(image);
            lbl.setIcon(icon);
        }
    }

    private void init(Image image) {
        ImageIcon icon = new ImageIcon(image);
        frame = new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(image.getWidth(null) + 50, image.getHeight(null) + 50);
        this.lbl = new JLabel();
        lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public Rectangle getWindows() {
        Rectangle rect = null;
        List<DesktopWindow> allWindows = WindowUtils.getAllWindows(true);
        for (DesktopWindow desktopWindow : allWindows) {
            if (desktopWindow.getTitle().contains("MEmu")) {
                rect = desktopWindow.getLocAndSize();
                break;
            }
        }
        this.windowsPosition = rect;
        return rect;
    }

    public void shoot(Point p, Point drag) {
        if (windowsPosition == null || p == null)
            return;
        int x = (int) (windowsPosition.getX() + p.x);
        int y = (int) (windowsPosition.getY() + p.y);
        robot.mouseMove(x, y);
        robot.delay(200);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(200);

        int dragStep = 100;
        double xStep = drag.x / (double) dragStep;
        double yStep = drag.y / (double) dragStep;
        for (int i = 1; i <= dragStep; i++) {
            int newX = (int) Math.round(xStep * (double) i);
            int newY = (int) Math.round(yStep * (double) i);
            robot.mouseMove(x + newX, y + newY);
            robot.delay(10);
        }

        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

}
