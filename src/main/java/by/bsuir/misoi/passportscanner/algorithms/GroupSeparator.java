package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;

import java.awt.image.BufferedImage;

/**
 * Created by Artem on 24-Sep-16.
 */
public final class GroupSeparator {

    public static BufferedImage findPhoto(final BufferedImage sourceImage, final int[] pixels, final int groupsCount){
        final int groupNumber = getMaxIndex(pixels, groupsCount);

        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();

        int minX = width, maxX = 0, maxY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(pixels[y*width+x] == groupNumber){
                    if(minX > x)
                        minX = x;
                    if(maxX < x)
                        maxX = x;
                    if(maxY < y)
                        maxY = y;
                }
            }
        }

        int photoWidth = maxX - minX;
        int photoHeight = photoWidth / 3 * 4;
        int minY = maxY - photoHeight;

        return sourceImage.getSubimage(minX, minY, photoWidth, photoHeight);
    }

    private static int getMaxIndex(final int[] pixels, final int groupsCount){
        int[] counts = new int[groupsCount+1];
        final int white = ColorRGB.getWhiteColor();

        for(int i = 0; i < pixels.length; i++)
            if(pixels[i] != white && pixels[i] > 0)
                counts[pixels[i]]++;

        int maxSize = 0;
        int index = 0;

        for(int i = 0; i < groupsCount; i++){
            if(maxSize < counts[i]){
                maxSize = counts[i];
                index = i;
            }
        }
        return index;
    }

}
