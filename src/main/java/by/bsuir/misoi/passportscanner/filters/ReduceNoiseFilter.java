package by.bsuir.misoi.passportscanner.filters;

public class ReduceNoiseFilter implements Filter {

    private final int FILTER_SIZE;

    public ReduceNoiseFilter() {
        FILTER_SIZE = 3;
    }

    public ReduceNoiseFilter(int filterSize) {
        this.FILTER_SIZE = filterSize;
    }

    @Override
    public int[] transform(int width, int height, int[] pixels) {
        int index = 0;
        final int[] r = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] g = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] b = new int[FILTER_SIZE * FILTER_SIZE];
        final int[] outPixels = new int[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int k = 0;
                int irgb = pixels[index];       //current pixel
                int ir = (irgb >> 16) & 0xff;
                int ig = (irgb >> 8) & 0xff;
                int ib = irgb & 0xff;

                for (int dy = -FILTER_SIZE/2; dy <= FILTER_SIZE/2; dy++) {
                    int iy = y+dy;
                    if (0 <= iy && iy < height) {
                        int ioffset = iy*width;         //offset in the pixel array (in orde rto find pixels in array)

                        for (int dx = -FILTER_SIZE/2; dx <= FILTER_SIZE/2; dx++) {
                            int ix = x+dx;
                            if (0 <= ix && ix < width) {
                                int rgb = pixels[ioffset+ix];
                                r[k] = (rgb >> 16) & 0xff;
                                g[k] = (rgb >> 8) & 0xff;
                                b[k] = rgb & 0xff;
                            } else {
                                r[k] = ir;
                                g[k] = ig;
                                b[k] = ib;
                            }
                            k++;
                        }
                    } else {
                        for (int dx = -FILTER_SIZE/2; dx <= FILTER_SIZE/2; dx++) {
                            r[k] = ir;
                            g[k] = ig;
                            b[k] = ib;
                            k++;
                        }
                    }
                }
                outPixels[index] = (pixels[index] & 0xff000000) | (smooth(r) << 16) | (smooth(g) << 8) | smooth(b);
                index++;
            }
        }
        return outPixels;
    }

    private int smooth(int[] v) {
        int minIndex = 0, maxIndex = 0, min = Integer.MAX_VALUE, max = Integer.MIN_VALUE;

        for (int i = 0; i < FILTER_SIZE * FILTER_SIZE; i++) {
            if ( i != FILTER_SIZE / 2 ) {
                if (v[i] < min) {
                    min = v[i];
                    minIndex = i;
                }
                if (v[i] > max) {
                    max = v[i];
                    maxIndex = i;
                }
            }
        }
        if ( v[FILTER_SIZE / 2] < min )
            return v[minIndex];
        if ( v[FILTER_SIZE / 2] > max )
            return v[maxIndex];
        return v[FILTER_SIZE / 2];
    }
}
