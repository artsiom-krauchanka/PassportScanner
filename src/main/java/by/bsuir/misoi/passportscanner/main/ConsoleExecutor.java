package by.bsuir.misoi.passportscanner.main;

import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.filters.MonochromeFilter;
import by.bsuir.misoi.passportscanner.filters.ReduceNoiseFilter;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import com.sun.imageio.plugins.jpeg.JPEGImageWriter;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Artem on 20-Sep-16.
 */
public class ConsoleExecutor {


    private static final MedianFilter medianFilter = new MedianFilter();
    private static final ReduceNoiseFilter reduceNoiseFilter = new ReduceNoiseFilter();
    private static final MonochromeFilter monochromeFilter = new MonochromeFilter();


    public static void main(String[] args)throws Throwable{
        final BufferedImage image =  ImageIO.read(new File("E:/img/2.jpg"));

        final int width = image.getWidth();
        final int height = image.getHeight();

        final int[] pixels = ImageHelper.getPixels(image);

        final int[] monochromeFilterPixels = monochromeFilter.transform(width, height, pixels);
        final int[] medianFilterPixels = medianFilter.transform(width, height, monochromeFilterPixels);



        BufferedImage resultImage = ImageHelper.getImageFromPixels(medianFilterPixels, width, height, BufferedImage.TYPE_BYTE_GRAY);
        ImageHelper.saveImage(resultImage, "E:/img/res/2.jpg");

    }










}
