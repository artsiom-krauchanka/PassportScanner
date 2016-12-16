package by.bsuir.misoi.passportscanner.text;


public class Letter {

    private static String $_ = "_";
    private static String $  = "<";

    public Letter(String maxProbability, String minProbability) {
        this.maxProbability = maxProbability.equals($_) ? $ : maxProbability;
        this.minProbability = minProbability.equals($_) ? $ : minProbability;
    }

    public String maxProbability;
    public String minProbability;

}
