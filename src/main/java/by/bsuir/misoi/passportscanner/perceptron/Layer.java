package by.bsuir.misoi.passportscanner.perceptron;

import by.bsuir.misoi.passportscanner.perceptron.struct.*;
import by.bsuir.misoi.passportscanner.text.Letter;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Random;

public class Layer {

    private int preInputNum;
    private int inputNum;
    private int hiddenNum;
    private int outputNum;

    private PreInput[] preInputLayer;
    private Input[] inputLayer;
    private Hidden[] hiddenLayer;
    private Output[] outputLayer;
    private double learningRate = 0.2;

    public Layer(int preInputNum, int inputNum, int hiddenNum, int outputNum) {
        this.preInputNum = preInputNum;
        this.inputNum = inputNum;
        this.hiddenNum = hiddenNum;
        this.outputNum = outputNum;

        initLayers();
    }

    private void initLayers() {
        preInputLayer = new PreInput[preInputNum];
        inputLayer = new Input[inputNum];
        hiddenLayer = new Hidden[hiddenNum];
        outputLayer = new Output[outputNum];

        for (int i = 0; i < preInputNum; i++) {
            preInputLayer[i] = new PreInput();
        }
        for (int i = 0; i < inputNum; i++) {
            inputLayer[i] = new Input();
        }
        for (int i = 0; i < hiddenNum; i++) {
            hiddenLayer[i] = new Hidden();
        }
        for (int i = 0; i < outputNum; i++) {
            outputLayer[i] = new Output();
        }
    }


    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public void backPropagate() {
        int i, j;
        double total;

        //Fix Hidden Layer's error
        for (i = 0; i < hiddenNum; i++) {
            total = 0.0;
            for (j = 0; j < outputNum; j++) {
                total += hiddenLayer[i].getWeights()[j] * outputLayer[j].getError();
            }
            hiddenLayer[i].setError(total);
        }

        //Fix Input Layer's error
        for (i = 0; i < inputNum; i++) {
            total = 0.0;
            for (j = 0; j < hiddenNum; j++) {
                total += inputLayer[i].weights[j] * hiddenLayer[j].getError();
            }
            inputLayer[i].setError(total);
        }

        //Update The First Layer's weights
        for (i = 0; i < inputNum; i++) {
            for (j = 0; j < preInputNum; j++) {
                double[] weights = preInputLayer[j].getWeights();
                weights[i] += learningRate * inputLayer[i].getError() * preInputLayer[j].getValue();
                preInputLayer[j].setWeights(weights);
            }
        }

        //Update The Second Layer's weights
        for (i = 0; i < hiddenNum; i++) {
            for (j = 0; j < inputNum; j++) {
                inputLayer[j].weights[i] += learningRate * hiddenLayer[i].getError() * inputLayer[j].output;
            }
        }

        //Update The Third Layer's weights
        for (i = 0; i < outputNum; i++) {
            for (j = 0; j < hiddenNum; j++) {
                hiddenLayer[j].getWeights()[i] += learningRate * outputLayer[i].getError() * hiddenLayer[j].getOutput();
            }
        }
    }

    public double f(double x) {
        return (1 / (1 + Math.exp(-x)));
    }

    public void forwardPropagate(double[] pattern, String output) {
        int i, j;
        double total;

        //Apply input to the network
        for (i = 0; i < preInputNum; i++) {
            preInputLayer[i].setValue(pattern[i]);
        }

        //Calculate The First(Input) Layer's Inputs and Outputs
        for (i = 0; i < inputNum; i++) {
            total = 0.0;
            for (j = 0; j < preInputNum; j++) {
                total += preInputLayer[j].getValue() * preInputLayer[j].getWeights()[i];
            }
            inputLayer[i].inputSum = total;
            inputLayer[i].output = f(total);
        }

        //Calculate The Second(Hidden) Layer's Inputs and Outputs
        for (i = 0; i < hiddenNum; i++) {
            total = 0.0;
            for (j = 0; j < inputNum; j++) {
                total += inputLayer[j].output * inputLayer[j].weights[i];
            }

            hiddenLayer[i].setInputSum(total);
            hiddenLayer[i].setOutput(f(total));
        }

        //Calculate The Third(output) Layer's Inputs, Outputs, Targets and errors
        for (i = 0; i < outputNum; i++) {
            total = 0;
            for (j = 0; j < hiddenNum; j++) {
                total += hiddenLayer[j].getOutput() * hiddenLayer[j].getWeights()[i];
            }

            outputLayer[i].setInputSum(total);
            outputLayer[i].setOutput(f(total));
            outputLayer[i].setTarget(outputLayer[i].getValue().compareTo(output) == 0 ? 1 : 0);
            outputLayer[i].setError((outputLayer[i].getTarget() - outputLayer[i].getOutput()) * (outputLayer[i].getOutput()) * (1 - outputLayer[i].getOutput()));
        }
    }

