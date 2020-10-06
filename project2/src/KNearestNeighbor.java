import java.util.ArrayList;
import java.lang.Double;
import java.util.Random;
import java.util.HashMap;

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

    public int nearestNeighborClassification(Node datapoint, ArrayList<Node> datapoints, int k){
        ArrayList<Double> distances = new ArrayList<>();
    
        for(Node node : datapoints)
        {
            double distance = getDistance(datapoint, node);
            distances.add(distance);
        }

        if(k > distances.size()){
            throw new IllegalArgumentException("k can not be bigger than size of dataset");
        }

        ArrayList<Double> classOfKLowestDistances = new ArrayList<>();
        double classOfLowestDistance = 0;
        int lowestDistanceIndex = 999;
    
        ArrayList<Node> knodes = new ArrayList<>(datapoints);

        for(int i = 0; i<k; i++) {
            //System.out.println(distances);
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

            System.out.println("lowest distance: ");
            System.out.println(lowestDistance);

            distances.remove(distances.get(lowestDistanceIndex));
            knodes.remove(knodes.get(lowestDistanceIndex));

            System.out.println("distances");
            for(Double distance : distances)
            {
                System.out.println(distance);
            }

            System.out.println("classes: ");
            for(Double classOfKLowestDistance : classOfKLowestDistances)
            {
                System.out.println(classOfKLowestDistance);
            }
            System.out.println();

        }

        int highestOccurence = 0;
        ArrayList<Double> classesWithHighestOccurence = new ArrayList<>();

        for(int i = 0; i<classOfKLowestDistances.size(); i++){
            int count = 0;
            for(Double classOfKLowestDistance : classOfKLowestDistances)
            {
                if(Double.compare(classOfKLowestDistance, classOfKLowestDistances.get(i)) == 0)
                {
                    count += 1;
                }
        
            }
            if(count >= highestOccurence) {
                highestOccurence = count;
                classesWithHighestOccurence.add(classOfKLowestDistances.get(i));
            }

        }
        //System.out.println("Highest Occurence: ");
        //System.out.println(highestOccurence);
        for(int i = 0; i<classesWithHighestOccurence.size(); i++) {
            //System.out.println(classesWithHighestOccurence);
        }

        Random rand = new Random();
        int classIndex = rand.nextInt(classesWithHighestOccurence.size());
    
        //System.out.println("CHOSEN CLASS: ");
        //System.out.println(chosenClass);
        //System.out.println("REAL CLASS ");
        //System.out.println((int)datapoint.getId());
        return (int)(double)classesWithHighestOccurence.get(classIndex);
    }
    
    public double nearestNeighborsRegression(Node datapoint, ArrayList<Node> datapoints, int ignoredAttr, int k, double sigma){
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
        ArrayList<ArrayList<Float>> dataWithoutTarget = new ArrayList<>();
    
        for(Node value : datapoints)
        {
            ArrayList<Float> datal = new ArrayList<>();
            for(int j = 0; j < value.getData().length; j++)
            {
                datal.add(value.getData()[j]);
            }
            datal.remove(ignoredAttr);
        
            dataWithoutTarget.add(datal);
        }

        float[][] newdata = new float[dataWithoutTarget.size()][dataWithoutTarget.get(0).size()];
        for(int i = 0; i<dataWithoutTarget.size(); i++){
            for(int j = 0; j<dataWithoutTarget.get(i).size(); j++) {
                float datapiece = dataWithoutTarget.get(i).get(j);
                newdata[i][j] = datapiece;
            }
        }
        
        ArrayList<Node> modifiedDatapoints = new ArrayList<>();

        for(int i = 0; i<dataWithoutTarget.size(); i++){
            Node node = new Node(0, newdata[i], 0);
            modifiedDatapoints.add(node);
        }

        ArrayList<Double> distances = new ArrayList<>();
    
        for(Node node : modifiedDatapoints)
        {
            double distance = getDistance(dpNode, node);
            distances.add(distance);
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
        double predictedValue = gaussianEquation(.1, nearestNeighbors, nearestNeighborIndexes, datapoints);
        System.out.println("PREDICTED VALUE: ");
        System.out.println(predictedValue);
        System.out.println("REAL VALUE: ");
        System.out.println(datapoint.getData()[ignoredAttr]);


        return predictedValue;
    }

    public double gaussianEquation(double sigma, ArrayList<Double> nearestNeighbors, ArrayList<Integer> nearestNeighborIndexes, ArrayList<Node> datapoints){
        double numerator = 0;
        double denominator = 0;
        int index = 0;

        for(int i = 0; i<nearestNeighbors.size(); i++){
            int ignoredAttr = datapoints.get(i).getIgnoredAttr();
            double neighborTargetValue = datapoints.get(nearestNeighborIndexes.get(index)).getData()[ignoredAttr];
            double nnVal = nearestNeighbors.get(i);
            numerator += (Math.exp((-(nnVal * nnVal)) / sigma)) * neighborTargetValue;
            denominator += (Math.exp((-(nnVal * nnVal)) / sigma));
            index += 1;

            /*if(Double.isNaN(numerator/denominator)){
                nnVal = nnVal / 10000;
                numerator += (Math.exp((-(nnVal * nnVal)) / sigma)) * neighborTargetValue;
                denominator += (Math.exp((-(nnVal * nnVal)) / sigma));
            }*/
        }

        //System.out.println(numerator);
        //System.out.println(denominator);
    
        return numerator / denominator;
    }

}
