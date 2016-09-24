package by.bsuir.misoi.passportscanner.utils;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageHelper {

    private static final String JPG_EXTENSION = "jpg";
    private static final String TRANSFORMED_PATH_SUFFIX = "-TRANSFORMED";

    private ImageHelper() {}

    public static void saveImage(BufferedImage image, String path) throws IOException {
        if(image != null)
            ImageIO.write(image, JPG_EXTENSION, new File(getTransformedPath(path)));
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


    private static volatile int counter = 0;

    private static String getTransformedPath(String imagePath) {
        String path = FilenameUtils.removeExtension(imagePath);
        String extension = FilenameUtils.getExtension(imagePath);
        return path + TRANSFORMED_PATH_SUFFIX + ++counter + "." + extension;
    }




}
