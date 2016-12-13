package by.bsuir.misoi.passportscanner.text;

public class PassportData {

    private TextLine machineReadableLineSecond;
    private TextLine machineReadableLineFirst;
    private TextLine name;
    private TextLine surname;
    private TextLine id;
    private TextLine passportNumber;

    public TextLine getMachineReadableLineSecond() {
        return machineReadableLineSecond;
    }

    public void setMachineReadableLineSecond(TextLine machineReadableLineSecond) {
        this.machineReadableLineSecond = machineReadableLineSecond;
    }

    public TextLine getMachineReadableLineFirst() {
        return machineReadableLineFirst;
    }

    public void setMachineReadableLineFirst(TextLine machineReadableLineFirst) {
        this.machineReadableLineFirst = machineReadableLineFirst;
    }

    public TextLine getName() {
        return name;
    }

    public void setName(TextLine name) {
        this.name = name;
    }

    public TextLine getSurname() {
        return surname;
    }

    public void setSurname(TextLine surname) {
        this.surname = surname;
    }

    public TextLine getId() {
        return id;
    }

    public void setId(TextLine id) {
        this.id = id;
    }

    public TextLine getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(TextLine passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("PassportData{");
        sb.append("max probability\n");
        sb.append(", machine Line First=").append(machineReadableLineFirst.readLine());
        sb.append("machine Line Second=").append(machineReadableLineSecond.readLine());
//        sb.append(", name=").append(name.readLine());
//        sb.append(", surname=").append(surname.readLine());
//        sb.append(", id=").append(id.readLine());
//        sb.append(", passportNumber=").append(passportNumber.readLine());
        sb.append("\nmin probability\n");
        sb.append(", machine Line First=").append(machineReadableLineFirst.readLine(true));
        sb.append("machine Line Second=").append(machineReadableLineSecond.readLine(true));
//        sb.append(", name=").append(name.readLine(true));
//        sb.append(", surname=").append(surname.readLine(true));
//        sb.append(", id=").append(id.readLine(true));
//        sb.append(", passportNumber=").append(passportNumber.readLine(true));
        sb.append('}');
        return sb.toString();
    }
}
