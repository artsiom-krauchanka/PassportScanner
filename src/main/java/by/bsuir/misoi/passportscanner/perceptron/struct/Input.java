package by.bsuir.misoi.passportscanner.perceptron.struct;


import java.io.Serializable;

public class Input implements Serializable {

    public double inputSum;
    public double output;
    public double error;
    public double[] weights;

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
