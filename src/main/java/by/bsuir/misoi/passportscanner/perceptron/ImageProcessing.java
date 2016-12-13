package by.bsuir.misoi.passportscanner.perceptron;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;

import java.awt.image.BufferedImage;

public class ImageProcessing {

    //Convert RGB To Matrix [Of Double]
    public static double[] toMatrix(BufferedImage bufferedImage, int matrixRowNumber, int matrixColumnNumber) {
        double hRate = ((double) matrixRowNumber / bufferedImage.getHeight());
        double wRate = ((double) matrixColumnNumber / bufferedImage.getWidth());
        double[] result = new double[matrixColumnNumber * matrixRowNumber];

        for (int row = 0; row < matrixRowNumber; row++) {
            for (int col = 0; col < matrixColumnNumber; col++) {

                int color = bufferedImage.getRGB((int) (col / wRate), (int) (row / hRate));
                result[row * matrixColumnNumber + col] = 1 - (ColorRGB.getRed(color) * 0.3 + ColorRGB.getGreen(color) * 0.59 + ColorRGB.getBlue(color) * 0.11) / 255;
            }
        }
        return result;
    }
}
