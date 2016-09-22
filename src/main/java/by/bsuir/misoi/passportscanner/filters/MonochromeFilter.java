package by.bsuir.misoi.passportscanner.filters;

public class MonochromeFilter implements Filter {

    private final int multiplier;

    public MonochromeFilter() {
        this.multiplier = 0xFFFFF;
    }

    public MonochromeFilter(int multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public int[] transform(int width, int height, int[] pixels) {
        final int[] transform = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            transform[i] = pixels[i] & multiplier;
        }
        return transform;
    }
}
