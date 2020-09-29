package NearestNeighborAlgorithms;

public class KNearestNeighbor {
    String type;
    int k;
    double[][] datapoints;
    double[] datapoint;

    public KNearestNeighbor(String type, int k, double[][] datapoints, double[] datapoint){
        this.type = type;
        this.k = k;
        this.datapoints = datapoints;
        this.datapoint = datapoint;
    }

    //finds distance between two data points from attribute values
    public double getDistance(double[] dp1, double[] dp2){
        double distance;
        double sum = 0;

        for(int i = 1; i<dp1.length; i++){
            double s =  ((dp1[i] - dp2[i]) * (dp1[i] - dp2[i]));
            sum += s;
        }
        distance = java.lang.Math.sqrt(sum);
        return distance;
    }

}
