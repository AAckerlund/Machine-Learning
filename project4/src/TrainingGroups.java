import java.util.ArrayList;
import java.util.Comparator;

public class TrainingGroups
{
	// Splits and stores dataset into a predetermined number of partitions
	// These partitions can be grouped into a training set and a test set depending on which fold is being tested
	private final ArrayList<ArrayList<Node>> groups;
	private ArrayList<Node> tuningSet;
	private final ArrayList<Node> fullDataSet;
	private final int folds = 10;    // Do 10-fold cross-validation
	private int testSetIndex;   // Indicates which fold to select as the test data
	
	public TrainingGroups(ArrayList<Node> dataset, int testSetIndex)
	{
		this.groups = new ArrayList<>();
		this.fullDataSet = new ArrayList<>(dataset);
		sortData();
		partitionData();    // split data into 'folds' number of groups
		this.testSetIndex = testSetIndex;
	}
	
	private void sortData()
	{
		//using a lambda function to override the built in comparator so that we can sort by Node.id
		fullDataSet.sort(Comparator.comparingDouble(Node :: getId));
	}
	
	// partition data into a number of folds and a tuning set
	private void partitionData()
	{
		for(int i = 0; i < folds; i++)
		{
			groups.add(new ArrayList<>());
		}
		tuningSet = new ArrayList<>();
		for(int i = 0; i < fullDataSet.size(); i++)
		{
			if(i % (folds + 1) < folds)
			{
				// add the first 10 (i=0 to 9) entries to folds
				groups.get(i % (folds + 1)).add(fullDataSet.get(i));
			}
			else
			{
				// add every 11th (i=10) entry to the tuning set
				tuningSet.add(fullDataSet.get(i));
			}
		}
	}
	
	public ArrayList<Node> getTrainingSet()
	{
		// Groups all folds that are not the test set into the training set
		ArrayList<Node> trainingSet = new ArrayList<>();
		for(int i = 0; i < folds; i++)
		{
			if(i != this.testSetIndex)
			{
				trainingSet.addAll(groups.get(i));
			}
		}
		return trainingSet;
	}
	
	public ArrayList<Node> getTestSet()
	{
		// Returns the current test set
		return groups.get(testSetIndex);
	}
	
	public ArrayList<Node> getTuningSet()
	{
		// Returns Tuning Set
		return tuningSet;
	}
	
	public void iterateTestSet()
	{
		if(testSetIndex >= groups.size() - 1)
			testSetIndex = 0;
		else
			this.testSetIndex++;
	}
}