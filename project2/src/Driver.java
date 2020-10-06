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
		boolean isRegression = false;
		switch (filePath) {//since each dataset is different it needs its own parse function
			case "house-votes-84" -> {
				nodes = p.votesParser(fileStart + filePath + fileEnd);
				Node datapoint = nodes.get(0);
				nodes.remove(0);
				KNearestNeighbor knn = new KNearestNeighbor();
				int chosenClass = knn.nearestNeighborClassification(datapoint, nodes, 5);
				System.out.println(chosenClass);
				System.out.println("Done Votes");
			}
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				Node datapoint = nodes.get(0);
				nodes.remove(0);
				KNearestNeighbor knn = new KNearestNeighbor();
				int chosenClass = knn.nearestNeighborClassification(datapoint, nodes, 5);
				System.out.println("Done Glass");
			}
			case "abalone" -> {
				nodes = p.abaloneParser(fileStart + filePath + fileEnd);
				int n = 14;
				Node datapoint = nodes.get(n);
				nodes.remove(n);

				//testing knn regression
				KNearestNeighbor knnre = new KNearestNeighbor();
				double value = knnre.nearestNeighborsRegression(datapoint, nodes, 7, 5, 0.1);
				float denormVal = p.deNormAttr((float) value, 7);
				//System.out.println(denormVal);

				isRegression = true;
				System.out.println("Done Abalone");
			}
			case "forestfires" -> {
				nodes = p.firesParser(fileStart + filePath + fileEnd);
				int n = 213;
				Node datapoint = nodes.get(n);
				nodes.remove(n);

				//testing knn regression
				KNearestNeighbor knnr = new KNearestNeighbor();
				knnr.nearestNeighborsRegression(datapoint, nodes, 12, 5, 0.1);

				isRegression = true;
				System.out.println("Done Forest Fires");
			}
			case "machine" -> {
				nodes = p.machineParser(fileStart + filePath + fileEnd);
				int n = 13;
				Node datapoint = nodes.get(n);
				nodes.remove(n);

				System.out.println(p.deNormAttr(datapoint.getData()[9], 9));
				//testing knn regression
				KNearestNeighbor knnrr = new KNearestNeighbor();
				double value = knnrr.nearestNeighborsRegression(datapoint, nodes, 9, 5, 0.1);

				System.out.println(p.deNormAttr((float)value, 9));
				knnrr.nearestNeighborsRegression(datapoint, nodes, 9, 5, 0.1);

				isRegression = true;
				System.out.println("Done Machine");
			}
			case "segmentation" -> {
				nodes = p.segmentationParser(fileStart + filePath + fileEnd);
				Node datapoint = nodes.get(0);
				nodes.remove(0);
				KNearestNeighbor knn = new KNearestNeighbor();
				int chosenClass = knn.nearestNeighborClassification(datapoint, nodes, 5);
				System.out.println(chosenClass);
				System.out.println("Done Segmentation");
			}
			default -> System.out.println("Bad file path: " + filePath);
		}
		if (isRegression) {
			//crossValidationRegression(nodes);
		}
		else {
			//crossValidation(nodes);
		}
	}

	public float[] tuneRegression(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet, double threshold) {
		System.out.println("Tuning k and sigma...");
		double[] sigmas = {0.1f, 0.01f, 0.001f, 0.0001f, 0.00001f};

		int bestk = 0;
		double bestsigma = 0.1f;
		double bestAccuracy = 0;
		ArrayList<Double[]> results = new ArrayList<>();

		KNearestNeighbor knn = new KNearestNeighbor();
		for (int k = (int)Math.sqrt(trainingSet.size()) - 2; k <= (int)Math.sqrt(trainingSet.size()) + 2; k++) {
			for (double sigma : sigmas) {
				//loop through 5 values of k centered around sqrt of the training set size
				//System.out.println("Test k value: " + k + " sigma: " + sigma);
				for (Node example : tuningSet) {
					double real = example.getData()[example.getIgnoredAttr()];
					//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
					double guess = knn.nearestNeighborsRegression(example, trainingSet, example.getIgnoredAttr(), k, sigma);
					//System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess + " Real Class: " + real + "\n");
					Double[] result = {guess, real};
					results.add(result);
				}
				int correct = 0;
				for (int i = 0; i < results.size(); i++) {
					if (Math.abs(results.get(i)[0] - results.get(i)[1]) < threshold) {
						// check if returned result is within the threshold value from the real value
						correct++;
					}
				}
				float accuracy = (float)correct/results.size();
				if (accuracy > bestAccuracy) {
					// save best values for k and average precision
					bestAccuracy = accuracy;
					bestsigma = sigma;
					bestk = k;
				}
			}
		}
		System.out.println("Chosen k: " + bestk + " Chosen sigma: " + bestsigma);
		System.out.println("with accuracy: " + bestAccuracy);

		float[] tunedParameters = {bestk, (float)bestsigma};
		return tunedParameters;
	}

	public int tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet) {
		// outputs a k that is tuned for precision based on a training and tuning set
		System.out.println("Tuning k...");
		int bestk = 0;
		double bestPrecision = 0;
		ArrayList<Integer[]> results = new ArrayList<>();

		KNearestNeighbor knn = new KNearestNeighbor();
		for (int k = (int)Math.sqrt(trainingSet.size()) - 2; k <= (int)Math.sqrt(trainingSet.size()) + 2; k++) {
			//loop through 5 values of k centered around sqrt of the training set size
			//System.out.println("Test k value: " + k);
			for (Node example : tuningSet) {
				int real = (int) example.getId();
				//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
				int guess = knn.nearestNeighborClassification(example, trainingSet, k);
				//System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess + " Real Class: " + real + "\n");
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

	public void testFold(ArrayList<Node> trainingSet, ArrayList<Node> testSet, int k) {
		System.out.println("Conducting test on testSet...");
		System.out.println("testSet size: " + testSet.size());
		if (trainingSet.size() < k) {
			System.out.println("training set less than k. Reducing k...");
			k = (int)Math.sqrt(trainingSet.size());
		}
		KNearestNeighbor knn = new KNearestNeighbor();
		ArrayList<Integer[]> results = new ArrayList<>();
		for (Node example : testSet) {
			int guess;
			int real = (int) example.getId();
			//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
			guess = knn.nearestNeighborClassification(example, trainingSet, k);
			//System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess + " Real Class: " + real + "\n");
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
			System.out.println("Precision of class " + _class + "\tTrue Positives: " + ptp + "\tFalse Positives: "
					+ pfp + "\tPrecision: " + precisionResult);

			recall.setTruePositivesAndFalseNegatives(_class);
			int rtp = recall.getTruePositives();
			int rfn = recall.getFalseNegatives();
			double recallResult = recall.findRecall();
			F1Score f1score = new F1Score(precisionResult, recallResult);
			double f1Score = f1score.getF1Score();

			System.out.println("Recall of class " + _class + "\tTrue Positives: " + rtp + "\tFalse Negatives: " + rfn + "\tRecall: " + recallResult);
			System.out.println("F1 Score: " + f1Score);
			System.out.println();
		}
	}

	public void testFoldRegression(ArrayList<Node> trainingSet, ArrayList<Node> testSet, int k, float sigma, float threshold) {
		System.out.println("Conducting test on testSet...");
		System.out.println("trainingSet size: " + trainingSet.size());
		System.out.println("testSet size: " + testSet.size());
		if (trainingSet.size() < k) {
			System.out.println("training set less than k. Reducing k...");
			k = testSet.size();
		}
		KNearestNeighbor knn = new KNearestNeighbor();
		ArrayList<double[]> results = new ArrayList<>();
		for (Node example : testSet) {
			double real = example.getData()[example.getIgnoredAttr()];
			//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
			double guess = knn.nearestNeighborsRegression(example, trainingSet, example.getIgnoredAttr(), k, sigma);
			//System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess + " Real Value: " + real + "\n");
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
		System.out.println("Accuracy: " + accuracy);
	}

	public void crossValidation(ArrayList<Node> nodes)
	{
		TrainingGroups groups = new TrainingGroups(nodes);

		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();
			ArrayList<Node> testSet = groups.getTestSet();
			ArrayList<Node> tuningSet = groups.getTuningSet();

			int k = tune(trainingSet, tuningSet);	// find best k for given training and tuning set

			System.out.println("Testing KNN...");
			testFold(trainingSet, testSet, k);		// test a single fold

			System.out.println("Testing Edited KNN...");
			EditedKNN EKNN = new EditedKNN(-1);	// -1 is for classification
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			testFold(editedTrainingSet, testSet, k);
			System.out.println("Edited KNN set size: " + editedTrainingSet.size());

			System.out.println("Testing Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN(-1);	// -1 is for classification
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			testFold(condensedTrainingSet, testSet, k);
			int kCluster = editedTrainingSet.size();	// set number of clusters to number of points returned from condensing
			System.out.println("Condensed KNN set: " + condensedTrainingSet.size());
			// Clustering
			System.out.println("KMeansClustering...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			System.out.println("Testing KNN with Centroids as training set...");
			testFold(kmc.getNearestToCentroids(), testSet, k);	// test fold using centroids as training set

			System.out.println("PAMClustering...");
			System.out.println("Number of clusters chosen: " + kCluster);
			PAMClustering pam = new PAMClustering(kCluster, nodes);
			System.out.println("Testing KNN with Medoids as training set...");
			testFold(pam.getMedoids(), testSet, k);	// test fold using medoids as training set

			groups.iterateTestSet();
		}
		/*for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}*/
	}

	public void crossValidationRegression(ArrayList<Node> nodes)
	{
		TrainingGroups groups = new TrainingGroups(nodes);
		float threshold = 0.08f;

		for (int i = 0; i < 10; i++) {
			System.out.println("Training set: " + i);
			ArrayList<Node> trainingSet = groups.getTrainingSet();
			ArrayList<Node> testSet = groups.getTestSet();
			ArrayList<Node> tuningSet = groups.getTuningSet();

			float[] tunedParameters = tuneRegression(trainingSet, tuningSet, threshold);	// find best k for given training and tuning set

			int k = (int)tunedParameters[0];
			float sigma = tunedParameters[1];

			//TODO: implement methods with regression enabled
			System.out.println("\nTesting KNN...");
			testFoldRegression(trainingSet, testSet, k, sigma, threshold);		// test a single fold

			System.out.println("\nTesting Edited KNN...");
			EditedKNN EKNN = new EditedKNN(threshold);
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			testFoldRegression(editedTrainingSet, testSet, k, sigma, threshold);
			System.out.println("Edited KNN training set size: " + editedTrainingSet.size());

			System.out.println("\nTesting Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN(threshold);
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			testFoldRegression(condensedTrainingSet, testSet, k, sigma, threshold);
			System.out.println("Condensed KNN training set size: " + condensedTrainingSet.size());

			// Clustering
			int kCluster = editedTrainingSet.size(); // set number of clusters to number of points returned from editing
			System.out.println("\nKMeansClustering...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			System.out.println("Testing KNN with Centroids as training set...");
			testFold(kmc.getNearestToCentroids(), testSet, k);	// test fold using centroids as training set

			System.out.println("\nPAMClustering...");
			System.out.println("Number of clusters chosen: " + kCluster);
			PAMClustering pam = new PAMClustering(kCluster, nodes);
			System.out.println("Testing KNN with Medoids as training set...");
			testFold(pam.getMedoids(), testSet, k);	// test fold using medoids as training set*/
			groups.iterateTestSet();
		}
		/*for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}*/
	}

	public static void main(String[] args) {
		String[] files = {"abalone", "forestfires", "glass", "house-votes-84", "machine", "segmentation"};
		
		//use these if you want to run a single data set
		Driver test = new Driver("abalone");
		test.start();
		
		//use these if you want to run all the data sets
		/*for (String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			//System.out.println("\n********************\n" + file + "\n********************\n");
			d.start();//Starts a new thread
		}*/
	}
}