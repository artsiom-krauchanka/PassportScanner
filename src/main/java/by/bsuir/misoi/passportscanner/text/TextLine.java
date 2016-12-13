package by.bsuir.misoi.passportscanner.text;

import java.util.LinkedList;
import java.util.List;

public class TextLine {

    private final List<Letter> line = new LinkedList<>();

    public TextLine () {}

    public void add(Letter letter) {
        this.line.add(letter);
    }

    public String readLine(boolean isMinProbability) {
        final StringBuilder buffer = new StringBuilder();

        for (Letter letter : line)
            buffer.append(isMinProbability ? letter.minProbability : letter.maxProbability);

        buffer.append('\n');
        return buffer.toString();
    }

    public String readLine() {
        return readLine(false);
    }

}
