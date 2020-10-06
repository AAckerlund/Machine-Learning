import LossFunctions.F1Score;
import LossFunctions.Precision;
import LossFunctions.Recall;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

public class Driver extends Thread//extending Thread allows for multithreading
{
	String fileStart = "dataSets/", fileEnd = ".data", filePath;

	public Driver(String path, int num) throws FileNotFoundException
	{
		safePrintln("" + num);
	}
	public Driver(String filePath) {
		this.filePath = filePath;
	}

	public void run() //the method that is called when a Thread starts
	{
		//parse out the data in the file
		try
		{
			safePrintln(filePath);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		Parser p = new Parser(this);
		ArrayList<Node> nodes = null;
		boolean isRegression = false;
		switch (filePath) {//since each dataset is different it needs its own parse function
			case "house-votes-84" -> {
				try
				{
					nodes = p.votesParser(fileStart + filePath + fileEnd);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				try
				{
					safePrintln("Done Votes");
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			case "glass" -> {
				try
				{
					nodes = p.glassParser(fileStart + filePath + fileEnd);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				try
				{
					safePrintln("Done Glass");
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			case "abalone" -> {
				try
				{
					nodes = p.abaloneParser(fileStart + filePath + fileEnd);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				isRegression = true;
				try
				{
					safePrintln("Done Abalone");
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			case "forestfires" -> {
				try
				{
					nodes = p.firesParser(fileStart + filePath + fileEnd);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				isRegression = true;
				try
				{
					safePrintln("Done Forest Fires");
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			case "machine" -> {
				try
				{
					nodes = p.machineParser(fileStart + filePath + fileEnd);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				isRegression = true;
				try
				{
					safePrintln("Done Machine");
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			case "segmentation" -> {
				try
				{
					nodes = p.segmentationParser(fileStart + filePath + fileEnd);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
				try
				{
					safePrintln("Done Segmentation");
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
			default -> {
				try
				{
					safePrintln("Bad file path: " + filePath);
				}
				catch(FileNotFoundException e)
				{
					e.printStackTrace();
				}
			}
		}
		if (isRegression) {
			try
			{
				crossValidationRegression(nodes);
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
		else {
			try
			{
				crossValidation(nodes);
			}
			catch(FileNotFoundException e)
			{
				e.printStackTrace();
			}
		}
	}

	public float[] tuneRegression(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet, double threshold) throws FileNotFoundException {
		safePrintln("Tuning k and sigma...");
		double[] sigmas = {0.1f, 0.01f, 0.001f, 0.0001f, 0.00001f};

		int bestk = 0;
		double bestSigma = 0.1f;
		double bestAccuracy = 0;
		ArrayList<Double[]> results = new ArrayList<>();

		KNearestNeighbor knn = new KNearestNeighbor();
		for (int k = (int)Math.sqrt(trainingSet.size()) - 2; k <= (int)Math.sqrt(trainingSet.size()) + 2; k++) {
			for (double sigma : sigmas) {
				//loop through 5 values of k centered around sqrt of the training set size
				for (Node example : tuningSet) {
					double real = example.getData()[example.getIgnoredAttr()];
					double guess = knn.nearestNeighborsRegression(example, trainingSet, example.getIgnoredAttr(), k, sigma);
					Double[] result = {guess, real};
					results.add(result);
				}
				int correct = 0;
				for(Double[] result : results)
				{
					if(Math.abs(result[0] - result[1]) < threshold)
					{
						// check if returned result is within the threshold value from the real value
						correct++;
					}
				}
				float accuracy = (float)correct/results.size();
				if (accuracy > bestAccuracy) {
					// save best values for k and average precision
					bestAccuracy = accuracy;
					bestSigma = sigma;
					bestk = k;
				}
			}
		}
		safePrintln("Chosen k: " + bestk + " Chosen sigma: " + bestSigma);
		safePrintln("with accuracy: " + bestAccuracy);

		return new float[] {bestk, (float)bestSigma};
	}

	public int tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet) throws FileNotFoundException {
		// outputs a k that is tuned for precision based on a training and tuning set
		safePrintln("Tuning k...");
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
			safePrintln("Avg Precision with k value " + k + ": " + avgPrecision);
			if (avgPrecision > bestPrecision) {
				// save best values for k and average precision
				bestPrecision = avgPrecision;
				bestk = k;
			}
		}
		safePrintln("Chosen k: " + bestk);
		return bestk;
	}

	public float testFold(ArrayList<Node> trainingSet, ArrayList<Node> testSet, int k) throws FileNotFoundException {
		// tests a fold, returns the average F1 score across all classes
		safePrintln("Conducting test on testSet...");
		safePrintln("testSet size: " + testSet.size());
		if (trainingSet.size() < k) {
			safePrintln("training set less than k. Reducing k...");
			k = (int)Math.sqrt(trainingSet.size());
		}
		KNearestNeighbor knn = new KNearestNeighbor();
		ArrayList<Integer[]> results = new ArrayList<>();

		for (Node example : testSet) {
			int guess = knn.nearestNeighborClassification(example, trainingSet, k);
			int real = (int) example.getId();
			Integer[] result = {guess, real};
			results.add(result);
		}

		Precision precision = new Precision(results);
		Recall recall = new Recall(results);
		ArrayList<Integer> classes = precision.getClasses();

		float avgF1 = 0;
		for(int _class: classes){
			precision.setTrueAndFalsePositives(_class);
			int ptp = precision.getTruePositives();
			int pfp = precision.getFalsePositives();
			double precisionResult = precision.findPrecision();
			safePrintln("Precision of class " + _class + "\tTrue Positives: " + ptp + "\tFalse Positives: " + pfp + "\tPrecision: " + precisionResult);

			recall.setTruePositivesAndFalseNegatives(_class);
			int rtp = recall.getTruePositives();
			int rfn = recall.getFalseNegatives();
			double recallResult = recall.findRecall();
			F1Score f1score = new F1Score(precisionResult, recallResult);
			double f1Score = f1score.getF1Score();

			safePrintln("Recall of class " + _class + "\tTrue Positives: " + rtp + "\tFalse Negatives: " + rfn + "\tRecall: " + recallResult);
			safePrintln("F1 Score: " + f1Score);
			safePrintln("");

			avgF1 += f1Score;
		}
		avgF1 = avgF1/classes.size();
		return avgF1;
	}

	public float testFoldRegression(ArrayList<Node> trainingSet, ArrayList<Node> testSet, int k, float sigma, float threshold) throws FileNotFoundException {
		// test a fold using KNN for regression, return accuracy
		safePrintln("Conducting test on testSet...");
		safePrintln("trainingSet size: " + trainingSet.size());
		safePrintln("testSet size: " + testSet.size());
		if (trainingSet.size() < k) {
			safePrintln("training set less than k. Reducing k...");
			k = testSet.size();
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
		safePrintln("Accuracy of Fold:" + accuracy);
		return accuracy;
	}

	public void crossValidation(ArrayList<Node> nodes) throws FileNotFoundException
	{
		TrainingGroups groups = new TrainingGroups(nodes);

		// running totals of sums of F1 scores for averaging at the end
		float F1KNN = 0;
		float F1EKNN = 0;
		float F1CKNN = 0;
		float F1KMEANS = 0;
		float F1PAM = 0;

		for (int i = 0; i < 10; i++) {
			safePrintln("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();
			ArrayList<Node> testSet = groups.getTestSet();
			ArrayList<Node> tuningSet = groups.getTuningSet();

			int k = tune(trainingSet, tuningSet);	// find best k for given training and tuning set

			safePrintln("Testing KNN...");
			F1KNN += testFold(trainingSet, testSet, k);		// test a single fold

			safePrintln("Testing Edited KNN...");
			EditedKNN EKNN = new EditedKNN(-1, this);	// -1 is for classification
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			F1EKNN += testFold(editedTrainingSet, testSet, k);

			safePrintln("Testing Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN(-1);	// -1 is for classification
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			F1CKNN = testFold(condensedTrainingSet, testSet, k);
			int kCluster = editedTrainingSet.size();	// set number of clusters to number of points returned from condensing


			// Clustering
			safePrintln("KMeansClustering...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			safePrintln("Testing KNN with Centroids as training set...");
			F1KMEANS += testFold(kmc.getNearestToCentroids(), testSet, k);	// test fold using centroids as training set

			safePrintln("PAMClustering...");
			safePrintln("Number of clusters chosen: " + kCluster);
			PAMClustering pam = new PAMClustering(kCluster, nodes, this);
			safePrintln("Testing KNN with Medoids as training set...");
			F1PAM += testFold(pam.getMedoids(), testSet, k);	// test fold using medoids as training set

			groups.iterateTestSet();
		}
		// Average F1 scores
		F1KNN = F1KNN / 10;
		F1EKNN = F1EKNN / 10;
		F1CKNN = F1CKNN / 10;
		F1KMEANS = F1KMEANS / 10;
		F1PAM = F1PAM / 10;

		safePrintln("Total Results:");
		safePrintln("KNN Average F1 Score: " + F1KNN);
		safePrintln("EKNN Average F1 Score: " + F1EKNN);
		safePrintln("CKNN Average F1 Score: " + F1CKNN);
		safePrintln("KMEANS + KNN Average F1 Score: " + F1KMEANS);
		safePrintln("PAM + KNN Average F1 Score: " + F1PAM);
		/*for (Node node : Objects.requireNonNull(nodes)) {
			safePrintln(node.getId() + Arrays.toString(node.getData()));
		}*/
	}

	public void crossValidationRegression(ArrayList<Node> nodes) throws FileNotFoundException
	{
		TrainingGroups groups = new TrainingGroups(nodes);
		float threshold = 0.08f;//chosen based on multiple tests

		for (int i = 0; i < 10; i++) {
			safePrintln("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();
			ArrayList<Node> testSet = groups.getTestSet();
			ArrayList<Node> tuningSet = groups.getTuningSet();

			float[] tunedParameters = tuneRegression(trainingSet, tuningSet, threshold);	// find best k for given training and tuning set

			int k = (int)tunedParameters[0];
			float sigma = tunedParameters[1];

			safePrintln("\nTesting KNN...");
			testFoldRegression(trainingSet, testSet, k, sigma, threshold);		// test a single fold

			safePrintln("\nTesting Edited KNN...");
			EditedKNN EKNN = new EditedKNN(threshold, this);
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			testFoldRegression(editedTrainingSet, testSet, k, sigma, threshold);
			safePrintln("Edited KNN training set size: " + editedTrainingSet.size());

			safePrintln("\nTesting Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN(threshold);
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			testFoldRegression(condensedTrainingSet, testSet, k, sigma, threshold);
			safePrintln("Condensed KNN training set size: " + condensedTrainingSet.size());

			// Clustering
			int kCluster = editedTrainingSet.size(); // set number of clusters to number of points returned from editing
			safePrintln("\nKMeansClustering...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			safePrintln("Testing KNN with Centroids as training set...");
			testFold(kmc.getNearestToCentroids(), testSet, k);	// test fold using centroids as training set

			safePrintln("\nPAMClustering...");
			safePrintln("Number of clusters chosen: " + kCluster);
			PAMClustering pam = new PAMClustering(kCluster, nodes, this);
			safePrintln("Testing KNN with Medoids as training set...");
			testFold(pam.getMedoids(), testSet, k);	// test fold using medoids as training set*/
			groups.iterateTestSet();
		}
	}
	
	public void safePrintln(String s) throws FileNotFoundException
	{
		synchronized (System.out) {
			System.out.println(s);
		}
	}
	
	
	public static void main(String[] args) throws FileNotFoundException
	{
		//use these if you want to run a single data set
		Driver test = new Driver("segmentation");
		test.start();
		

		//use these if you want to run all the data sets  "house-votes-84",
		/*
		String[] files = {"abalone", "forestfires", "glass", "machine", "segmentation"};
		for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			d.start();//Starts a new thread
		}
		*/
	}
}