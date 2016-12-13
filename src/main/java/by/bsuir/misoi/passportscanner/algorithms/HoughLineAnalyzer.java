package by.bsuir.misoi.passportscanner.algorithms;


import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class HoughLineAnalyzer {


    public static double getAngle(final Vector<HoughLine> houghLines, double deltaAngel){
        final HoughLine[] lines = houghLines.toArray(new HoughLine[houghLines.size()]);
        for (int i = 0; i < lines.length - 1; i++){
            for (int j = i + 1; j < lines.length; j++) {
                double degree1 = Math.toDegrees(lines[i].theta);
                double degree2 = Math.toDegrees(lines[j].theta);
                double delta = Math.abs(degree1 - degree2);
                if (delta <  90 + deltaAngel && delta > 90 - deltaAngel)
                    return lines[i].theta;
            }
        }
        return 0.0;
    }



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
