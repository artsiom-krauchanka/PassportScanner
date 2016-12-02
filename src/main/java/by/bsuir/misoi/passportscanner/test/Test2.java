package by.bsuir.misoi.passportscanner.test;


import by.bsuir.misoi.passportscanner.algorithms.PassportScanner;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;

public class Test2 {

    final static String fileName = "9.jpg";

    final static String inPath = "E:/img/examples/";
    final static String outPath = "E:/img/exa/";

    public static void main(String[] args) throws Throwable{
        BufferedImage sourceImage = ImageHelper.readImage(inPath + fileName);

        ImageHelper.saveImage(PassportScanner.extractPassportFromImage(sourceImage), outPath + "extractPassportFromImage.jpg");

    }

}
