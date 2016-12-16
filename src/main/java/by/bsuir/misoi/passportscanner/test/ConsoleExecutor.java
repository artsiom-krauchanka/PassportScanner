package by.bsuir.misoi.passportscanner.test;

import by.bsuir.misoi.passportscanner.algorithms.ContentAnalyzer;
import by.bsuir.misoi.passportscanner.algorithms.PassportAnalyzer;
import by.bsuir.misoi.passportscanner.algorithms.PassportInfo;
import by.bsuir.misoi.passportscanner.draw.Content;
import by.bsuir.misoi.passportscanner.draw.ContentLine;
import by.bsuir.misoi.passportscanner.filters.BinaryFilter;
import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.filters.MedianFilter;
import by.bsuir.misoi.passportscanner.perceptron.Perceptron;
import by.bsuir.misoi.passportscanner.text.PassportData;
import by.bsuir.misoi.passportscanner.text.TextLine;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import org.apache.commons.io.FileUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.List;


public class ConsoleExecutor {



    final static LinkedList<Filter> filters = new LinkedList<>();

    static {
        filters.add(new MedianFilter());
        filters.add(new BinaryFilter());
    }

    final static String inPath = "E:/img/exa/";
    final static String outPath = "E:/img/exa/res/";

    public static void main(String[] args) throws Throwable {
        Perceptron perceptron = new Perceptron();
        for( int i = 1; i <= 2; i++) {
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

            sourceImage = ImageHelper.getImageFromPixels(pixels, width, height, BufferedImage.TYPE_BYTE_BINARY);
            ImageHelper.saveImage(sourceImage, outPath + i + ".jpg");

            final List<ContentLine> lines = ContentAnalyzer.getContentLines(width, height, pixels);

            PassportAnalyzer analyzer = new PassportAnalyzer(sourceImage, perceptron, lines);

//            for (int j = 0; j < lines.size(); j++) {
//                File lineFolder = new File(outPath + i + "/" + j + "/");
//                lineFolder.mkdir();
//
//                List<Content> line = lines.get(j).getLine();
//                System.out.println("Line size: " + line.size());
//                for (Content content : line) {
//                    BufferedImage result = ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);
//
//
//                    ImageHelper.saveImage(result, lineFolder.getPath() + "/start" + i + ".bmp");
//                }
//            }

//            PassportInfo info = new PassportInfo(width, height, lines);
//            for (int j = 0; j < info.getLines().size(); j++) {
//                File lineFolder = new File(outPath + i + "/" + j + "/");
//                lineFolder.mkdir();
//                TextLine line = new TextLine();
//                int lineNum = 0;
//                for (Content content : info.getLines().get(j).getLine()) {
////                    System.out.println(lineNum++);
//                    BufferedImage result = ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);
//                    ImageHelper.saveImage(result, lineFolder.getPath() + "/" + i + ".bmp");
//                }
//                System.out.println("---------------------------------------");
//            }







            System.out.println();
        }

    }
    private static void printLine (List<Content> line, BufferedImage sourceImage, File lineFolder) throws Exception {
        for (Content content : line) {
            BufferedImage result = ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);


            ImageHelper.saveImage(result, lineFolder.getPath() + "/testPrint.bmp");
        }
    }




}
