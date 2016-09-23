package by.bsuir.misoi.passportscanner.filters;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;

public class MonochromeFilter implements Filter {

    private final int threshold;

    public MonochromeFilter() {
        this.threshold = 150;
    }

    public MonochromeFilter(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public int[] transform(int width, int height, int[] pixels) {
        final int[] transform = new int[width * height];

        for (int i = 0; i < width * height; i++) {

            int red = ColorRGB.getRed(pixels[i]);
            int green = ColorRGB.getGreen(pixels[i]);
            int blue = ColorRGB.getBlue(pixels[i]);


            transform[i] = (red+green+blue)/3 < threshold ? ColorRGB.getBlackColor() : ColorRGB.getWhiteColor();

        }

        return transform;
    }


}
