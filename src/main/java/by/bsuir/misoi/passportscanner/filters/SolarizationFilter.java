package by.bsuir.misoi.passportscanner.filters;


import by.bsuir.misoi.passportscanner.utils.ColorRGB;

public class SolarizationFilter implements Filter {

    private static final int THRESHOLD_DEFAULT = 127;
    private final int threshold;

    public SolarizationFilter() {
        this.threshold = THRESHOLD_DEFAULT;
    }

    public SolarizationFilter(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public int[] transform(int width, int height, int[] pixels) {
        final int[] result = new int[width * height];
        int red, green, blue, pixel;

        for (int i = 0; i < pixels.length; i++) {
            pixel = pixels[i];
            red = ColorRGB.getRed(pixel);
            if (red <= threshold) {
                red = 255 - red;
            }

            green = ColorRGB.getGreen(pixel);
            if (green <= threshold) {
                green = 255 - green;
            }

            blue = ColorRGB.getBlue(pixel);
            if (blue <= threshold) {
                blue = 255 - blue;
            }
            result[i] = ColorRGB.getMixColor(red, green, blue);
        }
        return result;
    }
}
