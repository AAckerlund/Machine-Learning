import java.util.ArrayList;
import java.lang.Double;
import java.util.Random;

public class KNearestNeighbor {
    //finds distance between two data points from attribute values
    public double getDistance(Node dp1, Node dp2){
        double distance;
        double sum = 0;

        for(int i = 0; i<dp1.getData().length; i++){
            double s =  ((dp1.getData()[i] - dp2.getData()[i]) * (dp1.getData()[i] - dp2.getData()[i]));
            sum += s;
        }
        distance = java.lang.Math.sqrt(sum);
        return distance;
    }

    public int getNearestNeighbors(Node datapoint, ArrayList<Node> datapoints, int k){
        ArrayList<Double> distances = new ArrayList<Double>();

        for(int i = 0; i<datapoints.size(); i++){
            double distance = getDistance(datapoint, datapoints.get(i));
            distances.add(distance);
        }

        if(k > distances.size()){
            throw new IllegalArgumentException("k can not be bigger than size of dataset");
        }

        ArrayList<Double> kLowestDistances = new ArrayList<>();
        ArrayList<Double> classOfKLowestDistances = new ArrayList<>();
        double classOfLowestDistance = 0;
        int lowestDistanceIndex = 999;

        ArrayList<Node> knodes = new ArrayList<>();

        for(int i = 0; i<datapoints.size(); i++){
            knodes.add(datapoints.get(i));
        }

        for(int i = 0; i<k; i++) {
            System.out.println(distances);
            double lowestDistance = Double.POSITIVE_INFINITY;
            for (int j = 0; j < distances.size(); j++) {
                if (distances.get(j) < lowestDistance) {
                    lowestDistance = distances.get(j);
                    lowestDistanceIndex = j;
                    classOfLowestDistance = knodes.get(j).getId();
                }
            }
            System.out.println("lowest class: ");
            System.out.println(classOfLowestDistance);
            classOfKLowestDistances.add(classOfLowestDistance);

            kLowestDistances.add(lowestDistance);
            System.out.println("lowest distance: ");
            System.out.println(lowestDistance);

            distances.remove(distances.get(lowestDistanceIndex));
            knodes.remove(knodes.get(lowestDistanceIndex));

            System.out.println("distances");
            for(int g = 0; g<distances.size(); g++){
                System.out.println(distances.get(g));
            }

            System.out.println("classes: ");
            for(int g = 0; g<classOfKLowestDistances.size(); g++) {
                System.out.println(classOfKLowestDistances.get(g));
            }
            System.out.println("");

        }

        int highestOccurence = 0;
        ArrayList<Double> classesWithHighestOccurence = new ArrayList<>();

        for(int i = 0; i<classOfKLowestDistances.size(); i++){
            int count = 0;
            for(int j = 0; j<classOfKLowestDistances.size(); j++){
                if(Double.compare(classOfKLowestDistances.get(j), classOfKLowestDistances.get(i)) == 0){
                    count += 1;
                }

            }
            if(count >= highestOccurence) {
                highestOccurence = count;
                classesWithHighestOccurence.add(classOfKLowestDistances.get(i));
            }

        }
        System.out.println("Highest Occurence: ");
        System.out.println(highestOccurence);
        for(int i = 0; i<classesWithHighestOccurence.size(); i++) {
            System.out.println(classesWithHighestOccurence);
        }

        Random rand = new Random();
        int classIndex = rand.nextInt(classesWithHighestOccurence.size());
        int chosenClass = (int)(double)classesWithHighestOccurence.get(classIndex);

        System.out.println("CHOSEN CLASS: ");
        System.out.println(chosenClass);
        System.out.println("REAL CLASS ");
        System.out.println((int)datapoint.getId());
        return chosenClass;
    }



    /*public double getNearestNeighborsRegression(Node datapoint, ArrayList<Node> datapoints, int k, String type){

    }*/

}
