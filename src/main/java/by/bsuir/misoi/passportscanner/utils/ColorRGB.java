package by.bsuir.misoi.passportscanner.utils;

import java.util.Random;


public class ColorRGB {

    private int red;
    private int green;
    private int blue;


    public ColorRGB(int red, int green, int blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public int getMixColor(){
        return red<<16|green<<8|blue;

    }
    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public static int getRed(int color) {
        return (color & 0x00ff0000)  >> 16;
    }

    public static int getGreen(int color) {
        return	(color & 0x0000ff00)  >> 8;
    }

    public static int getBlue(int color) {
        return  color & 0x000000ff;

    }

    public static int getMixColor(int red, int green, int blue) {
        return red<<16|green<<8|blue;
    }

    public static int getBlackColor() {
        return getMixColor(0, 0, 0);
    }

    public static int getWhiteColor() {
        return getMixColor(255, 255, 255);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColorRGB colorRGB = (ColorRGB) o;

        if (red != colorRGB.red) return false;
        if (green != colorRGB.green) return false;
        return blue == colorRGB.blue;

    }

    @Override
    public int hashCode() {
        int result = red;
        result = 31 * result + green;
        result = 31 * result + blue;
        return result;
    }

    private final static Random random = new Random();
    public static ColorRGB getRandomColorRGB(){
        return new ColorRGB(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

}
