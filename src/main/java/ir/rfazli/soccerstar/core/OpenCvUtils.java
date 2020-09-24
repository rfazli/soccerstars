package ir.rfazli.soccerstar.core;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class OpenCvUtils {

    public Mat loadImage(String path) {
        String filename = "D:\\sample3.png";
        return Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
    }

    public Mat getMat(BufferedImage inImage) {

        if (inImage == null)
            return null;

        byte[] data;
        if (inImage.getRaster().getDataBuffer() instanceof DataBufferByte) {
            data = ((DataBufferByte) inImage.getRaster().getDataBuffer()).getData();
        } else if (inImage.getRaster().getDataBuffer() instanceof DataBufferInt) {
            BufferedImage image = new BufferedImage(inImage.getWidth(), inImage.getWidth(), BufferedImage.TYPE_3BYTE_BGR);
            image.setData(inImage.getRaster());
            data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        } else {
            return null;
        }
        Mat mat = new Mat(inImage.getHeight(), inImage.getWidth(), CvType.CV_8UC3);
        mat.put(0, 0, data);
        return mat;
    }

    public BufferedImage getImage(Mat m) {

        if (m == null)
            return null;

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    public Mat resize(Mat in, float scale) {
        if (in == null)
            return null;
        Mat resizeImage = new Mat();
        Size sz = new Size(in.width() * scale, in.height() * scale);
        Imgproc.resize(in, resizeImage, sz);
        return resizeImage;
    }

    public double colorDistance(Scalar c1, Scalar c2) {
        double r = Math.pow(c1.val[0] - c2.val[0], 2);
        double g = Math.pow(c1.val[1] - c2.val[1], 2);
        double b = Math.pow(c1.val[2] - c2.val[2], 2);
        return Math.pow(r + g + b, 1.0 / 1.3);
    }

    public double distance(Point a, Point b) {
        double distance = 0.0;

        if (a != null && b != null) {
            double xDiff = a.x - b.x;
            double yDiff = a.y - b.y;
            distance = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
        }
        return distance;
    }

    public Scalar detectColor(Mat src, Point center, int radius) {

        if (src == null)
            return new Scalar(0, 0, 0);

        radius -= 5;
        Mat area = src.submat((int) (center.y - radius), (int) (center.y + radius), (int) (center.x - radius), (int) (center.x + radius));
        double r = 0;
        double g = 0;
        double b = 0;
        double total = area.rows() * area.cols();
        for (int i = 0; i < area.rows(); i++) {
            for (int j = 0; j < area.cols(); j++) {
                r += area.get(i, j)[0];
                g += area.get(i, j)[1];
                b += area.get(i, j)[2];
            }
        }
        return new Scalar(r / total, g / total, b / total);
    }

}
