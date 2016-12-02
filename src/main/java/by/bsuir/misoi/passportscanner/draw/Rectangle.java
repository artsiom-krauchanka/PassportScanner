package by.bsuir.misoi.passportscanner.draw;


import java.awt.image.BufferedImage;

public class Rectangle {

    /**
     * @param image - image in which the drawing will take place
     * @param points - size = 4; {x, y, width, height} in <br>!!!!PERCENT!!!!<br>
     * @param color - border color
     * @return - image
     */
    public static BufferedImage draw (BufferedImage image, int[] points, int color) {
        if (points.length != 4)
            return image;

        final int x = image.getWidth() * points[0] / 100;
        final int y = image.getHeight() * points[1] / 100;

        final int width = image.getWidth() * points[2] / 100;
        final int height = image.getHeight() * points[3] / 100;


        // draw horizontal lines
        for (int i = x; i < x + width; i++) {
            image.setRGB(i, y, color);
            image.setRGB(i, y + height, color);
        }

        // draw vertical lines
        for (int i = y; i < y + height; i++) {
            image.setRGB(x, i, color);
            image.setRGB(x + width, i, color);
        }

        return image;
    }

}
