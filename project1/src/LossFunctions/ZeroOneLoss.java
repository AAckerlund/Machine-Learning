package LossFunctions;

public class ZeroOneLoss{
    public int[][] setOfCorrectClasses;
    public int[][] setOfLearnedClasses;
    public int errorCount = 0;
    public int total = 0;

    public ZeroOneLoss(int[][] setOfCorrectClasses, int[][] setOfLearnedClasses){
        this.setOfCorrectClasses = setOfCorrectClasses;
        this.setOfLearnedClasses = setOfLearnedClasses;
    }

    public double getPercentCorrect(int[][] setOfCorrectClasses, int[][] setOfLearnedClasses){
        for (int i = 0; i<setOfCorrectClasses.length; i++){
            if(setOfCorrectClasses[i] == setOfLearnedClasses[i]){
                errorCount += 1;
            }
        }
        double percentCorrect = errorCount/setOfCorrectClasses.length;
        return percentCorrect;
    }
}