    public double getError() {
        double total = 0.0;
        for (int j = 0; j < outputNum; j++) {
            total += Math.pow((outputLayer[j].getTarget() - outputLayer[j].getOutput()), 2) / 2;
        }
        return total;
    }

    public void initializeNetwork(Dictionary<String, double[]> trainingSet) {
        int i, j;
        Random rand = new Random();
        for (i = 0; i < preInputNum; i++) {
            preInputLayer[i].setWeights(new double[inputNum]);
            for (j = 0; j < inputNum; j++) {
                double[] weights = preInputLayer[i].getWeights();
                weights[j] = 0.01 + ((double) rand.nextInt(8) / 100);
                preInputLayer[i].setWeights(weights);
            }
        }

        for (i = 0; i < inputNum; i++) {
            inputLayer[i].weights = new double[hiddenNum];
            for (j = 0; j < hiddenNum; j++) {
                inputLayer[i].weights[j] = 0.01 + ((double) rand.nextInt(8) / 100);
            }
        }

        for (i = 0; i < hiddenNum; i++) {
            hiddenLayer[i].setWeights(new double[outputNum]);
            for (j = 0; j < outputNum; j++) {
                hiddenLayer[i].getWeights()[j] = 0.01 + ((double) rand.nextInt(8) / 100);
            }
        }

        int k = 0;

        Enumeration<String> enumeration = trainingSet.keys();
        while (enumeration.hasMoreElements()) {
            outputLayer[k++].setValue(enumeration.nextElement());
        }
    }

    public Letter recognize(double[] input, String matchedHigh, String matchedLow) {
        int i, j;
        double total = 0.0;
        double max = -1;

        //Apply input to the network
        for (i = 0; i < preInputNum; i++) {
            preInputLayer[i].setValue(input[i]);
        }

        //Calculate input Layer's Inputs and Outputs
        for (i = 0; i < inputNum; i++) {
            total = 0.0;
            for (j = 0; j < preInputNum; j++) {
                total += preInputLayer[j].getValue() * preInputLayer[j].getWeights()[i];
            }
            inputLayer[i].inputSum = total;
            inputLayer[i].output = f(total);
        }

        //Calculate Hidden Layer's Inputs and Outputs
        for (i = 0; i < hiddenNum; i++) {
            total = 0.0;
            for (j = 0; j < inputNum; j++) {
                total += inputLayer[j].output * inputLayer[j].weights[i];
            }

            hiddenLayer[i].setInputSum(total);
            hiddenLayer[i].setOutput(f(total));
        }

        //Find the [Two] Highest Outputs
        for (i = 0; i < outputNum; i++) {
            total = 0.0;
            for (j = 0; j < hiddenNum; j++) {
                total += hiddenLayer[j].getOutput() * hiddenLayer[j].getWeights()[i];
            }
            outputLayer[i].setInputSum(total);
            outputLayer[i].setOutput(f(total));
            if (outputLayer[i].getOutput() > max) {
                matchedLow = matchedHigh;
                max = outputLayer[i].getOutput();
                matchedHigh = outputLayer[i].getValue();
            }
        }
//        System.out.println("Max :" + matchedHigh);
//        System.out.println("Min :" + matchedLow + "\n");
        return new Letter(matchedHigh, matchedLow);
    }
}
