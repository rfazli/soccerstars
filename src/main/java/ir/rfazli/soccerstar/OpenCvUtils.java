package ir.rfazli.soccerstar;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

public class OpenCvUtils {

    public Mat loadImage(String path) {
        String filename = "D:\\sample3.png";
        return Imgcodecs.imread(filename, Imgcodecs.IMREAD_COLOR);
    }

    public Mat getMat(BufferedImage inImage) {
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
}
