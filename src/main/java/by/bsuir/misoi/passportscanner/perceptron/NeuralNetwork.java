package by.bsuir.misoi.passportscanner.perceptron;


import by.bsuir.misoi.passportscanner.perceptron.struct.NeuralEventArgs;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Objects;

public class NeuralNetwork {
    private Layer NeuralNet;
    private double maximumError = 1.0;
    private int maximumIteration = 10000;
    Dictionary<String, double[]> TrainingSet;

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

//    public  abstract void IterationChangedCallBack(Object o, NeuralEventArgs args);

    public NeuralNetwork(Layer IBackPro, Dictionary<String, double[]> trainingSet)
    {
        NeuralNet = IBackPro;
        TrainingSet = trainingSet;
        NeuralNet.InitializeNetwork(TrainingSet);
    }

    public boolean Train()
    {
        double currentError = 0;
        int currentIteration = 0;
        NeuralEventArgs Args = new NeuralEventArgs() ;

        do
        {
            currentError = 0;
            Enumeration<String> enumeration = TrainingSet.keys();
            while (enumeration.hasMoreElements())
            {
                String key = enumeration.nextElement();
                NeuralNet.ForwardPropagate(TrainingSet.get(key), key);
                NeuralNet.BackPropagate();
                currentError += NeuralNet.GetError();
            }

            currentIteration++;


        } while (currentError > maximumError && currentIteration < maximumIteration && !Args.Stop);



        if (currentIteration >= maximumIteration || Args.Stop)
            return false;//Training Not Successful

        return true;
    }

    public void Recognize(double[] Input, String MatchedHigh, double OutputValueHight,
                          String MatchedLow, double OutputValueLow)
    {
        NeuralNet.Recognize(Input, MatchedHigh, OutputValueHight, MatchedLow, OutputValueLow);
    }

//    public void SaveNetwork(String path)
//    {
//        FileStream FS = new FileStream(path, FileMode.Create);
//        BinaryFormatter BF = new BinaryFormatter();
//        BF.Serialize(FS, NeuralNet);
//        FS.Close();
//    }
//
//    public void LoadNetwork(string path)
//    {
//        FileStream FS = new FileStream(path, FileMode.Open);
//        BinaryFormatter BF = new BinaryFormatter();
//        NeuralNet = (IBackPropagation<T>)BF.Deserialize(FS);
//        FS.Close();
//    }



}
