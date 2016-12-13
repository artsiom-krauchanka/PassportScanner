package by.bsuir.misoi.passportscanner.perceptron.struct;


import java.io.Serializable;

public class Hidden implements Serializable {

    private double inputSum;
    private double output;
    private double error;
    private double[] weights;

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

    public double[] getWeights() {
        return weights;
    }

    public void setWeights(double[] weights) {
        this.weights = weights;
    }
}
