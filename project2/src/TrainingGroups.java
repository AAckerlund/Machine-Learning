import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

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
        sortData();
        //shuffleDataSet();   // randomize order of dataset so each group has a random grouping of entries
        partitionData();    // split data into 'folds' number of groups
        this.testSetIndex = 0;
    }

    private void sortData()
    {
        //using a lambda function to override the built in comparator so that we can sort by Node.id
        fulldataset.sort((n1, n2) -> Float.compare(n1.getId(), n2.getId()));
    }
    
    private void shuffleDataSet() {
        Collections.shuffle(fulldataset);
    }
    
    // partition data into a number of folds
    private void partitionData()
    {
        for(int i = 0; i < folds; i++)
        {
            groups.add(new ArrayList<>());
        }
        for(int i = 0; i < fulldataset.size(); i++)
        {
            groups.get(i%folds).add(fulldataset.get(i));
        }
        //print the Ids by group. This allows us to verify that stratification is being done correctly.
        /*for(ArrayList<Node> list : groups)
        {
            System.out.println("\nNew Group: " + list.size());
            for(Node node : list)
            {
                System.out.print(node.getId() + " | ");
            }
        }*/
    }
    
    public ArrayList<ArrayList<Node>> groupData()
    {
        ArrayList<ArrayList<Node>> groups = new ArrayList<>();
        
        //add the first element of fulldataset to the first group to prevent errors.
        groups.add(new ArrayList<>());
        groups.get(0).add(fulldataset.get(0));
        
        //split the data into groups by their id.
        for(int i = 1; i < fulldataset.size(); i++)
        {
            if(groups.get(groups.size() - 1).get(0).getId() != fulldataset.get(i).getId())//found a Id value, so add a new list of nodes
            {
                groups.add(new ArrayList<>());
            }
            groups.get(groups.size()-1).add(fulldataset.get(i));
        }
        return groups;
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
    
    public void printDataSetInfo()
    {
        int counter = 1;
        float currentValue = fulldataset.get(0).getId();
        for(int i = 1; i < fulldataset.size(); i++)
        {
            if(currentValue != fulldataset.get(i).getId())
            {
                System.out.println(currentValue + ": " + counter);
                currentValue = fulldataset.get(i).getId();
                counter = 1;
            }
            else
                counter++;
        }
        System.out.println(currentValue + ": " + counter);
    }
}
