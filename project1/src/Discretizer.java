import java.util.ArrayList;

public class Discretizer {
    public int numOfThresholds;

    public Discretizer(int numOfThresholds) {
        this.numOfThresholds = numOfThresholds;
    }

    public ArrayList getSizeOfThreshold(double sampleDataPoint, double min, double max) {
        double sizeOfThreshold = (max - min) / (double)this.numOfThresholds;
        ArrayList thresholds = new ArrayList();
        double currentThreshold = min;
        thresholds.add(min);

        for(int i = 0; i < this.numOfThresholds - 1; ++i) {
            currentThreshold += sizeOfThreshold;
            thresholds.add(currentThreshold);
        }

        return thresholds;
    }
}
