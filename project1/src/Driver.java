import LossFunctions.Precision;
import LossFunctions.ZeroOneLoss;
import java.util.ArrayList;
import java.util.Arrays;//used in printing out the parsed data
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
		ZeroOneLoss lossfunction1 = new ZeroOneLoss();

		int correct = 0;
		int incorrect = 0;
		ArrayList<Integer[]> results = new ArrayList<Integer[]>();

		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();

			TrainingSetAlgorithm algo = new TrainingSetAlgorithm(trainingSet, attrValueLow, numattrValues);
			algo.train();
			ArrayList<Node> testSet = groups.getTestSet();

			for (Node example : testSet) {
				int guess;
				//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
				guess = algo.classifyExample(example.getData());
				System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess +
						" Real Class: " + (int)example.getId() + "\n");

				lossfunction1.total += 1;
				if(guess != (int)example.getId()){
					lossfunction1.errorCount += 1;
				}
				if(guess == (int)example.getId()){
					correct += 1;
				}
				else{
					incorrect += 1;
				}

				double pct = lossfunction1.getPercentCorrect();
				System.out.println(lossfunction1.total - lossfunction1.errorCount + " out of ");
				System.out.println(lossfunction1.total);
				System.out.println(pct);
			}
			groups.iterateTestSet();
		}
		for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}


		// ***** The data is shuffled, and then all the previous steps are run again
		DataShuffler.shuffleFeatureData(nodes);	// Shuffle data, repeat previous steps again
		System.out.println("Running shuffled data");

		groups = new TrainingGroups(nodes);
		lossfunction1 = new ZeroOneLoss();

		correct = 0;
		incorrect = 0;

		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();

			TrainingSetAlgorithm algo = new TrainingSetAlgorithm(trainingSet, attrValueLow, numattrValues);
			algo.train();
			ArrayList<Node> testSet = groups.getTestSet();

			for (Node example : testSet) {
				int guess;
				int real = (int)example.getId();
				//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
				guess = algo.classifyExample(example.getData());
				System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess +
						" Real Class: " + real + "\n");
				Integer[] result = {guess, real};
				results.add(result);

				lossfunction1.total += 1;
				if(guess != (int)example.getId()){
					lossfunction1.errorCount += 1;
				}
				if(guess == (int)example.getId()){
					correct += 1;
				}
				else{
					incorrect += 1;
				}

				double pct = lossfunction1.getPercentCorrect();
				System.out.println(lossfunction1.total - lossfunction1.errorCount + " out of ");
				System.out.println(lossfunction1.total);
				System.out.println(pct);

			}
			System.out.println(results.get(0)[0]);
			System.out.println(results.get(0)[1]);
			for(Integer[] result: results){
				//System.out.println(result[0]);
				//System.out.println(result[1]);
			}

			Precision precision = new Precision(results);
			ArrayList<Integer> classes = precision.getClasses();
			/*for(int classe: classes){
				System.out.println("classe: ");
				System.out.println(classe);
			}*/

			for(int _class: classes){
				precision.setTrueAndFalsePositives(classes, _class);
				int tp = precision.truePositives;
				int fp = precision.falsePositives;
				double pcn = precision.findPrecision();
				System.out.println("Precision of class " + _class);
				System.out.println("True Positives: " + tp);
				System.out.println("False Positives: " + fp);
				System.out.println("Precision: " + pcn);
				System.out.println("");
			}


			groups.iterateTestSet();
		}
		for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}
	}

	public static void main(String[] args) {

		//String[] files = {"house-votes-84", "breast-cancer-wisconsin", "glass", "iris", "soybean-small"};
		String house_votes = "house-votes-84";
		String breast_cancer_wisconsin = "breast-cancer-wisconsin";
		String glass = "glass";
		String iris = "iris";
		String soybean_small = "soybean-small";

		/*for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			d.start();//Starts a new thread
		}*/
		//Driver d = new Driver("breast-cancer-wisconsin");
		//d.start();
		Driver house_votes_driver = new Driver(house_votes);
		Driver breast_cancer_wisconsin_driver = new Driver(breast_cancer_wisconsin);
		Driver glass_driver = new Driver(glass);
		Driver iris_driver = new Driver(iris);
		Driver soybean_small_driver = new Driver(soybean_small);

		//System.out.println("House Votes Thread: ");
		house_votes_driver.start();
		//System.out.println("Breast Cancer Thread: ");
		//breast_cancer_wisconsin_driver.start();
		System.out.println("Glass Thread: ");
		glass_driver.start();
		//System.out.println("Iris Thread: ");
		//iris_driver.start();
		//System.out.println("Soybean Thread: ");
		//soybean_small_driver.start();
	}
}