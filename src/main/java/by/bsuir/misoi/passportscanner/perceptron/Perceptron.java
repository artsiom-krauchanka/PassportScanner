package by.bsuir.misoi.passportscanner.perceptron;

import by.bsuir.misoi.passportscanner.text.Letter;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;
import org.apache.commons.io.FilenameUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

public class Perceptron {

    private final static File TRANING_DIR = new File("E:/img/x/");
    //Neural Network Object With output Type String
    private NeuralNetwork neuralNetwork;
    //Data Members Required For Neural Network
    private Dictionary<String, double[]> trainingSet;
    private int avImageHeight = 0;
    private int avImageWidth = 0;
    private int numOfPatterns = 0;
    private int inputNum = 47;
    private int hiddenNum = 15;

    public Perceptron() throws Exception {
        File[] files = TRANING_DIR.listFiles();
        initializeSettings(files);
        generateTrainingSet(files);
        createNeuralNetwork();
    }

    private void initializeSettings(File[] files) {

        try {
            numOfPatterns = files.length;
            avImageHeight = 0;
            avImageWidth = 0;

            for (File f : files) {
                BufferedImage temp = ImageHelper.readImage(f);
                avImageHeight += temp.getHeight();
                avImageWidth += temp.getWidth();
            }
            avImageHeight /= numOfPatterns;
            avImageWidth /= numOfPatterns;

            int networkInput = avImageHeight * avImageWidth;

            inputNum = ((int) ((double) (networkInput + numOfPatterns) * 0.33));
            hiddenNum = ((int) ((double) (networkInput + numOfPatterns) * 0.11));

        } catch (Exception ex) {
            throw new RuntimeException("error Initializing Settings: " + ex.getMessage());
        }
    }

    private void generateTrainingSet(File[] Patterns) throws IOException {
        trainingSet = new Hashtable<>(Patterns.length);

        for (File f : Patterns) {
            BufferedImage temp = ImageHelper.readImage(f);
            trainingSet.put(FilenameUtils.removeExtension(FilenameUtils.getName(f.getPath())), ImageProcessing.toMatrix(temp, avImageHeight, avImageWidth));
        }
    }

    private void createNeuralNetwork() throws Exception {
        if (trainingSet == null) {
            throw new Exception("Unable to Create Neural Network As There is No Data to train..");
        }

        neuralNetwork = new NeuralNetwork(new Layer(avImageHeight * avImageWidth, inputNum, hiddenNum, numOfPatterns), trainingSet);
        neuralNetwork.setMaximumError(0.02);
        neuralNetwork.train();
    }


    public Letter recognize(BufferedImage image) {
        double[] input = ImageProcessing.toMatrix(image, avImageHeight, avImageWidth);
        return neuralNetwork.recognize(input, "?", "?");
    }
}
