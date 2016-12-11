package by.bsuir.misoi.passportscanner.perceptron;


import by.bsuir.misoi.passportscanner.utils.ColorRGB;

import java.awt.image.BufferedImage;

public class ImageProcessing {

    //Convert RGB To Matrix [Of Double]
    public static double[] ToMatrix(BufferedImage BM, int MatrixRowNumber, int MatrixColumnNumber)
    {
        double HRate = (MatrixRowNumber / BM.getHeight());
        double WRate = (MatrixColumnNumber / BM.getWidth());
        double[] Result = new double[MatrixColumnNumber * MatrixRowNumber];

        for (int r = 0; r < MatrixRowNumber; r++)
        {
            for (int c = 0; c < MatrixColumnNumber; c++)
            {
                int color = BM.getRGB((int)(c / WRate), (int)(r / HRate));
                Result[r * MatrixColumnNumber + c] = 1 - (ColorRGB.getRed(color) * .3 +
                        ColorRGB.getGreen(color) * .59 +
                        ColorRGB.getBlue(color) * .11) / 255;
            }
        }
        return Result;
    }

}
