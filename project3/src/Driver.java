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
			default -> {
				System.out.println("Bad file path: " + filePath);
			}
		}
		if (isRegression) {
			crossValidationRegression(nodes);
		}
		else {
			crossValidation(nodes);
		}
	}

	public float[] tuneRegression(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet, double threshold){
		System.out.println("Tuning k and sigma...");
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
		System.out.println("Chosen k: " + bestk + " Chosen sigma: " + bestSigma);
		System.out.println("with accuracy: " + bestAccuracy);

		return new float[] {bestk, (float)bestSigma};
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

	public float testFold(ArrayList<Node> trainingSet, ArrayList<Node> testSet, int k){
		// tests a fold, returns the average F1 score across all classes
		System.out.println("Conducting test on testSet...");
		System.out.println("testSet size: " + testSet.size());
		if (trainingSet.size() < k) {
			System.out.println("training set less than k. Reducing k...");
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

			avgF1 += f1Score;
		}
		avgF1 = avgF1/classes.size();
		return avgF1;
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

	public void crossValidation(ArrayList<Node> nodes)
	{
		TrainingGroups groups = new TrainingGroups(nodes);

		// running totals of sums of F1 scores for averaging at the end
		float F1KNN = 0;
		float F1EKNN = 0;
		float F1CKNN = 0;
		float F1KMEANS = 0;
		float F1PAM = 0;
		
		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();
			ArrayList<Node> testSet = groups.getTestSet();
			ArrayList<Node> tuningSet = groups.getTuningSet();

			int k = tune(trainingSet, tuningSet);	// find best k for given training and tuning set

			System.out.println("Testing KNN...");
			F1KNN += testFold(trainingSet, testSet, k);		// test a single fold

			System.out.println("Testing Edited KNN...");
			EditedKNN EKNN = new EditedKNN(-1);	// -1 is for classification
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			F1EKNN += testFold(editedTrainingSet, testSet, k);

			System.out.println("Testing Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN(-1);	// -1 is for classification
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			F1CKNN = testFold(condensedTrainingSet, testSet, k);
			int kCluster = editedTrainingSet.size();	// set number of clusters to number of points returned from condensing


			// Clustering
			System.out.println("KMeansClustering...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			System.out.println("Testing KNN with Centroids as training set...");
			F1KMEANS += testFold(kmc.getNearestToCentroids(), testSet, k);	// test fold using centroids as training set

			System.out.println("PAMClustering...");
			System.out.println("Number of clusters chosen: " + kCluster);
			PAMClustering pam = new PAMClustering(kCluster, nodes);
			System.out.println("Testing KNN with Medoids as training set...");
			F1PAM += testFold(pam.getMedoids(), testSet, k);	// test fold using medoids as training set

			groups.iterateTestSet();
		}
		// Average F1 scores
		F1KNN = F1KNN / 10;
		F1EKNN = F1EKNN / 10;
		F1CKNN = F1CKNN / 10;
		F1KMEANS = F1KMEANS / 10;
		F1PAM = F1PAM / 10;

		System.out.println("Total Results:");
		System.out.println("KNN Average F1 Score: " + F1KNN);
		System.out.println("EKNN Average F1 Score: " + F1EKNN);
		System.out.println("CKNN Average F1 Score: " + F1CKNN);
		System.out.println("KMEANS + KNN Average F1 Score: " + F1KMEANS);
		System.out.println("PAM + KNN Average F1 Score: " + F1PAM);
	}

	public void crossValidationRegression(ArrayList<Node> nodes)
	{
		TrainingGroups groups = new TrainingGroups(nodes);
		float threshold = 0.08f;//chosen based on multiple tests

		// running totals of sums of accuracies for averaging at the end
		float accKNN = 0;
		float accEKNN = 0;
		float accCKNN = 0;
		float accKMEANS = 0;
		float accPAM = 0;

		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();
			ArrayList<Node> testSet = groups.getTestSet();
			ArrayList<Node> tuningSet = groups.getTuningSet();

			float[] tunedParameters = tuneRegression(trainingSet, tuningSet, threshold);	// find best k for given training and tuning set

			int k = (int)tunedParameters[0];
			float sigma = tunedParameters[1];

			System.out.println("\nTesting KNN...");
			accKNN += testFoldRegression(trainingSet, testSet, k, sigma, threshold);		// test a single fold

			System.out.println("\nTesting Edited KNN...");
			EditedKNN EKNN = new EditedKNN(threshold);
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			accEKNN += testFoldRegression(editedTrainingSet, testSet, k, sigma, threshold);
			System.out.println("Edited KNN training set size: " + editedTrainingSet.size());

			System.out.println("\nTesting Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN(threshold);
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			accCKNN += testFoldRegression(condensedTrainingSet, testSet, k, sigma, threshold);
			System.out.println("Condensed KNN training set size: " + condensedTrainingSet.size());

			// Clustering
			int kCluster = editedTrainingSet.size(); // set number of clusters to number of points returned from editing
			System.out.println("\nKMeansClustering...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			System.out.println("Testing KNN with Centroids as training set...");
			accKMEANS += testFoldRegression(kmc.getNearestToCentroids(), testSet, k, sigma, threshold);	// test fold using centroids as training set

			System.out.println("\nPAMClustering...");
			System.out.println("Number of clusters chosen: " + kCluster);
			if (kCluster > 50) {
				System.out.println("Too high, changing number of clusters to 50");
				kCluster = 50;
			}
			PAMClustering pam = new PAMClustering(kCluster, nodes);
			System.out.println("Testing KNN with Medoids as training set...");
			accPAM += testFoldRegression(pam.getMedoids(), testSet, k, sigma, threshold);	// test fold using medoids as training set*/
			groups.iterateTestSet();
		}
		// Average F1 scores
		accKNN = accKNN / 10;
		accEKNN = accEKNN / 10;
		accCKNN = accCKNN / 10;
		accKMEANS = accKMEANS / 10;
		accPAM = accPAM / 10;

		System.out.println("Total Results:");
		System.out.println("KNN Average Accuracy: " + accKNN);
		System.out.println("EKNN Average Accuracy: " + accEKNN);
		System.out.println("CKNN Average Accuracy: " + accCKNN);
		System.out.println("KMEANS + KNN Average Accuracy: " + accKMEANS);
		System.out.println("PAM + KNN Average Accuracy: " + accPAM);
	}
	
	
	public static void main(String[] args)
	{
		//use these if you want to run a single data set
		Driver test = new Driver("segmentation");
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