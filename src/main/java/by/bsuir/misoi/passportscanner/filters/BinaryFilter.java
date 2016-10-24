package by.bsuir.misoi.passportscanner.filters;

public class BinaryFilter implements Filter {

    private int width;
    private int height;

    public BinaryFilter() {
    }

    @Override
    public int[] transform(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        int[] grayScale = toGray(pixels);
        return binarize(grayScale);
    }

    private int otsuTreshold(int[] original) {
        int[] histogram = imageHistogram(original);
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

    private int[] toGray(int[] pixels) {
        int[] lum = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            int p = pixels[i];
            int a = (p & 0xff0000) >> 24;
            int r = (p & 0xff0000) >> 16;
            int g = (p & 0xff00) >> 8;
            int b = p & 0xff;

            r = (int) (0.21 * r + 0.71 * g + 0.07 * b);
            int newPixel = colorToRGB(a, r, r, r);
            lum[i] = newPixel;
        }
        return lum;
    }

    private int[] binarize(int[] pixels) {
        int threshold = otsuTreshold(pixels);
        int[] binarized = new int[width * height];

        for (int i = 0; i < width * height; i++) {
            int newPixel;
            int p = pixels[i];
            int red = (p & 0xff0000) >> 16;
            int alpha = (p & 0xff0000) >> 24;

            if (red > threshold) {
                newPixel = 255;
            } else {
                newPixel = 0;
            }
            newPixel = colorToRGB(alpha, newPixel, newPixel, newPixel);
            binarized[i] = newPixel;
        }
        return binarized;
    }

    private int colorToRGB(int alpha, int red, int green, int blue) {
        int newPixel = 0;
        newPixel += alpha;
        newPixel = newPixel << 8;
        newPixel += red;
        newPixel = newPixel << 8;
        newPixel += green;
        newPixel = newPixel << 8;
        newPixel += blue;
        return newPixel;
    }

    private int[] imageHistogram(int[] pixels) {
        int[] histogram = new int[256];

        for (int i = 0; i < histogram.length; i++) {
            histogram[i] = 0;
        }

        for (int i = 0; i < width * height; i++) {
            int p = pixels[i];
            int red = (p & 0xff0000) >> 16;
            histogram[red]++;
        }
        return histogram;
    }
}
