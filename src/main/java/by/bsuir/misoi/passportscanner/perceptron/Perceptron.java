package by.bsuir.misoi.passportscanner.perceptron;


import by.bsuir.misoi.passportscanner.filters.Filter;
import by.bsuir.misoi.passportscanner.perceptron.struct.NeuralEventArgs;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import org.apache.commons.io.FilenameUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

import static java.lang.System.in;

public class Perceptron {

    //Neural Network Object With Output Type String
    private NeuralNetwork neuralNetwork = null;

    //Data Members Required For Neural Network
    private Dictionary<String, double[]> TrainingSet = null;
    private int av_ImageHeight = 0;
    private int av_ImageWidth = 0;
    private int NumOfPatterns = 0;

    private final static File traningDir = new File("E:/img/x/");


    public Perceptron() throws Exception
    {
        File[] files = traningDir.listFiles();
        InitializeSettings(files);

        GenerateTrainingSet(files);
        CreateNeuralNetwork();
    }

    private void InitializeSettings (File[] files)
    {

        try
        {
            NumOfPatterns = files.length;

            av_ImageHeight = 0;
            av_ImageWidth = 0;

            for (File f : files)
            {
                BufferedImage Temp = ImageHelper.readImage(f);
                av_ImageHeight += Temp.getHeight();
                av_ImageWidth += Temp.getWidth();
            }
            av_ImageHeight /= NumOfPatterns;
            av_ImageWidth /= NumOfPatterns;

            int networkInput = av_ImageHeight * av_ImageWidth;

        }
        catch (Exception ex)
        {
            throw new RuntimeException("Error Initializing Settings: " + ex.getMessage());
        }
    }

    private void GenerateTrainingSet(File[] Patterns) throws IOException {

        TrainingSet = new Hashtable<>(Patterns.length);

        for (File f : Patterns)
        {
            BufferedImage Temp = ImageHelper.readImage(f);

            TrainingSet.put(FilenameUtils.getName(f.getPath()),
                    ImageProcessing.ToMatrix(Temp, av_ImageHeight, av_ImageWidth));
        }

    }

    private void CreateNeuralNetwork() throws Exception {
        if (TrainingSet == null)
            throw new Exception("Unable to Create Neural Network As There is No Data to Train..");

        int InputNum = 47;
        int HiddenNum = 15;

        neuralNetwork = new NeuralNetwork
                    (new Layer(av_ImageHeight * av_ImageWidth, InputNum, HiddenNum, NumOfPatterns), TrainingSet);


        neuralNetwork.setMaximumError(0.08);
        neuralNetwork.Train();
    }


    public void recognize(BufferedImage image)
    {
        String MatchedHigh = "?", MatchedLow = "?";
        double OutputValueHight = 0, OutputValueLow = 0;

        double[] input = ImageProcessing.ToMatrix(image,
                av_ImageHeight, av_ImageWidth);

        neuralNetwork.Recognize(input, MatchedHigh, OutputValueHight,
                MatchedLow, OutputValueLow);

    }

}
