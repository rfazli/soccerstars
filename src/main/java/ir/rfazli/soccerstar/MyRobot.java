package ir.rfazli.soccerstar;

import com.sun.jna.platform.DesktopWindow;
import com.sun.jna.platform.WindowUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class MyRobot {
    private JFrame frame;
    private JLabel lbl;

    public MyRobot() {
    }

    public BufferedImage captureScreen() throws AWTException {
        Robot robot = new Robot();
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
        return rect;
    }
}
