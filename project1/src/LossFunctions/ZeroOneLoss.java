package LossFunctions;

public class ZeroOneLoss{
    public int errorCount;
    public int total;

    public double getPercentCorrect(){
        double percentCorrect = (double)(total-errorCount)/total;
        return percentCorrect;
    }
}