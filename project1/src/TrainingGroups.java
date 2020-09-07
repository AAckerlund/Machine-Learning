import java.util.ArrayList;
import java.util.Collections;

public class TrainingGroups {

    ArrayList<ArrayList<Node>> groups;
    ArrayList<Node> fulldataset;
    int folds = 10;    // Do 10-fold cross-validation
    int testSetIndex;   // Indicates which fold to treat as the test data

    public TrainingGroups(ArrayList<Node> dataset) {
        this.groups = new ArrayList<ArrayList<Node>>();
        this.fulldataset = new ArrayList<Node>(dataset);
        shuffleDataSet();
        partitionData();
        this.testSetIndex = 0;
    }

    private void shuffleDataSet() {
        Collections.shuffle(fulldataset);
    }

    private void partitionData() {
        // partition data into a number of folds
        System.out.print("Partitioning Data...");
        for (int i = 0; i < folds; i++) {
            ArrayList<Node> fold = new ArrayList<Node>();
            for (int j = i*fulldataset.size()/folds; j < (i+1)*fulldataset.size()/folds; j++) {
                fold.add(fulldataset.get(j));
            }
            groups.add(fold);
        }
        System.out.println("Done!");
    }

    public ArrayList<Node> getTrainingSet() {
        ArrayList<Node> trainingSet = new ArrayList<Node>();
        for (int i = 0; i < folds; i++) {
            if (i != this.testSetIndex) {
                trainingSet.addAll(groups.get(i));
            }
        }
        return trainingSet;
    }

    public ArrayList<Node> getTestSet() {
        return groups.get(testSetIndex);
    }

    public void iterateTestSet() {
        this.testSetIndex++;
    }
}
