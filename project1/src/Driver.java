import LossFunctions.F1Score;
import LossFunctions.Precision;
import LossFunctions.Recall;

import java.util.ArrayList;
import java.util.Arrays;//used in printing out the parsed data


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
		
		BayesNet(nodes, attrValueLow, numattrValues);//runs on the data as it appears in the .data files.
		DataShuffler.shuffleFeatureData(nodes);	//Shuffle one attribute
		System.out.println("Running shuffled data");
		BayesNet(nodes, attrValueLow, numattrValues);//runs on the data that has had one attribute shuffled.
	}
	
	public void BayesNet(ArrayList<Node> nodes, int attrValueLow, int numattrValues)
	{
		TrainingGroups groups = new TrainingGroups(nodes);
		ArrayList<Integer[]> results = new ArrayList<>();

		for (int i = 0; i < 10; i++) {
			//System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();

			TrainingSetAlgorithm algo = new TrainingSetAlgorithm(trainingSet, attrValueLow, numattrValues);
			algo.train();
			ArrayList<Node> testSet = groups.getTestSet();

			for (Node example : testSet) {
				int guess;
				int real = (int) example.getId();
				//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
				guess = algo.classifyExample(example.getData());
				System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess + " Real Class: " + real + "\n");
				Integer[] result = {guess, real};
				results.add(result);
			}
			
			Precision precision = new Precision(results);
			Recall recall = new Recall(results);
			ArrayList<Integer> classes = precision.getClasses();

			for(int _class: classes){
				precision.setTrueAndFalsePositives(_class);
				int ptp = precision.getTruePositives();
				int pfp = precision.getFalsePositives();
				double precisionResult = precision.findPrecision();
				System.out.println("Precision of class " + _class + "\tTrue Positives: " + ptp + "\tFalse Positives: " + pfp + "\tPrecision: " + precisionResult);
				
				recall.setTruePositivesAndFalseNegatives(_class);
				int rtp = recall.getTruePositives();
				int rfn = recall.getFalseNegatives();
				double recallResult = recall.findRecall();
				F1Score f1score = new F1Score(precisionResult, recallResult);
				double f1Score = f1score.getF1Score();

				System.out.println("Recall of class " + _class + "\tTrue Positives: " + rtp + "\tFalse Negatives: " + rfn + "\tRecall: " + recallResult);
				System.out.println("F1 Score: " + f1Score);
				System.out.println("");
			}
			groups.iterateTestSet();
		}
		/*for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}*/
	}

	public static void main(String[] args) {
		String[] files = {"house-votes-84", "breast-cancer-wisconsin", "glass", "iris", "soybean-small"};
		
		for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			System.out.println("\n********************\n" + file + "\n********************\n");
			d.run();//Starts a new thread
		}

		/*
		Driver house_votes_driver = new Driver("house-votes-84");
		System.out.println("House Votes Thread: ");
		house_votes_driver.start();
*/
		/*
		Driver breast_cancer_wisconsin_driver = new Driver("breast-cancer-wisconsin");
		System.out.println("Breast Cancer Thread: ");
		breast_cancer_wisconsin_driver.start();
		*/
		/*
		Driver glass_driver = new Driver("glass");
		System.out.println("Glass Thread: ");
		glass_driver.start();
		*/
		/*
		Driver iris_driver = new Driver("iris");
		System.out.println("Iris Thread: ");
		iris_driver.start();
		*/
		/*Driver soybean_small_driver = new Driver("soybean-small");
		System.out.println("Soybean Thread: ");
		soybean_small_driver.start();*/
		
	}
}