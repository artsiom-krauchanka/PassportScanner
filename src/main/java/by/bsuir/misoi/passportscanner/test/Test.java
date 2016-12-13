package by.bsuir.misoi.passportscanner.test;

import by.bsuir.misoi.passportscanner.algorithms.HoughLine;
import by.bsuir.misoi.passportscanner.algorithms.HoughLineAnalyzer;
import by.bsuir.misoi.passportscanner.algorithms.HoughTransform;
import by.bsuir.misoi.passportscanner.filters.CannyDetectorFilter;
import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.Vector;

/**
 * Created by Artem on 23-Sep-16.
 */
public class Test {

    final static LinkedList<Filter> filters = new LinkedList<>();

    static {
        filters.add(new CannyDetectorFilter());
    }


    final static String fileName = "11.jpg";

    final static String inPath = "E:/img/examples/";
    final static String outPath = "E:/img/res/";


    public static void main(String[] args) throws Exception {
        BufferedImage sourceImage = ImageHelper.readImage(inPath + fileName);

        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();

        int[] pixels = ImageHelper.getPixels(sourceImage);

        for (Filter filter : filters) {
            pixels = filter.transform(width, height, pixels);
        }

        BufferedImage image = ImageHelper.getImageFromPixels(pixels, width, height,BufferedImage.TYPE_INT_RGB);
        ImageHelper.saveImage(image, outPath + "copy.jpg");

        HoughTransform hough = new HoughTransform(image);

        // get the lines out
        Vector<HoughLine> lines = hough.getLines(350);

        // draw the lines back onto the image
        for (int j = 0; j < lines.size(); j++) {
            HoughLine line = lines.elementAt(j);
            line.draw(image, Color.RED.getRGB());
        }

        ImageHelper.saveImage(image, outPath + "out.jpg");

        ImageHelper.saveImage(HoughLineAnalyzer.getPassportFromList(sourceImage, lines), outPath + "getPassportFromList.jpg");
    }


}
