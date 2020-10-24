package LossFunctions;

public class CrossEntropy {
    public double calculateLoss(double predictedValue, double trueValue){
        double loss;
        if(trueValue == 1){
            loss = -Math.log(predictedValue);
        }
        else{
            loss = -Math.log(1 - predictedValue);
        }
        return loss;
    }
}
