package by.bsuir.misoi.passportscanner.algorithms;

import by.bsuir.misoi.passportscanner.draw.ContentLine;

import java.util.ArrayList;
import java.util.List;

public class PassportInfo {

    private static final int DELTA = 5;
    private static final int MACHINE_READABLE_LINE_LENGTH = 44;
    private final List<ContentLine> lines;
    private int letterHeight;
    private ContentLine machineReadableLineSecond;
    private ContentLine machineReadableLineFirst;
    private ContentLine name;
    private ContentLine surname;
    private ContentLine id;
    private ContentLine passportNumber;

    private PassportInfo() {
        lines = new ArrayList<>();
    }

    public PassportInfo(List<ContentLine> lines) {
        this.lines = lines;
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
        } while ((machineReadableLineSecond == null && machineReadableLineFirst == null) || (last != 0));

        letterHeight = (machineReadableLineFirst.getMedianHeight() + machineReadableLineSecond.getMedianHeight()) / 2;
        System.out.println(letterHeight);
    }

    public ContentLine getMachineReadableLineSecond() {
        return machineReadableLineSecond;
    }

    public ContentLine getMachineReadableLineFirst() {
        return machineReadableLineFirst;
    }

    public List<ContentLine> getLines() {
        List<ContentLine> info = new ArrayList<>();
        info.add(machineReadableLineFirst);
        info.add(machineReadableLineSecond);

        return info;
    }

}
