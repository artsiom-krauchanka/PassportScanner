package by.bsuir.misoi.passportscanner.utils;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.io.File;
import java.io.IOException;

public class ImageHelper {

    private static final String DOT = ".";
    private static final String JPG_EXTENSION = "jpg";
    private static final String TRANSFORMED_PATH_SUFFIX = "-TRANSFORMED";
    private static volatile int counter = 0;

    private ImageHelper() {
    }

    public static BufferedImage readImage(String path) throws IOException {
        return path != null ? ImageIO.read(new File(path)) : null;
    }

    public static void saveImage(BufferedImage image, String path) throws IOException {
        if (image != null)
            ImageIO.write(image, JPG_EXTENSION, new File(getTransformedPath(path)));
    }

    public static BufferedImage getSubImage(final BufferedImage sourceImage, int x, int y, int width, int height) {
        BufferedImage img;
        try {
            img = sourceImage.getSubimage(x, y, width, height);
        } catch (RasterFormatException e) {
            System.err.printf("RasterFormatException: BufferedImage#getSubimage(%d, %d, %d, %d)\r\n", x, y, width, height);
            e.printStackTrace();
            return null;
        }
        return img;
    }

    public static BufferedImage getImageFromPixels(int[] pixels, int width, int height, int imageType) {
        BufferedImage image = new BufferedImage(width, height, imageType);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    public static int[] getPixels(BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = new int[width * height];
        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[index++] = image.getRGB(x, y);
            }
        }
        return pixels;
    }

    private static String getTransformedPath(String imagePath) {
        String path = FilenameUtils.removeExtension(imagePath);
        String extension = FilenameUtils.getExtension(imagePath);
        return path + TRANSFORMED_PATH_SUFFIX + ++counter + DOT + extension;
    }


    public static BufferedImage rotate(BufferedImage image, double angle) {
        int width = image.getWidth();
        int height = image.getHeight();

        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x0 = 0.5 * (width - 1);     // point to rotate about
        double y0 = 0.5 * (height - 1);     // center of image

        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // rotation
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double a = x - x0;
                double b = y - y0;
                int xx = (int) (+a * cos - b * sin + x0);
                int yy = (int) (+a * sin + b * cos + y0);

                // plot pixel (x, y) the same color as (xx, yy) if it's in bounds
                result.setRGB(x, y, (xx >= 0 && xx < width && yy >= 0 && yy < height) ? image.getRGB(xx, yy) : ColorRGB.getWhiteColor());
            }
        }

        return result;
    }

}
