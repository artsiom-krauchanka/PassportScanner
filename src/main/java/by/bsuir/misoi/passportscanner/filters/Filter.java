package by.bsuir.misoi.passportscanner.filters;

public interface Filter {
    int[] transform(int width, int height, int[] pixels);
}
