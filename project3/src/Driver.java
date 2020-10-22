import LossFunctions.F1Score;
import LossFunctions.Precision;
import LossFunctions.Recall;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Driver extends Thread//extending Thread allows for multithreading
{
	String fileStart = "dataSets/", fileEnd = ".data", filePath;
	
	public Driver(String filePath) {
		this.filePath = filePath;
	}

	public void run() //the method that is called when a Thread starts
	{
		//parse out the data in the file
		System.out.println(filePath);
		Parser p = new Parser();
		ArrayList<Node> nodes = null;
		boolean isRegression = false;
		switch (filePath) {//since each dataset is different it needs its own parse function
			case "abalone" -> {
				nodes = p.abaloneParser(fileStart + filePath + fileEnd);
				isRegression = true;
				System.out.println("Done Abalone");
			}
			case "breast-cancer-wisconsin" -> {
				nodes = p.cancerParser(fileStart + filePath + fileEnd);
				System.out.println("Done Cancer");
			}
			case "forestfires" -> {
				nodes = p.firesParser(fileStart + filePath + fileEnd);
				isRegression = true;
				System.out.println("Done Forest Fires");
			}
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				System.out.println("Done Glass");
			}
			case "machine" -> {
				nodes = p.machineParser(fileStart + filePath + fileEnd);
				isRegression = true;
				System.out.println("Done Machine");
			}
			case "soybean-small" -> {
				nodes = p.beanParser(fileStart + filePath + fileEnd);
				System.out.println("Done Soybean");
			}
			default -> System.out.println("Bad file path: " + filePath);
		}
		Normalization.zNormalize(nodes);	// use z-normalization to normalize the nodes
		
		Network net;
		for(int i = 0; i < 2; i++)
		{
			int[] hiddenLayers = new int[i];
			for(int j = 0; j < i; j++)
			{
				hiddenLayers[j] = (int)(Math.random()*nodes.get(0).getData().length-1);
			}
			for(int j = 0; j < nodes.; j++)
			{
			
			}
			net = new Network(new double[]{.5, .3, .35}, hiddenLayers, 1, .5, !isRegression);
			ArrayList<Neuron> output = net.feedForward();
			for(Neuron neuron : output)
			{
				System.out.println(neuron.getValue());
			}
		}
	}
	
	public int tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet){
		// outputs a k that is tuned for precision based on a training and tuning set
		System.out.println("Tuning k...");
		int bestk = 0;
		double bestPrecision = 0;
		ArrayList<Integer[]> results = new ArrayList<>();

		KNearestNeighbor knn = new KNearestNeighbor();
		for (int k = (int)Math.sqrt(trainingSet.size()) - 2; k <= (int)Math.sqrt(trainingSet.size()) + 2; k++) {
			//loop through 5 values of k centered around sqrt of the training set size
			for (Node example : tuningSet) {
				int real = (int) example.getId();
				int guess = knn.nearestNeighborClassification(example, trainingSet, k);
				Integer[] result = {guess, real};
				results.add(result);
			}

			Precision precision = new Precision(results);
			ArrayList<Integer> classes = precision.getClasses();

			double avgPrecision = 0;
			for(int _class: classes) {
				// finds precisions of each class and averages them
				precision.setTrueAndFalsePositives(_class);
				double precisionResult = precision.findPrecision();
				avgPrecision += precisionResult;

			}
			avgPrecision = avgPrecision/classes.size();	// take average
			System.out.println("Avg Precision with k value " + k + ": " + avgPrecision);
			if (avgPrecision > bestPrecision) {
				// save best values for k and average precision
				bestPrecision = avgPrecision;
				bestk = k;
			}
		}
		System.out.println("Chosen k: " + bestk);
		return bestk;
	}
	
	public float testFoldRegression(ArrayList<Node> trainingSet, ArrayList<Node> testSet, int k, float sigma, float threshold){
		// test a fold using KNN for regression, return accuracy
		System.out.println("Conducting test on testSet...");
		System.out.println("trainingSet size: " + trainingSet.size());
		System.out.println("testSet size: " + testSet.size());
		if (trainingSet.size() < k) {
			System.out.println("training set less than k. Reducing k...");
			k = (int)Math.sqrt(trainingSet.size());
		}
		KNearestNeighbor knn = new KNearestNeighbor();
		ArrayList<double[]> results = new ArrayList<>();

		for (Node example : testSet) {
			double guess = knn.nearestNeighborsRegression(example, trainingSet, example.getIgnoredAttr(), k, sigma);
			double real = example.getData()[example.getIgnoredAttr()];
			double[] result = {guess, real};
			results.add(result);
		}
		int correct = 0;
		for (double[] result : results) {
			if (Math.abs(result[0] - result[1]) < threshold) {
				correct++;
			}
		}
		float accuracy = (float)correct/results.size();
		System.out.println("Accuracy of Fold:" + accuracy);
		return accuracy;
	}
	
	public static void main(String[] args)
	{
		//use these if you want to run a single data set
		Driver test = new Driver("glass");
		test.start();
		

		//use these if you want to run all the data sets  "house-votes-84",
		/*
		String[] files = {"abalone", "breast-cancer-wisconsin", "forestfires", "glass", "machine", "soybean-small"};
		for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			d.start();//Starts a new thread
		}
		*/
	}
}