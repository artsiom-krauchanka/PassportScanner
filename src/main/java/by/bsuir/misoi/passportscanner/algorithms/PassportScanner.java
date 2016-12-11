package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.draw.Content;
import by.bsuir.misoi.passportscanner.filters.BinaryFilter;
import by.bsuir.misoi.passportscanner.filters.CannyDetectorFilter;
import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.utils.Constants;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Vector;


public class PassportScanner {

    final static LinkedList<Filter> extractFilters = new LinkedList<>();
    final static LinkedList<Filter> filters = new LinkedList<>();

    static {
        filters.add(new MedianFilter());
        filters.add(new BinaryFilter());
        extractFilters.add(new MedianFilter());
        extractFilters.add(new CannyDetectorFilter());
    }


    public static BufferedImage extractPassportFromImage(BufferedImage sourceImage) throws Exception {
        final int width = sourceImage.getWidth();
        final int height = sourceImage.getHeight();

        int[] pixels = ImageHelper.getPixels(sourceImage);
        for (Filter filter : extractFilters) {
            pixels = filter.transform(width, height, pixels);
        }

        BufferedImage image = ImageHelper.getImageFromPixels(pixels, width, height,BufferedImage.TYPE_INT_RGB);

        HoughTransform hough = new HoughTransform(image);
        Vector<HoughLine> lines = hough.getLines(Constants.HOUGH_LOCAL_MAXIMUM);

        double angle = HoughLineAnalyzer.getAngle(lines, Constants.PERPENDICULAR_INFELICITY);

        sourceImage = ImageHelper.rotate(sourceImage, angle);

        pixels = ImageHelper.getPixels(sourceImage);
        for (Filter filter : extractFilters) {
            pixels = filter.transform(width, height, pixels);
        }

        image = ImageHelper.getImageFromPixels(pixels, width, height,BufferedImage.TYPE_INT_RGB);

        hough.setImage(image);
        lines = hough.getLines(Constants.HOUGH_LOCAL_MAXIMUM);

        image = HoughLineAnalyzer.getPassportFromList(sourceImage, lines);

        if(image.getWidth() > image.getHeight())
            image = ImageHelper.rotateCw(image);


        if(getPhotoSection(image).equals(ContentPosition.TOP))
            image = ImageHelper.rotate(image, Math.toRadians(180));

        return image;
    }


    public static ContentPosition getPhotoSection(BufferedImage image) throws Exception {
        final int width = image.getWidth();
        final int height = image.getHeight();

        final int y0 = (int)(0.5 * height);

        int[] pixels = ImageHelper.getPixels(image);

        for (Filter filter : filters)
            pixels = filter.transform(width, height, pixels);


        final GroupFinder finder = new GroupFinder(width, height, pixels);
        int count = finder.fastFindGroups();
        Content content = GroupSeparator.getPhotoContent(width, height, finder.getPixels(), count);

        if (content != null) {
            if (content.y < y0)  //top
                return ContentPosition.TOP;
            else if (content.y > y0)  // bottom
                return ContentPosition.BOTTOM;
        }

        return ContentPosition.NOT_FOUND;
    }

    public enum ContentPosition {
        TOP, BOTTOM, NOT_FOUND
    }
}
