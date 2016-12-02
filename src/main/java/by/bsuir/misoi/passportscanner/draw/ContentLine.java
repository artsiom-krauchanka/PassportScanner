package by.bsuir.misoi.passportscanner.draw;


import java.util.LinkedList;

public class ContentLine {

    private LinkedList<Content> line = new LinkedList<>();

    private int maxY;
    private int minY;

    public boolean checkAndAdd(Content content) {
        if (check(content)) {
            add(content);
            return true;
        }

        return false;
    }

    public boolean check(Content content) {
        return (content.y < maxY && content.y >= minY) ||
                (content.y + content.height < maxY && content.y + content.height >= minY);
    }

    public void add(Content content) {
        line.add(content);

        if (minY > content.y)
            minY = content.y;


        if (maxY < content.y + content.height)
            maxY = content.y + content.height;
    }

    public LinkedList<Content> getLine() {
        return this.line;
    }

}
