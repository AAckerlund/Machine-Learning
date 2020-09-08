import java.util.ArrayList;
import java.util.Collections;

public class DataShuffler {
    public static void shuffleFeatureData(ArrayList<Node> nodes) {
        // shuffles data between the nodes. Modifies existing copy. It does not create a new dataset
        ArrayList<Integer> featuresToShuffle = new ArrayList<Integer>();

        int numFeatures = nodes.get(1).getData().length;    //total number of features
        int numFeaturesToShuffle = numFeatures/10;
        if (numFeaturesToShuffle == 0) {
            numFeaturesToShuffle = 1;   // always shuffle at least one feature (for datasets with less than 10 features)
        }
        for (int i = 0; i < numFeaturesToShuffle; i++) {
            featuresToShuffle.add((int)(numFeatures*Math.random()));
        }

        for (int featureIndex : featuresToShuffle) {
            // repeat for each feature that has been picked
            System.out.println("Shuffling Feature: " + featureIndex);
            ArrayList<Float> values = new ArrayList<Float>();
            for (Node node : nodes) {
                // pull out feature values into list
                float[] data = node.getData();
                values.add(data[featureIndex]);
            }
            Collections.shuffle(values);    // Shuffle feature values
            for (Node node : nodes) {
                float[] data = node.getData();
                data[featureIndex] = values.remove(0); // pop from arraylist, save to node
            }
        }
        return;
    }
}
