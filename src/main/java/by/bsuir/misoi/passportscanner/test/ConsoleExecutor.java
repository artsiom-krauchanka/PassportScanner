package by.bsuir.misoi.passportscanner.test;

import by.bsuir.misoi.passportscanner.algorithms.GroupFinder;
import by.bsuir.misoi.passportscanner.algorithms.GroupSeparator;
import by.bsuir.misoi.passportscanner.draw.Content;
import by.bsuir.misoi.passportscanner.draw.ContentLine;
import by.bsuir.misoi.passportscanner.draw.Rectangle;
import by.bsuir.misoi.passportscanner.filters.BinaryFilter;
import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.utils.ColorRGB;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;


public class ConsoleExecutor {

    public static final int SMALL_GROUP_LIMIT = 30;


    final static LinkedList<Filter> filters = new LinkedList<>();

    static {
        filters.add(new MedianFilter());
        filters.add(new BinaryFilter());
    }

    final static String fileName = "1.jpg";

    final static String inPath = "E:/img/exa/";
    final static String outPath = "E:/img/exa/res/";

    public static void main(String[] args) throws Throwable {
        for( int i = 1; i <= 3; i++) {
            File folder = new File(outPath + i + "/");
            if (folder.exists())
                FileUtils.deleteDirectory(folder);
            folder.mkdir();

            BufferedImage sourceImage = ImageHelper.readImage(inPath + i + ".jpg");
            sourceImage = sourceImage.getSubimage(0, sourceImage.getHeight() / 2, sourceImage.getWidth(), sourceImage.getHeight() / 2);
            ImageHelper.saveImage(sourceImage, outPath + i + ".jpg");


            final int width = sourceImage.getWidth();
            final int height = sourceImage.getHeight();

            int[] pixels = ImageHelper.getPixels(sourceImage);

            for (Filter filter : filters) {
                pixels = filter.transform(width, height, pixels); //применяем 2 фильтра
            }
            ImageHelper.saveImage(ImageHelper.getImageFromPixels(pixels, width, height, BufferedImage.TYPE_BYTE_BINARY), outPath + i + ".jpg");

            final GroupFinder demoFinder = new GroupFinder(width, height, pixels);
            int[] demoPixels = demoFinder.findGroups();
            ImageHelper.saveImage(ImageHelper.getImageFromPixels(demoPixels, width, height, BufferedImage.TYPE_INT_RGB), outPath + i + ".jpg");


            final GroupFinder finder = new GroupFinder(width, height, pixels);

            int count = finder.fastFindGroups(); //вернет количество найденных групп
            System.out.println(count);

            final Hashtable<Integer, Integer> stats = GroupSeparator.getGroupStatistics(finder.getPixels(), count);
            final ArrayList<Integer> removingGroups = GroupSeparator.deleteSmallGroups(finder.getPixels(), SMALL_GROUP_LIMIT, stats);

            removingGroups.add(GroupSeparator.getMaxGroup(finder.getPixels(), count));

            System.out.println(count - removingGroups.size());

            LinkedList<Content> contents = GroupSeparator.getAllGroups(width, height, finder.getPixels(), count, removingGroups);

            LinkedList<ContentLine> lines = GroupSeparator.getLines(contents);
            System.out.println(lines.size());

            for (int j = 0; j < lines.size(); j++) {
                File lineFolder = new File(outPath + i + "/" + j + "/");
                lineFolder.mkdir();

                for (Content content : lines.get(j).getLine()) {
                    BufferedImage result = ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);
                    ImageHelper.saveImage(result, lineFolder.getPath() + "/" + i + ".jpg");
                }
            }

//            for (int j = 1; j <= count; j++) {
//                if (!removingGroups.contains(j)) {
//                    Content content = GroupSeparator.getGroup(width, height, finder.getPixels(), j);
//                    if (content != null) {
//                        BufferedImage result = ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);
//                        ImageHelper.saveImage(result, outPath + i + "/" + i + ".jpg");
//                    }
//                }
//            }


            System.out.println();
        }
    }




}
