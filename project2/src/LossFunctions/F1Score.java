package LossFunctions;

public class F1Score {
    private final double precision, recall;

    public F1Score(double precision, double recall){
        this.precision = precision;
        this.recall = recall;
    }

    //uses instances of Precision and Recall to calculate F1 score using equation given in class
    public double getF1Score(){
        return(2*((precision*recall)/(precision+recall)));
    }
}
