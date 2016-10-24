package by.bsuir.misoi.passportscanner.algorithms;


import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class HoughLineAnalyzer {

    public static BufferedImage getPassportFromList(BufferedImage src, Vector<HoughLine> lines) {
        int xMin = src.getWidth();
        int yMin = src.getHeight();
        int xMax = 0;
        int yMax = 0;
        int height = src.getHeight();
        int width = src.getWidth();

        float centerX = width / 2;
        float centerY = height / 2;

        for (HoughLine line : lines) {
            double tsin = Math.sin(line.theta);
            double tcos = Math.cos(line.theta);
            // During processing h_h is doubled so that -ve r values
            int houghHeight = (int) (Math.sqrt(2) * Math.max(height, width)) / 2;

            if (line.theta < Math.PI * 0.25 || line.theta > Math.PI * 0.75) {
                // Draw vertical-ish lines
                for (int y = 0; y < height; y++) {
                    int x = (int) ((((line.r - houghHeight) - ((y - centerY) * tsin)) / tcos) + centerX);
                    if (x < width && x >= 0) {
                        if (x > xMax)
                            xMax = y;
                        else if (x < xMin)
                            xMin = x;
                    }
                }
            } else {
                // Draw horizontal-sh lines
                for (int x = 0; x < width; x++) {
                    int y = (int) ((((line.r - houghHeight) - ((x - centerX) * tcos)) / tsin) + centerY);
                    if (y < height && y >= 0) {
                        if (y > yMax)
                            yMax = y;
                        else if (y < yMin)
                            yMin = y;
                    }
                }
            }

        }
        return ImageHelper.getSubImage(src, xMin, yMin, xMax - xMin, yMax - yMin);
    }
}
