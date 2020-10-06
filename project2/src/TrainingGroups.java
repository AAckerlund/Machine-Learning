import java.util.ArrayList;

public class TrainingGroups {
    // Splits and stores dataset into a predetermined number of partitions
    // These partitions can be grouped into a training set and a test set depending on which fold is being tested
    private final ArrayList<ArrayList<Node>> groups;
    private ArrayList<Node> tuningSet;
    private final ArrayList<Node> fulldataset;
    private final int folds = 10;    // Do 10-fold cross-validation
    private int testSetIndex;   // Indicates which fold to select as the test data

    public TrainingGroups(ArrayList<Node> dataset) {
        this.groups = new ArrayList<>();
        this.fulldataset = new ArrayList<>(dataset);
        sortData();
        partitionData();    // split data into 'folds' number of groups
        this.testSetIndex = 0;
    }

    private void sortData()
    {
        //using a lambda function to override the built in comparator so that we can sort by Node.id
        fulldataset.sort((n1, n2) -> Float.compare(n1.getId(), n2.getId()));
    }
    
    // partition data into a number of folds and a tuning set
    private void partitionData()
    {
        for(int i = 0; i < folds; i++)
        {
            groups.add(new ArrayList<>());
        }
        tuningSet = new ArrayList<>();
        for(int i = 0; i < fulldataset.size(); i++)
        {
            if (i%(folds+1) < folds) {
                // add the first 10 (i=0 to 9) entries to folds
                groups.get(i % (folds + 1)).add(fulldataset.get(i));
            }
            else
            {
                // add every 11th (i=10) entry to the tuning set
                tuningSet.add(fulldataset.get(i));
            }
        }
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

    public ArrayList<Node> getTuningSet() {
        // Returns Tuning Set
        return tuningSet;
    }

    public void iterateTestSet() {
        this.testSetIndex++;
    }
}
