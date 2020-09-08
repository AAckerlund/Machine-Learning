package LossFunctions;

public class Recall {
    public int truePositives;
    public int falseNegatives;
    public double recall;

    public double findRecall(){
        recall = (double) truePositives / (truePositives + falseNegatives);
        return recall;
    }
}
