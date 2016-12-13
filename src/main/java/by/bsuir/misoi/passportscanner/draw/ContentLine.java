package by.bsuir.misoi.passportscanner.draw;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContentLine {

    private List<Content> line = new ArrayList<>();

    private int maxY;
    private int minY;

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
        int lineWidth = line.get(line.size() - 1).x + line.get(line.size() - 1).width - line.get(0).x;
        int realWidth = 0;
        for (Content c : line)
            realWidth += c.width;

        int space = (lineWidth - realWidth) / line.size();

        for (Content c : line) {
            c.x -= space;
            c.y -= space;
            c.height += 2 * space;
            c.width += 2 * space;
        }

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

}
