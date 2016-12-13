package by.bsuir.misoi.passportscanner.perceptron.struct;


public class NeuralEventArgs {

    public static final boolean STOP = false;
    private final double currentError = 0;
    private final int currentIteration = 0;

    public int getCurrentIteration() {
        return currentIteration;
    }

    public double getCurrentError() {
        return currentError;
    }
}
