import java.util.ArrayList;

public class Discretizer {
    public int numOfThresholds;

    public Discretizer(int numOfThresholds) {
        this.numOfThresholds = numOfThresholds;
    }

    public int getSizeOfThreshold(double sampleDataPoint, double min, double max) {
        double sizeOfThreshold = (max - min) / numOfThresholds;
        ArrayList thresholds = new ArrayList();
        double currentThreshold = min;
        thresholds.add(min);

        for(int i = 0; i < this.numOfThresholds; i++) {
            currentThreshold += sizeOfThreshold;
            thresholds.add(currentThreshold);
        }
        System.out.println(thresholds);
        
        int thresholdNum = 1;

        for(int i = 0; i<thresholds.size(); i++){
            if((sampleDataPoint >= (double)(thresholds.get(i))) && (sampleDataPoint <= (double)(thresholds.get(i+1)))){
                return thresholdNum;
            }
            thresholdNum += 1;
        }
        return 0;
    }
}
