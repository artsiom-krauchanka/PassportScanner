package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Artem on 23-Sep-16.
 */
public final class GroupFinder {

    public static int[] findGroups(int width, int height, int[] sourcePixels){
        final int[] pixels = new int[sourcePixels.length];
        System.arraycopy(sourcePixels, 0, pixels, 0, sourcePixels.length);

        final Set<ColorRGB> colorRGBs = new HashSet<>();

        final int black = ColorRGB.getBlackColor();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if(pixels[y*width + x] == black){

                    int neighborColor = getNeighborColor(x, y, width, height, pixels);
                    if(neighborColor != -1){
                        pixels[y * width + x] = neighborColor;
                    }else {
                        ColorRGB randomColor;
                        do {
                            randomColor = ColorRGB.getRandomColorRGB();
                        } while (colorRGBs.contains(randomColor));
                        colorRGBs.add(randomColor);

                        pixels[y * width + x] = randomColor.getMixColor();
                    }
                }


            }
        }

        return pixels;
    }

    /**
     *
     * @param x - current x;
     * @param y - current y;
     * @param width - image width;
     * @param height - image height;
     * @param pixels - image pixels;
     * @return - return color of neighbor if find, else -1;
     */

    private static int getNeighborColor (int x, int y, int width, int height, int[] pixels){
        final int black = ColorRGB.getBlackColor();
        final int white = ColorRGB.getWhiteColor();

        int pixel = black;
        if(x > 0) {         // check left
            pixel = pixels[y * width + x - 1];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(x > 0 && y > 0) { //check left top diagonal
            pixel = pixels[(y - 1) * width + x - 1];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(x > 0 && y < height - 1) { //check left bottom diagonal
            pixel = pixels[(y + 1) * width + x - 1];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(y > 0) {         //check top
            pixel = pixels[(y - 1) * width + x];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(y < height - 1) { //check bottom
            pixel = pixels[(y + 1) * width + x];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(x < width - 1) {  // check right
            pixel = pixels[y * width + x + 1];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(x < width - 1 && y > 0) { //check right top diagonal
            pixel = pixels[(y - 1) * width + x + 1];
            if (pixel != black && pixel != white)
                return pixel;
        }

        if(x < width - 1 && y < height - 1) { //check right bottom diagonal
            pixel = pixels[(y + 1) * width + x + 1];
            if (pixel != black && pixel != white)
                return pixel;
        }

        return -1;
    }

}
