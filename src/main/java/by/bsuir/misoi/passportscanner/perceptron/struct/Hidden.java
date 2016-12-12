package by.bsuir.misoi.passportscanner.perceptron.struct;


import java.io.Serializable;

public class Hidden implements Serializable {
    public double InputSum;
    public double Output;
    public double Error;
    public double[] Weights;
}
