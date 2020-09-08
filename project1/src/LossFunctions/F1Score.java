package LossFunctions;

public class F1Score {
    public double precision;
    public double recall;

    public F1Score(double precision, double recall){
        this.precision = precision;
        this.recall = recall;
    }

    public double getF1Score(){
        double f1Score = 2*((precision*recall)/(precision+recall));
        return(f1Score);
    }
}
