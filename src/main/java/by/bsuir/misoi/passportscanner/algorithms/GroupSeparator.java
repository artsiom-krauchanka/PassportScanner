package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.draw.Content;
import by.bsuir.misoi.passportscanner.draw.ContentLine;
import by.bsuir.misoi.passportscanner.draw.Pixel;
import by.bsuir.misoi.passportscanner.utils.ColorRGB;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;

public final class GroupSeparator {

    private static final double RATIO_CONSTANT = 6.8d;

    public static Content getPhotoContent(int width, int height, final int[] groupedPixels, final int groupsCount) {
        final Content content = getGroup(width, height, groupedPixels, getMaxGroup(groupedPixels, groupsCount));

        int middle = height / 2;
        double ratio = (double) (height * width) / (content.height * content.width);
        if (ratio < 6.8d || ratio > 8.2d) {
            if (isHead(content, middle)) {
                double ratio1;
                do {
                    content.width += 2;
                    content.x -= 1;
                    double realHeight = content.width * 1.34;
                    ratio1 = (double) (height * width) / (realHeight * content.width);
                } while ((ratio1 < RATIO_CONSTANT || ratio1 > 8.2));

                double realHeight = content.width * 1.34;
                content.height = (int) realHeight;

            } else {
                double realHeight = content.width * 1.34;
                int realY = (content.y + content.height) - (int) realHeight;
                content.y = realY;
                content.height = (int) realHeight;
            }
        }

        return content;
    }


    public static BufferedImage findPhoto(final BufferedImage sourceImage, final int[] groupedPixels, final int groupsCount) throws Exception {
//        final Content content = getGroup(sourceImage.getWidth(), sourceImage.getHeight(), groupedPixels, getMaxGroup(groupedPixels, groupsCount));
        final Content content = getPhotoContent(sourceImage.getWidth(), sourceImage.getHeight(), groupedPixels, groupsCount);
        return ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);
    }

    public static LinkedList<Content> exclude(Content element, LinkedList<Content> elements) {
        final Line2D topElement = new Line2D.Float(element.x, element.y, element.x + element.width, element.y);
        final Line2D leftElement = new Line2D.Float(element.x, element.y, element.x, element.y + element.height);
        final Line2D bottomElement = new Line2D.Float(element.x, element.y + element.height, element.x + element.width, element.y);
        final Line2D rightElement = new Line2D.Float(element.x + element.width, element.y + element.height, element.x + element.width, element.y + element.height);

        final LinkedList<Content> temp = new LinkedList<>();
        for (Content content : elements) {
            if (content != null) {
                final Line2D top = new Line2D.Float(content.x, content.y, content.x + content.width, content.y);
                final Line2D left = new Line2D.Float(content.x, content.y, content.x, content.y + content.height);
                final Line2D bottom = new Line2D.Float(content.x, content.y + content.height, content.x + content.width, content.y);
                final Line2D right = new Line2D.Float(content.x + content.width, content.y + content.height, content.x + content.width, content.y + content.height);
                if (topElement.intersectsLine(left) || topElement.intersectsLine(right) ||
                        bottomElement.intersectsLine(left) || bottomElement.intersectsLine(right) ||
                        leftElement.intersectsLine(top) || leftElement.intersectsLine(bottom) ||
                        rightElement.intersectsLine(top) || rightElement.intersectsLine(bottom)) {
                    temp.add(content);
                }
            }
        }
        return temp;
    }

    private static boolean isHead(Content content, int middle) {
        if (middle > content.y + content.height) {
            return true; // голова
        } else if (middle < content.y) {
            return false;  // плечи
        } else if ((middle - content.y) > (content.y + content.height) - middle) {
            return true;
        } else {
            return false;
        }
    }

    public static LinkedList<Content> getAllGroups(int width, int height, final int[] pixels, final int groupsCount) throws Exception {
        final LinkedList<Content> images = new LinkedList<>();
        for (int i = 0; i < groupsCount; i++) {
            images.add(getGroup(width, height, pixels, i));
        }
        return images;
    }

    public static LinkedList<Content> getAllGroups(int width, int height, final int[] pixels, final int groupsCount, final ArrayList<Integer> removingGroups) throws Exception {
        final LinkedList<Content> images = new LinkedList<>();
        for (int i = 0; i < groupsCount; i++) {
            if (!removingGroups.contains(i))
                images.add(getGroup(width, height, pixels, i));
        }
        return images;
    }

    public static LinkedList<ContentLine> getLines(LinkedList<Content> contents) {
        final LinkedList<ContentLine> lines = new LinkedList<>();

        for (Content content : contents) {
            if (content != null) {
                boolean isAdded = false;
                for (ContentLine line : lines) {
                    if (line.checkAndAdd(content)) {
                        isAdded = true;
                        break;
                    }
                }
                if (!isAdded) {
                    ContentLine line = new ContentLine();
                    line.add(content);
                    lines.add(line);
                }
            }
        }

        return lines;
    }

    public static ArrayList<Integer> deleteSmallGroups(final int[] pixels, int minGroupSize, final Hashtable<Integer, Integer> stats) {
        final ArrayList<Integer> removingKeys = new ArrayList<>(stats.size());

        final Iterator<Integer> iterator = stats.keySet().iterator();
        while (iterator.hasNext()) {
            Integer key = iterator.next();
            if (stats.get(key) < minGroupSize)
                removingKeys.add(key);
        }

        for (int j = 0; j < pixels.length; j++)
            if (removingKeys.contains(pixels[j]))
                pixels[j] = ColorRGB.getWhiteColor();

        return removingKeys;
    }

    public static Hashtable<Integer, Integer> getGroupStatistics(final int[] pixels, int groupsCount) {
        final Hashtable<Integer, Integer> stats = new Hashtable<>(groupsCount);

        for (int i = 1; i <= groupsCount; i++) {
            int size = 0;
            for (int j = 0; j < pixels.length; j++)
                if (pixels[j] == i)
                    size++;
            stats.put(i, size);
        }

        return stats;
    }

    public static Content getGroup(int width, int height, final int[] pixels, final int groupNumber) {
        final Pixel left = new Pixel(width, 0);
        final Pixel top = new Pixel(0, height);
        final Pixel right = new Pixel(0, 0);
        final Pixel bottom = new Pixel(0, 0);

//        int minX = width, maxX = 0, maxY = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (pixels[y * width + x] == groupNumber) {
                    left.setLeft(x);
                    right.setRight(x);

                    top.setTop(y);
                    bottom.setBottom(y);
                }
            }
        }
        final int x = left.x;
        final int y = top.y;

        final int groupWidth = right.x - x;
        final int groupHeight = bottom.y - y;


        return (x > 0 && y > 0 && groupWidth > 0 && groupHeight > 0) ?
                new Content(x, y, groupWidth, groupHeight) :
                null;
    }

    public static int getMaxGroup(final int[] pixels, final int groupsCount) {
        int[] counts = new int[groupsCount + 1];
        final int white = ColorRGB.getWhiteColor();

        for (int pixel : pixels) {
            if (pixel != white && pixel > 0 && pixel < groupsCount) {
                counts[pixel]++;
            }
        }

        int maxSize = 0;
        int index = 0;

        for (int i = 0; i < groupsCount; i++) {
            if (maxSize < counts[i]) {
                maxSize = counts[i];
                index = i;
            }
        }
        return index;
    }
}
