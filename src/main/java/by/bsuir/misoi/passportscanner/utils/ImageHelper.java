package by.bsuir.misoi.passportscanner.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Artem on 21-Sep-16.
 */
public class ImageHelper {

    public static void saveImage(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "jpg", new File(path));
    }

    public static BufferedImage getImageFromPixels(int[] pixels, int width, int height, int imageType) {
        BufferedImage image = new BufferedImage(width, height, imageType);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    public static int[] getPixels(BufferedImage image) {
        final int width = image.getWidth();
        final int height = image.getHeight();
        final int[] pixels = new int[width*height];

        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                pixels[index++] = image.getRGB(x, y);
            }
        }

        return pixels;
    }
}
