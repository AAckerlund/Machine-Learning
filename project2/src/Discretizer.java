import java.util.ArrayList;

public class Discretizer {
    // Converts a float to an integer representing a bin given a min and max float value

    public int numOfBins;

    public Discretizer(int numOfBins) {
        this.numOfBins = numOfBins;
    }

    //places a quantitative datapoint and puts it in a bin
    public int getBin(float sampleDataPoint, float min, float max) {
        float sizeOfBin = (max - min) / numOfBins;      //determines size of bins by max and min of dataset, evenly divided
        ArrayList<Float> bins = new ArrayList<>();
        float currentBin = min;
        bins.add(min);  //sets first bin

        for(int i = 0; i < this.numOfBins; i++) {
            currentBin += sizeOfBin;
            bins.add(currentBin);
        }

        if(sampleDataPoint >= (bins.get(bins.size()-1))){
            return bins.size()-1;       //use case of max datapoint
        }
        
        int binNum = 1;//first bin

        //locates threshold of datapoint
        for(int i = 0; i<bins.size(); i++){
            if((sampleDataPoint >= bins.get(i)) && (sampleDataPoint <= bins.get(i+1))){
                return binNum;
            }
            binNum += 1;  //iterates through bins
        }
        System.out.println("ERROR");
        return 999;   //if no bin was found; error in data
    }
}
