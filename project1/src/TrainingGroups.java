import java.util.ArrayList;
import java.util.Collections;

public class TrainingGroups {
    private ArrayList<ArrayList<Node>> groups;
    private ArrayList<Node> fulldataset;
    private int folds = 10;    // Do 10-fold cross-validation
    private int testSetIndex;   // Indicates which fold to treat as the test data

    public TrainingGroups(ArrayList<Node> dataset) {
        this.groups = new ArrayList<>();
        this.fulldataset = new ArrayList<>(dataset);
        shuffleDataSet();
        partitionData();
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
        ArrayList<Node> trainingSet = new ArrayList<>();
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
