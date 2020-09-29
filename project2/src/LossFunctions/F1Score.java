package LossFunctions;

public class F1Score {
    private double precision, recall;

    public F1Score(double precision, double recall){
        this.precision = precision;
        this.recall = recall;
    }

    //uses instances of Precision and Recall to calculate F1 score using equation given in class
    public double getF1Score(){
        double f1Score = 2*((precision*recall)/(precision+recall));
        return(f1Score);
    }

    //getters and setters
    public double getPrecision()
    {
        return precision;
    }
    
    public void setPrecision(double precision)
    {
        this.precision = precision;
    }
    
    public double getRecall()
    {
        return recall;
    }
    
    public void setRecall(double recall)
    {
        this.recall = recall;
    }
}
