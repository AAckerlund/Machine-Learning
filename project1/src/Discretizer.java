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
        //System.out.println(sampleDataPoint);
        //System.out.println(bins);

        if(sampleDataPoint >= (float)(bins.get(bins.size()-1))){
            //System.out.println(bins.size()-1);
            return bins.size()-1;
        }
        
        int binNum = 1;   //first threshold

        //locates threshold of datapoint
        for(int i = 0; i<bins.size(); i++){
            if((sampleDataPoint >= (float)(bins.get(i))) && (sampleDataPoint <= (float)(bins.get(i+1)))){

                //System.out.println(binNum);
                return binNum;
            }
            binNum += 1;  //iterates through thresholds
        }
        System.out.println("ERROR");
        return 999;   //if no threshold was found; error in data
    }
}
