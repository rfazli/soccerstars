package ir.rfazli.soccerstar;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MyRobot {
    private JFrame frame;
    private JLabel lbl;

    public MyRobot() {
    }

    public BufferedImage captureScreen() throws AWTException {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle screenRectangle = new Rectangle(0, 20, 1280, 720);
        Robot robot = new Robot();
        return robot.createScreenCapture(screenRectangle);
    }

    public void displayImage(Image image) {
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
}
