package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.draw.Content;
import by.bsuir.misoi.passportscanner.draw.ContentLine;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ContentAnalyzer {

    private static final int SMALL_GROUP_LIMIT = 30;
    private static final int SPAM_LINE_CONTENT = 5;

    public static List<ContentLine> getContentLines(int width, int height, int[] pixels) throws Exception {
        final GroupFinder finder = new GroupFinder(width, height, pixels);

        final int count = finder.fastFindGroups(); //вернет количество найденных групп

        final Map<Integer, Integer> stats = GroupSeparator.getGroupStatistics(finder.getPixels(), count);
        final List<Integer> removingGroups = GroupSeparator.deleteSmallGroups(finder.getPixels(), SMALL_GROUP_LIMIT, stats);

        final List<Content> contents = GroupSeparator.getAllGroups(width, height, finder.getPixels(), count, removingGroups);
        final Content photoContent = GroupSeparator.getPhotoContent(width, height, finder.getPixels(), count);

        final List<Content> photoSegments = GroupSeparator.exclude(photoContent, contents);


        for (Content content : photoSegments) {
            if (contents.contains(content)) {
                contents.remove(content);
            }
        }

        final List<ContentLine> lines = GroupSeparator.getLines(contents);
        final List<ContentLine> removingItems = new LinkedList<>();

        for (ContentLine line : lines) {
            if (line.size() < SPAM_LINE_CONTENT) {
                removingItems.add(line);
            }
        }
        lines.removeAll(removingItems);
        return lines;
    }
}
