package by.bsuir.misoi.passportscanner.text;

public class PassportData {

    private TextLine machineReadableLineSecond;
    private TextLine machineReadableLineFirst;
    private TextLine name;
    private TextLine surname;
    private TextLine id;
    private TextLine birthday;
    private TextLine passportNumber;

    public void setBirthday(TextLine birthday) {
        this.birthday = birthday;
    }

    public void setMachineReadableLineSecond(TextLine machineReadableLineSecond) {
        this.machineReadableLineSecond = machineReadableLineSecond;
    }


    public void setMachineReadableLineFirst(TextLine machineReadableLineFirst) {
        this.machineReadableLineFirst = machineReadableLineFirst;
    }


    public void setName(TextLine name) {
        this.name = name;
    }


    public void setSurname(TextLine surname) {
        this.surname = surname;
    }


    public void setId(TextLine id) {
        this.id = id;
    }


    public void setPassportNumber(TextLine passportNumber) {
        this.passportNumber = passportNumber;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("\n\n");

        sb.append("\n").append(name.readLine());
        sb.append("\n").append(surname.readLine());
        sb.append("\n").append(birthday.readLine());
        sb.append("\n").append(passportNumber.readLine());
        sb.append("\n").append(id.readLine());
        sb.append("\n").append(machineReadableLineFirst.readLine());
        sb.append("\n").append(machineReadableLineSecond.readLine());
        sb.append('\n');
        return sb.toString();
    }
}
