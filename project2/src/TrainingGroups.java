import java.util.ArrayList;
import java.util.Collections;

public class TrainingGroups {
    // Splits and stores dataset into a predetermined number of partitions
    // These partitions can be grouped into a training set and a test set depending on which fold is being tested
    private ArrayList<ArrayList<Node>> groups;
    private ArrayList<Node> fulldataset;
    private int folds = 10;    // Do 10-fold cross-validation
    private int testSetIndex;   // Indicates which fold to select as the test data

    public TrainingGroups(ArrayList<Node> dataset) {
        this.groups = new ArrayList<>();
        this.fulldataset = new ArrayList<>(dataset);
        shuffleDataSet();   // randomize order of dataset so each group has a random grouping of entries
        partitionData();    // split data into 'folds' number of groups
        this.testSetIndex = 0;
    }

    private void shuffleDataSet() {
        Collections.shuffle(fulldataset);
    }

    private void partitionData() {
        // partition data into a number of folds
        //System.out.print("Partitioning Data...");
        for (int i = 0; i < folds; i++) {
            ArrayList<Node> fold = new ArrayList<>();
            for (int j = i*fulldataset.size()/folds; j < (i+1)*fulldataset.size()/folds; j++) {
                fold.add(fulldataset.get(j));
            }
            groups.add(fold);
        }
        //System.out.println("Done!");
    }

    public ArrayList<Node> getTrainingSet() {
        // Groups all folds that are not the test set into the training set
        ArrayList<Node> trainingSet = new ArrayList<>();
        for (int i = 0; i < folds; i++) {
            if (i != this.testSetIndex) {
                trainingSet.addAll(groups.get(i));
            }
        }
        return trainingSet;
    }

    public ArrayList<Node> getTestSet() {
        // Returns the current test set
        return groups.get(testSetIndex);
    }

    public void iterateTestSet() {
        this.testSetIndex++;
    }
}
