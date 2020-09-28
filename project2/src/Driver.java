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
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				System.out.println("Done Glass");
			}
			case "abalone" -> {
				nodes = p.abaloneParser(fileStart + filePath + fileEnd);
				System.out.println("Done Abalone");
			}
			case "forestfires" -> {
				nodes = p.firesParser(fileStart + filePath + fileEnd);
				System.out.println("Done Forest Fires");
			}
			case "machine" -> {
				nodes = p.machineParser(fileStart + filePath + fileEnd);
				System.out.println("Done Machine");
			}
			case "segmentation" -> {
				nodes = p.segmentationParser(fileStart + filePath + fileEnd);
				System.out.println("Done Segmentation");
			}
			default -> System.out.println("Bad file path: " + filePath);
		}
		
		//print data to verify
		/*for(Node node : nodes)
			System.out.println(node.getId() + " " + Arrays.toString(node.getData()));
		
		/*
		BayesNet(nodes, attrValueLow, numattrValues);//runs on the data as it appears in the .data files.
		DataShuffler.shuffleFeatureData(nodes);	//Shuffle one attribute
		System.out.println("Running shuffled data");
		BayesNet(nodes, attrValueLow, numattrValues);//runs on the data that has had one attribute shuffled.
		 */
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
		String[] files = {"abalone", "forestfires", "glass", "house-votes-84", "machine", "segmentation"};
		
		//use these if you want to run a single data set
		//Driver test = new Driver(files[5]);
		//test.start();
		
		//use these if you want to run all the data sets
		for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			//System.out.println("\n********************\n" + file + "\n********************\n");
			d.start();//Starts a new thread
		}
	}
}