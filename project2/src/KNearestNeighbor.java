import java.util.ArrayList;
import java.lang.Double;
import java.util.Random;
import java.util.HashMap;

public class KNearestNeighbor {
    //finds distance between two data points from attribute values
    public double getDistance(Node dp1, Node dp2){
        double distance;
        double sum = 0;

        //System.out.println("LENGTH OF 1");
        //System.out.println(dp1.getData().length);

        for(int i = 0; i<dp1.getData().length; i++){
            double s =  ((dp1.getData()[i] - dp2.getData()[i]) * (dp1.getData()[i] - dp2.getData()[i]));
            sum += s;
        }
        distance = java.lang.Math.sqrt(sum);
        return distance;
    }

    public int nearestNeighborClassification(Node datapoint, ArrayList<Node> datapoints, int k){
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

        //System.out.println("CHOSEN CLASS: ");
        //System.out.println(chosenClass);
        //System.out.println("REAL CLASS ");
        //System.out.println((int)datapoint.getId());
        return chosenClass;
    }



    public double nearestNeighborsRegression(Node datapoint, ArrayList<Node> datapoints, int ignoredAttr, int k){
        //ArrayList<Double> distances = new ArrayList<Double>();
        //ArrayList<Node> dpCopy = new ArrayList<>();
        //ArrayList<Float> datal = new ArrayList<>();
        //float[][] dataWithoutTarget = {};

        ArrayList<Float> modifiedData = new ArrayList<>();
        for(int i = 0; i<datapoint.getData().length; i++){
            modifiedData.add(datapoint.getData()[i]);
        }
        modifiedData.remove(ignoredAttr);

        float[] modifiedDatapoint = new float[modifiedData.size()];

        for(int i = 0; i< modifiedData.size(); i++){
            modifiedDatapoint[i] = modifiedData.get(i);
        }

        Node dpNode = new Node(0, modifiedDatapoint, 0);

        /*for(int i = 0; i<dpNode.getData().length; i++){
            System.out.println(dpNode.getData()[i]);
        }*/

        ArrayList<ArrayList<Float>> dataWithoutTarget = new ArrayList<>();

        /*ArrayList<Integer> ints = new ArrayList<>();
        ArrayList<Integer> ints2 = (ArrayList<Integer>)ints.clone();

        ints.add(3);
        ints2.add(1);

        for(int i = 0; i<ints2.size(); i++){
            System.out.println(ints2.get(i));
        }*/

        /*for(int i = 0; i<datapoints.size(); i++){
            dpCopy.add(datapoints.get(i));
        }*/

        //System.out.println("LENGTH");
        //System.out.println(datapoint.getData().length);

        /*for(int i = 0; i<dpCopy.size(); i++){
            System.out.println(dpCopy.get(i).getData()[12]);
        }*/

        for(int i = 0; i<datapoints.size(); i++){
            ArrayList<Float> datal = new ArrayList<>();
            for(int j = 0; j<datapoints.get(i).getData().length; j++){
                datal.add(datapoints.get(i).getData()[j]);
            }
            datal.remove(ignoredAttr);

            dataWithoutTarget.add(datal);
        }

        //verifies deleteing of ignored attribute was done correctly
        /*for(int i = 0; i<dataWithoutTarget.size(); i++){
            for(int j = 0; j<dataWithoutTarget.get(i).size(); j++) {
                System.out.print(dataWithoutTarget.get(i).get(j) + ", ");
            }
            System.out.println("");
        }*/

        float[][] newdata = new float[dataWithoutTarget.size()][dataWithoutTarget.get(0).size()];
        for(int i = 0; i<dataWithoutTarget.size(); i++){
            for(int j = 0; j<dataWithoutTarget.get(i).size(); j++) {
                float datapiece = dataWithoutTarget.get(i).get(j);
                newdata[i][j] = datapiece;
                System.out.print(datapiece + ", ");
            }
            System.out.println("");
        }



        ArrayList<Node> modifiedDatapoints = new ArrayList<>();

        for(int i = 0; i<dataWithoutTarget.size(); i++){
            Node node = new Node(0, newdata[i], 0);
            modifiedDatapoints.add(node);
        }

        ArrayList<Double> distances = new ArrayList<Double>();

        for(int i = 0; i<modifiedDatapoints.size(); i++){
            double distance = getDistance(dpNode, modifiedDatapoints.get(i));
            distances.add(distance);
        }

        for(int i = 0; i<distances.size(); i++){
            System.out.println(distances.get(i));
        }

        HashMap indexToDistance = new HashMap<Integer, Double>();

        for(int i = 0; i<distances.size(); i++){
            indexToDistance.put(i, distances.get(i));
        }

        ArrayList<Double> nearestNeighbors = new ArrayList<>();
        ArrayList<Integer> nearestNeighborIndexes = new ArrayList<>();
        int nearestNeighborIndex = 0;

        for(int i = 0; i<k; i++){
            double lowestDistance = Double.POSITIVE_INFINITY;
            for(int j = 0; j<distances.size(); j++){
                if(distances.get(j) < lowestDistance){
                    if(!nearestNeighborIndexes.contains(j)) {
                        nearestNeighborIndex = j;
                        lowestDistance = distances.get(j);
                    }
                }
            }
            nearestNeighborIndexes.add(nearestNeighborIndex);
            nearestNeighbors.add(lowestDistance);


        }


        /*double neighborTargetValue = datapoints.get(18).getData()[ignoredAttr];
        System.out.println(neighborTargetValue);

        System.out.println(nearestNeighborIndexes.size());
        System.out.println(nearestNeighbors.size());
        System.out.println(distances.size());
        System.out.println(datapoints.size());*/


        double pv = gaussianEquation(0.1, nearestNeighbors, nearestNeighborIndexes, datapoints);
        System.out.println("PREDICTED VALUE: ");
        System.out.println(pv);
        System.out.println("REAL VALUE: ");
        System.out.println(datapoint.getData()[ignoredAttr]);


        return 2;
    }

    public double gaussianEquation(double sigma, ArrayList<Double> nearestNeighbors, ArrayList<Integer> nearestNeighborIndexes, ArrayList<Node> datapoints){
        double numerator = 0;
        double denominator = 0;
        int index = 0;

        /*for(int i = 0; i<nearestNeighborIndexes.size(); i++){
            int ignoredAttr = datapoints.get(i).getIgnoredAttr();
            double a = datapoints.get(nearestNeighborIndexes.get(index)).getData()[ignoredAttr];
            System.out.println(nearestNeighborIndexes.get(index));
            System.out.println(a);
        }*/

        for(int i = 0; i<nearestNeighbors.size(); i++){
            int ignoredAttr = datapoints.get(i).getIgnoredAttr();
            double neighborTargetValue = datapoints.get(nearestNeighborIndexes.get(index)).getData()[ignoredAttr];
            System.out.println("NTV" + neighborTargetValue);
            numerator += (Math.exp((-(nearestNeighbors.get(i) * nearestNeighbors.get(i))) / sigma)) * neighborTargetValue;
            denominator += (Math.exp((-(nearestNeighbors.get(i) * nearestNeighbors.get(i))) / sigma));
            index += 1;
        }

        double predictedValue = numerator / denominator;
        System.out.println(predictedValue);
        return predictedValue;
    }

}
