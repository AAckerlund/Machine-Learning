package NearestNeighborAlgorithms;

import java.util.ArrayList;
import java.lang.Double;
import java.util.Random;

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

    public double getNearestNeighbors(double[] datapoint, double[][] datapoints){
        for(int i = 0; i<datapoints.length; i++) {
            System.out.println(datapoints[i][0]);
        }
        ArrayList<Double> distances = new ArrayList<Double>();
        //double[] distances = {};
        for(int i = 0; i<datapoints.length; i++){
            double distance = getDistance(datapoint, datapoints[i]);
            distances.add(distance);
            //distances[i] = distance;
        }

        if(k > distances.size()){
            throw new IllegalArgumentException("k can not be bigger than size of dataset");
        }

        ArrayList<Double> kLowestDistances = new ArrayList<>();
        ArrayList<Double> classOfKLowestDistances = new ArrayList<>();
        double classOfLowestDistance = 0;

        for(int i = 0; i<k; i++) {
            System.out.println(distances);
            double lowestDistance = Double.POSITIVE_INFINITY;
            for (int j = 0; j < distances.size(); j++) {
                if (distances.get(j) < lowestDistance) {
                    lowestDistance = distances.get(j);
                    classOfLowestDistance = datapoints[j][0];
                    //classOfKLowestDistances.add(datapoints[j][0]);
                }
            }
            classOfKLowestDistances.add(classOfLowestDistance);
            kLowestDistances.add(lowestDistance);
            System.out.println(lowestDistance);
            distances.remove(lowestDistance);
        }
        for(int i = 0; i<classOfKLowestDistances.size(); i++) {
            System.out.println(classOfKLowestDistances.get(i));
        }
        //return 5;

        int highestOccurence = 0;
        ArrayList<Double> classesWithHighestOccurence = new ArrayList<>();

        for(int i = 0; i<classOfKLowestDistances.size(); i++){
            int count = 0;
            for(int j = 0; j<classOfKLowestDistances.size(); j++){
                if(Double.compare(classOfKLowestDistances.get(j), classOfKLowestDistances.get(i)) == 0){
                    count += 1;
                }
                else{
                    /*System.out.println(classOfKLowestDistances.get(j));
                    System.out.println("is not equal to");
                    System.out.println(classOfKLowestDistances.get(i));*/
                }


            }
            if(count > highestOccurence) {
                highestOccurence = count;
                classesWithHighestOccurence.add(classOfKLowestDistances.get(i));
            }

        }
        System.out.println(highestOccurence);
        for(int i = 0; i<classesWithHighestOccurence.size(); i++) {
            System.out.println(classesWithHighestOccurence);
        }
        //return kLowestDistances;
        Random rand = new Random();
        int classIndex = rand.nextInt(classesWithHighestOccurence.size());
        double chosenClass = classesWithHighestOccurence.get(classIndex);

        return chosenClass;
    }

    //pubic getClassFromNearestNeighbors(ArrayList<Double> nearestNeighbors)

}
