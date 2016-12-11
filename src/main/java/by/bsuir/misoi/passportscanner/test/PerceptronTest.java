package by.bsuir.misoi.passportscanner.test;


import by.bsuir.misoi.passportscanner.perceptron.Perceptron;
import by.bsuir.misoi.passportscanner.utils.ImageHelper;

public class PerceptronTest {

    public static void main(String[] args) throws Exception {
        Perceptron perceptron = new Perceptron();

        perceptron.recognize(ImageHelper.readImage("E:/img/exa/res/3/1/3-TRANSFORMED244.bmp"));
    }

}
