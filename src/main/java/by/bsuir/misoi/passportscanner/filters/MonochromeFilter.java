package by.bsuir.misoi.passportscanner.filters;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * Converts an image to a binary one based on given multiplier
 */
public class MonochromeFilter implements Filter{

    private final int multiplier;

    public MonochromeFilter(){
        this.multiplier = 0xFFFFF;
    }

    public MonochromeFilter(int threshold){
        this.multiplier = threshold;
    }


    @Override
    public int[] transform(int width, int height, int[] pixels) {
        final int[] transform = new int[width*height];
        for (int i = 0; i < width*height; i++) {
            transform[i] = pixels[i] & multiplier;
        }
        return transform;
    }


}
