package by.bsuir.misoi.passportscanner.perceptron;

import by.bsuir.misoi.passportscanner.text.Letter;
import by.bsuir.misoi.passportscanner.perceptron.struct.NeuralEventArgs;

import java.util.Dictionary;
import java.util.Enumeration;

public class NeuralNetwork {

    private Dictionary<String, double[]> trainingSet;
    private Layer neuralNet;
    private double maximumError = 1.0;
    private int maximumIteration = 1000000;

    public NeuralNetwork(Layer iBackPro, Dictionary<String, double[]> trainingSet) {
        neuralNet = iBackPro;
        this.trainingSet = trainingSet;
        neuralNet.initializeNetwork(this.trainingSet);
    }

    public double getMaximumError() {
        return maximumError;
    }

    public void setMaximumError(double maximumError) {
        this.maximumError = maximumError;
    }

    public int getMaximumIteration() {
        return maximumIteration;
    }

    public void setMaximumIteration(int maximumIteration) {
        this.maximumIteration = maximumIteration;
    }

    public boolean train() {
        double currentError = 0;
        int currentIteration = 0;
        final NeuralEventArgs args = new NeuralEventArgs();

        do {
            currentError = 0;
            Enumeration<String> enumeration = trainingSet.keys();
            while (enumeration.hasMoreElements()) {
                String key = enumeration.nextElement();
                neuralNet.forwardPropagate(trainingSet.get(key), key);
                neuralNet.backPropagate();
                currentError += neuralNet.getError();
            }

            currentIteration++;

        } while (currentError > maximumError && currentIteration < maximumIteration && !NeuralEventArgs.STOP);

        return !(currentIteration >= maximumIteration || NeuralEventArgs.STOP);

    }

    public Letter recognize(double[] input, String matchedHigh, String matchedLow) {
        return neuralNet.recognize(input, matchedHigh, matchedLow);
    }

//    public void SaveNetwork(String path)
//    {
//        FileStream FS = new FileStream(path, FileMode.Create);
//        BinaryFormatter BF = new BinaryFormatter();
//        BF.Serialize(FS, neuralNet);
//        FS.Close();
//    }
//
//    public void LoadNetwork(string path)
//    {
//        FileStream FS = new FileStream(path, FileMode.Open);
//        BinaryFormatter BF = new BinaryFormatter();
//        neuralNet = (IBackPropagation<T>)BF.Deserialize(FS);
//        FS.Close();
//    }
}
