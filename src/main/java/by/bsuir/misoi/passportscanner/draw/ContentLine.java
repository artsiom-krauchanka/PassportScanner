package by.bsuir.misoi.passportscanner.draw;


import by.bsuir.misoi.passportscanner.utils.ImageHelper;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ContentLine {

    private List<Content> line;

    private int maxY;
    private int minY;

    public ContentLine() {
        this.line = new ArrayList<>();
    }

    public ContentLine(List<Content> line) {
        Collections.sort(line, (o1, o2) -> Integer.compare(o1.x, o2.x));
        this.line = line;
    }

    public Content get (int index) {
        return this.line.get(index);
    }

    public boolean checkAndAdd (Content content) {
        if (check(content)) {
            add(content);
            return true;
        }

        return false;
    }

    private boolean check (Content content) {
        return (content.y < maxY && content.y >= minY) ||
                (content.y + content.height < maxY && content.y + content.height >= minY);
    }

    public void add (Content content) {
        line.add(content);

        if (minY > content.y)
            minY = content.y;


        if (maxY < content.y + content.height)
            maxY = content.y + content.height;
    }

    public List<Content> getLine () {
        Collections.sort(line, (o1, o2) -> Integer.compare(o1.x, o2.x));
        checkInvalid();
        for (int i = 0; i < line.size(); i++) {
            if (i == 0) { //first left
                Content c = line.get(i);
                Content cRight = line.get(i + 1);
                int space = cRight.x - (c.x + c.width);
                space = (space > c.width) ? (c.width / 4) : space / 2;
                c.x -= space;
                c.y -= space;
                c.width += 2 * space;
                c.height += 2 * space;
            } else if (i == line.size() - 1) { //last right
                Content c = line.get(i);
                Content cLeft = line.get(i - 1);
                int space = c.x - (cLeft.x + cLeft.width);
                space = (space > c.width) ? (c.width / 4) : space / 2;
                c.x -= space;
                c.y -= space;
                c.width += 2 * space;
                c.height += 2 * space;
            } else {
                Content c = line.get(i);
                Content cPlus1 = line.get(i + 1);
                Content cMinus1 = line.get(i - 1);

                int spaceRight = cPlus1.x - (c.x + c.width);
                int spaceLeft = c.x - (cMinus1.x + cMinus1.width);


                int space = (spaceRight < spaceLeft) ? spaceLeft : spaceRight;
                space = (space > c.width) ? (c.width / 4) : space / 2;

                c.x -= space;
                c.y -= space;
                c.width += 2 * space;
                c.height += 2 * space;
            }
        }

//        int lineWidth = line.get(line.size() - 1).x + line.get(line.size() - 1).width - line.get(0).x;
//        int realWidth = 0;
//        for (Content c : line)
//            realWidth += c.width;
//
//        int space = (lineWidth - realWidth) / line.size();
//
//        for (Content c : line) {
//            c.x -= space;
//            c.y -= space;
//            c.height += 2 * space;
//            c.width += 2 * space;
//        }

        return line;
    }

    public int size () {
        return this.line.size();
    }

    public int getMedianHeight() {
        int sum = 0;
        for (Content c : line)
            sum += c.height;
        return sum / line.size();
    }

    public int getMedianWidth() {
        int sum = 0;
        for (Content c : line)
            sum += c.width;
        return sum / line.size();
    }


    public ContentLine getSubLine(int start, int lenght) {
        Collections.sort(line, (o1, o2) -> Integer.compare(o1.x, o2.x));
        int invalid  = checkInvalid();
        return new ContentLine(line.subList(start - invalid, start + lenght - invalid));
    }

    public ContentLine getCopySubLine(int start, int lenght) {
        Collections.sort(line, (o1, o2) -> Integer.compare(o1.x, o2.x));
        int invalid  = checkInvalid();
        List<Content> list = new ArrayList<>(lenght - invalid);
        for (int i = start - invalid; i < start + lenght - invalid; i++) {
            Content c = line.get(i);
            list.add(new Content(c.x, c.y, c.width, c.height));
        }
        return new ContentLine(list);
    }

    private int checkInvalid() {
        List<Content> invalid = new ArrayList<>();
        for (Content c : line) {
            if (c.height < 5 || c.width < 5 || c.width > c.height)
                invalid.add(c);
        }
        line.removeAll(invalid);
        return invalid.size();
    }


    public int getMinX() {
        Collections.sort(line, (o1, o2) -> Integer.compare(o1.x, o2.x));
        return line.get(0).x;
    }


}
