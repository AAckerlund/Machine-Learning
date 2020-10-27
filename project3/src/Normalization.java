import java.util.ArrayList;
import java.util.Arrays;

public class Normalization {
    public static void zNormalize(ArrayList<Node> list) {
        //normalizes the attributes of nodes in a list using z-score normalization

        int numAttributes = list.get(0).getData().length;
        int numEntries = list.size();

        // Calculate mean of each attribute
        double [] meanList = new double[numAttributes];

        for (int i = 0; i < numAttributes; i++) {
            meanList[i] = 0;                            // Initialize all values in meanList to 0
        }

        for (Node node : list) {
            for (int j = 0; j < numAttributes; j++) {
                meanList[j] = meanList[j] + node.getData()[j];  // sum up all attributes
            }
        }

        for (int i = 0; i < numAttributes; i++) {
            meanList[i] = meanList[i] / numEntries;     // take mean of all attributes
        }

        // Find standard deviation of each attribute
        double [] stdDevList = new double[numAttributes];
        for (int i = 0; i < numAttributes; i++) {
            stdDevList[i] = 0;                          // initialize all values to 0
        }

        for (Node node : list) {
            for (int j = 0; j < numAttributes; j++) {
                stdDevList[j] = stdDevList[j] + Math.pow(node.getData()[j] - meanList[j], 2);   // sum variances
            }
        }

        for (int i = 0; i < numAttributes; i++) {
            // divide by number of entries and take the square roots to get the StdDev
            stdDevList[i] = Math.sqrt(stdDevList[i]/numEntries);
        }

        // Normalize all entries using z-score normalization
        for (Node node : list) {
            for (int j = 0; j < numAttributes; j++) {
                if (stdDevList[j] == 0) {
                    node.getData()[j] = 0;  // Prevent dividing by zero, score is just 0
                }
                else {
                    node.getData()[j] = ((node.getData()[j] - meanList[j]) / stdDevList[j]);
                }
            }
        }

        // Print out results
        /*for (Node node : list) {
            System.out.println(node.getId() + Arrays.toString(node.getData()));
        }

         */

    }
}
