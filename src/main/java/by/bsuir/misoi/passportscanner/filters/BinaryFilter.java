package by.bsuir.misoi.passportscanner.filters;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;

public class BinaryFilter implements Filter {


    @Override
    public int[] transform(int width, int height, final int[] pixels) {
        int[] grayScale = toGray(width, height, pixels);

        int threshold = otsuTreshold(width, height, grayScale);

        int[] binarized = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            int newPixel = ColorRGB.getBlue(grayScale[i]) > threshold ? 255 : 0;
            binarized[i] = ColorRGB.getMixColor(newPixel, newPixel, newPixel);
        }

        return binarized;
    }

    private int[] toGray(int width, int height, final int[] pixels) {
        int[] gray = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            gray[i] = ColorRGB.getGray(pixels[i]);
        }

        return gray;
    }


    private int otsuTreshold(int width, int height, final int[] original) {
        int[] histogram = imageHistogram(width, height, original);
        int total = width * height;
        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int i = 0; i < 256; i++) {
            wB += histogram[i];
            if (wB == 0) continue;
            wF = total - wB;

            if (wF == 0) break;

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }

        return threshold;
    }

    private int[] imageHistogram(int width, int height, final int[] pixels) {
        int[] histogram = new int[256];

        for (int i = 0; i < width * height; i++) {
            histogram[ColorRGB.getRed(pixels[i])]++;
        }

        return histogram;
    }
}
