package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.filters.CannyDetectorFilter;
import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.utils.Constants;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Vector;


public class PassportScanner {

    final static LinkedList<Filter> filters = new LinkedList<>();

    static {
        filters.add(new MedianFilter());
        filters.add(new CannyDetectorFilter());
    }


    final static String outPath = "E:/img/res/";

    public static BufferedImage extractPassportFromImage(BufferedImage sourceImage){
        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();

        int[] pixels = ImageHelper.getPixels(sourceImage);
        for (Filter filter : filters) {
            pixels = filter.transform(width, height, pixels);
        }

        BufferedImage image = ImageHelper.getImageFromPixels(pixels, width, height,BufferedImage.TYPE_INT_RGB);

        HoughTransform hough = new HoughTransform(image);
        Vector<HoughLine> lines = hough.getLines(Constants.HOUGH_LOCAL_MAXIMUM);

        double angle = HoughLineAnalyzer.getAngle(lines, Constants.PERPENDICULAR_INFELICITY);

        sourceImage = ImageHelper.rotate(sourceImage, angle);

        pixels = ImageHelper.getPixels(sourceImage);
        for (Filter filter : filters) {
            pixels = filter.transform(width, height, pixels);
        }

        image = ImageHelper.getImageFromPixels(pixels, width, height,BufferedImage.TYPE_INT_RGB);

        hough.setImage(image);
        lines = hough.getLines(Constants.HOUGH_LOCAL_MAXIMUM);

        image = HoughLineAnalyzer.getPassportFromList(sourceImage, lines);
        return image;
    }


}
