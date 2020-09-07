import java.util.ArrayList;
import java.util.Arrays;//used in printing out the parsed data
import java.util.HashMap;
import java.util.Objects;

public class Driver extends Thread//extending Thread allows for multithreading
{
	String fileStart = "dataSets/", fileEnd = ".data", filePath;

	public Driver(String filePath) {
		this.filePath = filePath;
	}

	public void run()//the method that is called when a Thread starts
	{
		//parse out the data in the file
		System.out.println(filePath);
		Parser p = new Parser();
		ArrayList<Node> nodes = null;
		int attrValueLow = 1;
		int numattrValues = 10;	// most datasets have 10 attribute values or 10 bins
		switch (filePath) {
			case "house-votes-84" -> {
				nodes = p.votesParser(fileStart + filePath + fileEnd);
				numattrValues = 2;
				//attributeRanges =
				System.out.println("Done Votes");
			}
			case "breast-cancer-wisconsin" -> {
				nodes = p.cancerParser(fileStart + filePath + fileEnd);
				System.out.println("Done Cancer");
			}
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				System.out.println("Done Glass");
			}
			case "iris" -> {
				nodes = p.irisParser(fileStart + filePath + fileEnd);
				System.out.println("Done Iris");
			}
			case "soybean-small" -> {
				nodes = p.beanParser(fileStart + filePath + fileEnd);
				attrValueLow = 0;
				System.out.println("Done Beans");
			}
			default -> System.out.println("Bad file path: " + filePath);
		}
		TrainingGroups groups = new TrainingGroups(nodes);
		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();

			// debug function
			/*int countDems = 0;
			int countReps = 0;
			for (Node node : trainingSet) {
				if ((int)node.getId() == 1) {
					countDems++;
				}
				if ((int)node.getId() == 0) {
					countReps++;
				}
			}
			System.out.println("Percent Dems: " + (float)countDems/trainingSet.size());
			System.out.println("Percent Reps: " + (float)countReps/trainingSet.size());*/

			TrainingSetAlgorithm algo = new TrainingSetAlgorithm(trainingSet, attrValueLow, numattrValues);
			algo.train();
			ArrayList<Node> testSet = groups.getTestSet();
			for (Node example : testSet) {
				int guess;
				//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
				guess = algo.classifyExample(example.getData());
				System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess +
						" Real Class: " + (int)example.getId() + "\n");
			}
			groups.iterateTestSet();
		}
		for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}
	}

	public static void main(String[] args) {

		String[] files = {"house-votes-84", "breast-cancer-wisconsin", "glass", "iris", "soybean-small"};

		for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			d.start();//Starts a new thread
		}
		//Driver d = new Driver("breast-cancer-wisconsin");
		//d.start();
	}
}