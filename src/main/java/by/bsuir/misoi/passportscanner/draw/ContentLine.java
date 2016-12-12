package by.bsuir.misoi.passportscanner.draw;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ContentLine {

    private static final Comparator<Content> contentComparator = (o1, o2) -> Integer.compare(o1.x, o2.x);

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
        Collections.sort(line, contentComparator);
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
