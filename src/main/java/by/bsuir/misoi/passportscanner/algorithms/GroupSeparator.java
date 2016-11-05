package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;

public final class GroupSeparator {

    public static BufferedImage findPhoto(final BufferedImage sourceImage, final int[] groupedPixels, final int groupsCount) throws Exception {
        final int[] position = getGroup(sourceImage, groupedPixels, getMaxGroup(groupedPixels, groupsCount));
        return ImageHelper.getSubImage(sourceImage, position[0], position[1], position[2], position[3]);
    }

//    public static LinkedList<BufferedImage> getAllGroups(final BufferedImage sourceImage, final int[] pixels, final int groupsCount) throws Exception {
//        final LinkedList<BufferedImage> images = new LinkedList<>();
//        for (int i = 0; i < groupsCount; i++) {
//            images.add(getGroup(sourceImage, pixels, i));
//        }
//        return images;
//    }

    public static int[] getGroup(final BufferedImage sourceImage, final int[] pixels, final int groupNumber) {
        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();

        int minX = width, maxX = 0, maxY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y * width + x] == groupNumber) {
                    if (minX > x) {
                        minX = x;
                    }
                    if (maxX < x) {
                        maxX = x;
                    }
                    if (maxY < y) {
                        maxY = y;
                    }
                }
            }
        }
        int photoWidth = maxX - minX;
        int photoHeight = photoWidth / 3 * 4;
        int minY = maxY - photoHeight;
        return new int[] {minX, minY, photoWidth, photoHeight};
    }

    public static int getMaxGroup(final int[] pixels, final int groupsCount) {
        int[] counts = new int[groupsCount + 1];
        final int white = ColorRGB.getWhiteColor();

        for (int pixel : pixels) {
            if (pixel != white && pixel > 0 && pixel < groupsCount) {
                counts[pixel]++;
            }
        }

        int maxSize = 0;
        int index = 0;

        for (int i = 0; i < groupsCount; i++) {
            if (maxSize < counts[i]) {
                maxSize = counts[i];
                index = i;
            }
        }
        return index;
    }
}
