package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.draw.ContentLine;

import java.util.ArrayList;
import java.util.List;

public class PassportInfo {

    private static final int DELTA = 5;
    private static final int MACHINE_READABLE_LINE_LENGTH = 44;
    private static final int PASSPORT_NUMBER_LENGTH = 9;
    private static final int ID_AND_DATE_LENGTH = 22;
    private static final int BIRTHDAY_LENGTH = 8;
    private static final int ID_LENGTH = 14;

    private final List<ContentLine> lines;
    private final int width;
    private final int height;
    private int letterHeight;
    private int letterWidth;


    private ContentLine machineReadableLineSecond;
    private ContentLine machineReadableLineFirst;
    private ContentLine name;
    private ContentLine surname;
    private ContentLine id;
    private ContentLine birthday;
    private ContentLine passportNumber;

    private PassportInfo() {
        lines = new ArrayList<>();
        height = 0;
        width = 0;
    }

    public PassportInfo(int width, int height, List<ContentLine> lines) {
        this.lines = lines;
        this.width = width;
        this.height = height;
        init();
    }

    private void init() {
        int last = this.lines.size() - 1;

        do {
            if (lines.get(last).size() >= MACHINE_READABLE_LINE_LENGTH) {
                if (machineReadableLineSecond == null)
                    machineReadableLineSecond = lines.get(last);
                else if (machineReadableLineFirst == null)
                    machineReadableLineFirst = lines.get(last);
            }
            last--;
        } while ((machineReadableLineSecond == null || machineReadableLineFirst == null));

        letterHeight = (machineReadableLineFirst.getMedianHeight() + machineReadableLineSecond.getMedianHeight()) / 2;
        letterWidth = (machineReadableLineFirst.getMedianWidth() + machineReadableLineSecond.getMedianWidth()) / 2;

        List<ContentLine> removeContent = new ArrayList<>();
        for (int i = last; i < this.lines.size(); i++)
            removeContent.add(this.lines.get(i));

        this.lines.removeAll(removeContent);
        removeContent.clear();

        int first = 0;
        do {
            ContentLine l = this.lines.get(first);
            if (l.size() <= 9)
                continue;
            ContentLine number = l.getSubLine(l.size() - 9, 9);
            int median = number.getMedianHeight();
            if ((median <= letterHeight + 0.2 * letterHeight && median >= letterHeight - 0.2 * letterHeight) &&
                    (number.getMinX() > width * 2.1 / 3))
                passportNumber = l;
            first++;
        } while (passportNumber == null);

        passportNumber = passportNumber.getSubLine(passportNumber.size() - PASSPORT_NUMBER_LENGTH, PASSPORT_NUMBER_LENGTH);


        int second = first;
        do {
            ContentLine l = this.lines.get(second);
            int median = l.getMedianHeight();
            if ((median <= letterHeight + 0.2 * letterHeight && median >= letterHeight - 0.2 * letterHeight))
                surname = l;
            second++;
        } while (surname == null);

        int third = second;
        do {
            ContentLine l = this.lines.get(third);
            int median = l.getMedianHeight();
            if ((median <= letterHeight + 0.18 * letterHeight || median >= letterHeight - 0.18 * letterHeight))
                name = l;
            third++;
        } while (name == null);

        int fourth = third;
        ContentLine tmp = null;
        do {
            ContentLine l = this.lines.get(fourth);
            int median = l.getMedianHeight();
            if ((median <= letterHeight + 0.18 * letterHeight || median >= letterHeight - 0.18 * letterHeight) && l.size() == ID_AND_DATE_LENGTH)
                tmp = l;
            fourth++;
        } while (tmp == null);

        birthday = tmp.getCopySubLine(0, BIRTHDAY_LENGTH);
        id = tmp.getCopySubLine(BIRTHDAY_LENGTH, ID_LENGTH);

    }

    public ContentLine getMachineReadableLineSecond() {
        return machineReadableLineSecond;
    }

    public ContentLine getMachineReadableLineFirst() {
        return machineReadableLineFirst;
    }

    public ContentLine getName() {
        return name;
    }

    public ContentLine getSurname() {
        return surname;
    }

    public ContentLine getId() {
        return id;
    }

    public ContentLine getPassportNumber() {
        return passportNumber;
    }

    public ContentLine getBirthday() {
        return birthday;
    }

    public List<ContentLine> getLines() {
        List<ContentLine> info = new ArrayList<>();
        info.add(passportNumber);
        info.add(surname);
        info.add(name);
        info.add(birthday);
        info.add(id);
        info.add(machineReadableLineFirst);
        info.add(machineReadableLineSecond);
        return info;
    }

}
