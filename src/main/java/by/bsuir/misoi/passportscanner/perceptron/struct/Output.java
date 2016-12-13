package by.bsuir.misoi.passportscanner.perceptron.struct;


import java.io.Serializable;

public class Output implements Serializable, Comparable<String> {

    private double inputSum;
    private double output;
    private double error;
    private double target;
    private String value;

    public double getInputSum() {
        return inputSum;
    }

    public void setInputSum(double inputSum) {
        this.inputSum = inputSum;
    }

    public double getOutput() {
        return output;
    }

    public void setOutput(double output) {
        this.output = output;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    public double getTarget() {
        return target;
    }

    public void setTarget(double target) {
        this.target = target;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(String o) {
        return value.compareTo(o);
    }
}
