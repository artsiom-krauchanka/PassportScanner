package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.draw.Content;
import by.bsuir.misoi.passportscanner.draw.ContentLine;
import by.bsuir.misoi.passportscanner.perceptron.Perceptron;
import by.bsuir.misoi.passportscanner.text.PassportData;
import by.bsuir.misoi.passportscanner.text.TextLine;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class PassportAnalyzer {

    final static String outPath = "E:/img/exa/res/";

    private static int id;
    private final BufferedImage sourceImage;
    private final Perceptron perceptron;
    private final List<ContentLine> lines;

    public PassportAnalyzer(BufferedImage sourceImage, Perceptron perceptron, final List<ContentLine> lines) throws Exception {
        this.perceptron = perceptron;
        this.sourceImage = sourceImage;
        this.lines = lines;
        id++;
        analyze();
    }

    private PassportInfo info;
    private PassportData data;

    private void analyze() throws Exception {
        info = new PassportInfo(lines);
        data = new PassportData();

        data.setMachineReadableLineSecond(transform(info.getMachineReadableLineSecond(), "machine second"));
        data.setMachineReadableLineFirst(transform(info.getMachineReadableLineFirst(), "machine first"));


        System.out.println(data.toString());
    }

    private TextLine transform(ContentLine contentLine, String lineName) throws Exception{
        File lineFolder = new File(outPath + id + "/" + lineName + "/");
        lineFolder.mkdir();
        TextLine line = new TextLine();
        for (Content content : contentLine.getLine()) {
            BufferedImage result = ImageHelper.getSubImage(sourceImage, content.x, content.y, content.width, content.height);

            line.add(perceptron.recognize(result));

            ImageHelper.saveImage(result, lineFolder.getPath() + "/" + id + ".bmp");
        }
        return line;
    }


}
