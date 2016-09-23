package by.bsuir.misoi.passportscanner.filters;

public class MedianFilter implements Filter {

    private final int FILTER_SIZE;

    public MedianFilter() {
        FILTER_SIZE = 3;
    }

    public MedianFilter(int filterSize) {
        this.FILTER_SIZE = filterSize;
    }

    @Override
    public int[] transform(int width, int height, int[] pixels) {
        int index = 0;
        final int[] argb = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] r = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] g = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] b = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] outPixels = new int[width * height + 10];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int k = 0;

                for (int dy = -FILTER_SIZE/2; dy <= FILTER_SIZE/2; dy++) {
                    int iy = y+dy;

                    if (0 <= iy && iy < height) {
                        int ioffset = iy*width;

                        for (int dx = -FILTER_SIZE/2; dx <= FILTER_SIZE/2; dx++) {
                            int ix = x+dx;
                            if (0 <= ix && ix < width) {
                                int rgb = pixels[ioffset+ix];
                                argb[k] = rgb;
                                r[k] = (rgb >> 16) & 0xff;
                                g[k] = (rgb >> 8) & 0xff;
                                b[k] = rgb & 0xff;
                                k++;
                            }
                        }
                    }
                }
                while (k < FILTER_SIZE * FILTER_SIZE) {
                    argb[k] = 0xff000000;
                    r[k] = g[k] = b[k] = 0;
                    k++;
                }
                outPixels[index++] = argb[rgbMedian(r, g, b)];
            }
        }
        return outPixels;
    }

    private int rgbMedian(int[] r, int[] g, int[] b) {
        int sum, index = 0, min = Integer.MAX_VALUE;

        for (int i = 0; i < FILTER_SIZE * FILTER_SIZE; i++) {
            sum = 0;
            for (int j = 0; j < FILTER_SIZE * FILTER_SIZE; j++) {
                sum += Math.abs(r[i]-r[j]);
                sum += Math.abs(g[i]-g[j]);
                sum += Math.abs(b[i]-b[j]);
            }
            if (sum < min) {
                min = sum;
                index = i;
            }
        }
        return index;
    }
}
