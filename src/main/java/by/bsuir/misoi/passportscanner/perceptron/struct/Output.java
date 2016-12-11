package by.bsuir.misoi.passportscanner.perceptron.struct;


import java.io.Serializable;

public class Output implements Serializable, Comparable<String> {
    public double InputSum;
    public double output;
    public double Error;
    public double Target;
    public String Value;

    @Override
    public int compareTo(String o) {
        return Value.compareTo(o);
    }
}
