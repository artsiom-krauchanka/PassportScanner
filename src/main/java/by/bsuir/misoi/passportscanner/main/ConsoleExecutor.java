package by.bsuir.misoi.passportscanner.main;

import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.filters.MonochromeFilter;
import by.bsuir.misoi.passportscanner.filters.ReduceNoiseFilter;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by Artem on 20-Sep-16.
 */
public class ConsoleExecutor {


//    private static final MedianFilter medianFilter = new MedianFilter();
//    private static final ReduceNoiseFilter reduceNoiseFilter = new ReduceNoiseFilter();
//    private static final MonochromeFilter monochromeFilter = new MonochromeFilter();

    private static final LinkedList<Filter> filters = new LinkedList<>();
    private static void init(){
        filters.add(new ReduceNoiseFilter());
        filters.add(new MedianFilter());
        filters.add(new MonochromeFilter());
    }

    public static void main(String[] args)throws Throwable{
        init();
        final BufferedImage image =  ImageIO.read(new File("E:/img/3.jpg"));

        final int width = image.getWidth();
        final int height = image.getHeight();

        int[] pixels = ImageHelper.getPixels(image);

        for(Filter filter : filters)
            pixels = filter.transform(width, height, pixels);


        BufferedImage resultImage = ImageHelper.getImageFromPixels(pixels, width, height, BufferedImage.TYPE_BYTE_GRAY);
        ImageHelper.saveImage(resultImage, "E:/img/res/4.jpg");

    }










}
