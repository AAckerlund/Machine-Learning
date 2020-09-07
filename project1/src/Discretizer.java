import java.util.ArrayList;

public class Discretizer {
    public int numOfBins;

    public Discretizer(int numOfBins) {
        this.numOfBins = numOfBins;
    }

    public int getBin(float sampleDataPoint, float min, float max) {
        float sizeOfBin = (max - min) / numOfBins;
        ArrayList bins = new ArrayList();
        float currentBin = min;
        bins.add(min);

        for(int i = 0; i < this.numOfBins; i++) {
            currentBin += sizeOfBin;
            bins.add(currentBin);
        }

        if(sampleDataPoint >= (float)(bins.get(bins.size()-1))){
            return bins.size()-1;
        }
        
        int binNum = 1;   //first bin

        //locates threshold of datapoint
        for(int i = 0; i<bins.size(); i++){
            if((sampleDataPoint >= (float)(bins.get(i))) && (sampleDataPoint <= (float)(bins.get(i+1)))){
                return binNum;
            }
            binNum += 1;  //iterates through bins
        }
        System.out.println("ERROR");
        return 999;   //if no bin was found; error in data
    }
}
