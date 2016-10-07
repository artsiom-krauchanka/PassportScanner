package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.utils.ColorRGB;

import java.util.HashSet;
import java.util.Set;

/**
 * increase -xss parameter when you have StackOverFlowError
 */
public final class GroupFinder {

    public GroupFinder(int width, int height, int[] sourcePixels){
        this.width = width;
        this.height = height;
        this.pixels = new int[sourcePixels.length];
        System.arraycopy(sourcePixels, 0, pixels, 0, sourcePixels.length);
    }

    private final int[] pixels;
    private final int width;
    private final int height;

    /**
     * means a group number in the range [1..0x7fffffff]
     */
    private int groupCount;
    private ColorRGB currentColor;
    private boolean isFast;

    private  void setNeighborColor (int x, int y){
        final int black = ColorRGB.getBlackColor();

        if(isFast)
            pixels[y*width + x] = groupCount;
        else
            pixels[y*width + x] = currentColor.getMixColor();

        int pixel;

        if(x < width - 1) {  // check right
            pixel = pixels[y * width + x + 1];
            if(pixel == black)
                setNeighborColor(x+1, y);
        }

        if(x < width - 1 && y < height - 1) { //check right bottom diagonal
            pixel = pixels[(y + 1) * width + x + 1];
            if(pixel == black)
                setNeighborColor(x+1, y+1);

        }

        if(y < height - 1) { //check bottom
            pixel = pixels[(y + 1) * width + x];
            if(pixel == black)
                setNeighborColor(x, y+1);

        }

        if(x > 0 && y < height - 1) { //check left bottom diagonal
            pixel = pixels[(y + 1) * width + x - 1];
            if(pixel == black)
                setNeighborColor(x-1, y+1);

        }

        if(x > 0) {         // check left
            pixel = pixels[y * width + x - 1];
            if(pixel == black)
                setNeighborColor(x-1, y);

        }

        if(x > 0 && y > 0) { //check left top diagonal
            pixel = pixels[(y - 1) * width + x - 1];
            if(pixel == black)
                setNeighborColor(x-1, y-1);

        }

        if(y > 0) {         //check top
            pixel = pixels[(y - 1) * width + x];
            if(pixel == black)
                setNeighborColor(x, y-1);

        }

        if(x < width - 1 && y > 0) { //check right top diagonal
            pixel = pixels[(y - 1) * width + x + 1];
            if(pixel == black)
                setNeighborColor(x+1, y-1);

        }
    }


    /**
     *
     * @return max count of found groups
     */
    public int fastFindGroups(){
        this.groupCount = 1;
        this.isFast = true;
        final int black = ColorRGB.getBlackColor();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if(pixels[y*width + x] == black){
                    setNeighborColor(x, y);
                    groupCount++;
                }

            }
        }

        return groupCount;
    }

    /**
     *
     * @return color copy pixels with separate groups
     */
    public int[] findGroups(){
        final Set<ColorRGB> colorRGBs = new HashSet<>();
        this.groupCount = 1;
        this.isFast = false;
        final int black = ColorRGB.getBlackColor();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                if(pixels[y*width + x] == black){

                    do {
                        currentColor = ColorRGB.getRandomColorRGB();
                    } while (colorRGBs.contains(currentColor));
                    colorRGBs.add(currentColor);

                    setNeighborColor(x, y);
                    groupCount++;
                }

            }
        }

        return pixels;
    }

    public int[] getPixels(){
        return this.pixels;
    }

    public int getGroupCount(){
        return this.groupCount;
    }

}
