import LossFunctions.F1Score;
import LossFunctions.Precision;
import LossFunctions.Recall;

import javax.swing.*;
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
		//knn examples
		/*double[] exdp1 = {1.0, 5.0, 10.0};
		double[] exdp2 = {2.0, 2.0, 7.0};
		double[] exdp3 = {3.0, 4.0, 4.0};
		double[] exdp4 = {3.0, 1.0, 3.0};
		double[] exdp5 = {3.0, 9.0, 8.0};

		double[] exdp = {0.0, 5.0, 8.0};

		double[][] exampleDatapoints = {exdp1, exdp2, exdp3, exdp4, exdp5};

		float[] data1 = {5, 4};
		float[] data2 = {7, 4};
		float[] data3 = {7, 4};
		float[] data4 = {5, 4};
		float[] data5 = {7, 4};
		float[] data6 = {5, 4};

		float[] datax = {8, 4};

		Node node1 = new Node(1, data1, 0);
		Node node2 = new Node(2, data2, 0);
		Node node3 = new Node(2, data3, 0);
		Node node4 = new Node(4, data4, 0);
		Node node5 = new Node(5, data5, 0);
		Node node6 = new Node(6, data6, 0);

		Node nodex = new Node(0, datax, 0);

		ArrayList<Node> exnodes = new ArrayList<>();
		exnodes.add(node1);
		exnodes.add(node2);
		exnodes.add(node3);
		exnodes.add(node4);
		exnodes.add(node5);
		exnodes.add(node6);


		KNearestNeighbor knn = new KNearestNeighbor("classification", 6, exnodes, nodex);

		double nn = knn.getNearestNeighbors(nodex, exnodes);
		System.out.println(nn);

		float[] data1 = {5, 4, 1};
		float[] data2 = {7, 4, 6};
		float[] data3 = {7, 4, 2};
		float[] data4 = {5, 4, 6};
		float[] data5 = {7, 4, 4};
		float[] data6 = {5, 4, 4};

		float[] datax = {8, 4, 6};

		Node node1 = new Node(0, data1, 2);
		Node node2 = new Node(0, data2, 2);
		Node node3 = new Node(0, data3, 2);
		Node node4 = new Node(0, data4, 2);
		Node node5 = new Node(0, data5, 2);
		Node node6 = new Node(0, data6, 2);

		Node nodex = new Node(0, datax, 0);

		ArrayList<Node> enodes = new ArrayList<>();
		enodes.add(node1);
		enodes.add(node2);
		enodes.add(node3);
		enodes.add(node4);
		enodes.add(node5);
		enodes.add(node6);

		KNearestNeighbor knnreg = new KNearestNeighbor();
		knnreg.nearestNeighborsRegression(nodex, enodes, 2, 5, 0.1);*/

		
		//parse out the data in the file
		System.out.println(filePath);
		Parser p = new Parser();
		ArrayList<Node> nodes = null;
		switch (filePath) {
			case "house-votes-84" -> {
				nodes = p.votesParser(fileStart + filePath + fileEnd);
				System.out.println("Done Votes");
			}
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				System.out.println("Done Glass");
			}
			case "abalone" -> {
				nodes = p.abaloneParser(fileStart + filePath + fileEnd);
				int n = 14;
				Node datapoint = nodes.get(n);
				nodes.remove(n);

				//testing knn regression
				KNearestNeighbor knnre = new KNearestNeighbor();
				knnre.nearestNeighborsRegression(datapoint, nodes, 7, 20, 0.1);

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

				System.out.println("Done Forest Fires");
			}
			case "machine" -> {
				nodes = p.machineParser(fileStart + filePath + fileEnd);
				int n = 13;
				Node datapoint = nodes.get(n);
				nodes.remove(n);

				//testing knn regression
				KNearestNeighbor knnrr = new KNearestNeighbor();
				knnrr.nearestNeighborsRegression(datapoint, nodes, 9, 5, 0.1);

				System.out.println("Done Machine");
			}
			case "segmentation" -> {
				nodes = p.segmentationParser(fileStart + filePath + fileEnd);
				System.out.println("Done Segmentation");
			}
			case "simpleData" -> {
				nodes = p.simpleParser(fileStart + filePath + fileEnd);
				System.out.println("Done Simple");
			}
			default -> System.out.println("Bad file path: " + filePath);
		}
		
		//System.out.println("REAL CLASS");
		//System.out.println(newNodes.get(0).getId());

		//knn.getNearestNeighbors(newNodes.get(0), newNodes);

		/*
		// verify clustering works
		// construct KMeans, which also computes all the clusters
		KMeansClustering kMeans = new KMeansClustering(5, nodes);

		//remove this later - loop to keep running until an empty cluster is assigned to test the code that handles it
		while (!kMeans.emptyClusterCreated) {
			kMeans = new KMeansClustering(5, nodes);
		}

		ArrayList<Node> centroids = kMeans.getCentroids();
		for(Node node : centroids) {
			System.out.println(node.getId() + " " + Arrays.toString(node.getData()));
		}
		ArrayList<ArrayList<Node>> clusters = kMeans.getClusters();
		for (int i = 0; i < clusters.size(); i++) {
			System.out.println("Cluster " + i + ": ");
			ArrayList<Node> cluster = clusters.get(i);
			for (Node node : cluster) {
				float[] data = node.getData();
				for (int j = 0; j < data.length; j++) {
					System.out.print(data[j] + " | ");
				}
				System.out.println();
			}
		}*/

		/*
		// verify clustering works
		// construct KMeans, which also computes all the clusters
		PAMClustering pam = new PAMClustering(5, nodes);

		//remove this later - loop to keep running until an empty cluster is assigned to test the code that handles it
		while (!pam.emptyClusterCreated) {
			pam = new PAMClustering(5, nodes);
		}

		ArrayList<Node> medoids = pam.getMedoids();
		for(Node node : medoids) {
			System.out.println(node.getId() + " " + Arrays.toString(node.getData()));
		}
		ArrayList<ArrayList<Node>> clusters = pam.getClusters();
		for (int i = 0; i < clusters.size(); i++) {
			System.out.println("Cluster " + i + ": ");
			ArrayList<Node> cluster = clusters.get(i);
			for (Node node : cluster) {
				float[] data = node.getData();
				for (int j = 0; j < data.length; j++) {
					System.out.print(data[j] + " | ");
				}
				System.out.println();
			}
		}
		*/
		
		//crossValidation(nodes);//runs on the data as it appears in the .data files.
	}
	
	public void visualize(ArrayList<Node> nodes1, String title)
	{
		VisualizeData vd1 = new VisualizeData(nodes1);
		JFrame f1 = new JFrame(title);
		f1.add(vd1);
		f1.setSize(600, 600);
		f1.setLocationRelativeTo(null);
		f1.setVisible(true);
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
			System.out.println("Test k value: " + k);
			for (Node example : tuningSet) {
				int real = (int) example.getId();
				//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
				int guess = knn.nearestNeighborClassification(example, trainingSet, k);
				System.out.println("For attributes: " + Arrays.toString(example.getData()) + " Guess: " + guess + " Real Class: " + real + "\n");
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

		KNearestNeighbor knn = new KNearestNeighbor();
		ArrayList<Integer[]> results = new ArrayList<>();
		for (Node example : testSet) {
			int guess;
			int real = (int) example.getId();
			//System.out.println("\nAttempting to classify with attributes: " + Arrays.toString(example.getData()));
			guess = (int)knn.nearestNeighborClassification(example, trainingSet, k);
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
			EditedKNN EKNN = new EditedKNN();
			ArrayList<Node> editedTrainingSet = EKNN.editSet(trainingSet, k);
			testFold(editedTrainingSet, testSet, k);

			System.out.println("Testing Condensed KNN...");
			CondensedKNN CKNN = new CondensedKNN();
			ArrayList<Node> condensedTrainingSet = CKNN.condenseSet(trainingSet);
			testFold(condensedTrainingSet, testSet, k);
			int kCluster = condensedTrainingSet.size();	// set number of clusters to number of points returned from condensing
			// Clustering
			System.out.println("Testing KNN with Centroids as training set...");
			KMeansClustering kmc = new KMeansClustering(kCluster, nodes);
			testFold(kmc.getNearestToCentroids(), testSet, k);	// test fold using centroids as training set

			System.out.println("Testing KNN with Medoids as training set...");
			PAMClustering pam = new PAMClustering(kCluster, nodes);
			testFold(pam.getMedoids(), testSet, k);	// test fold using medoids as training set

			groups.iterateTestSet();
		}
		/*for (Node node : Objects.requireNonNull(nodes)) {
			System.out.println(node.getId() + Arrays.toString(node.getData()));
		}*/
	}

	public static void main(String[] args) {
		String[] files = {"abalone", "forestfires", "glass", "house-votes-84", "machine", "segmentation"};
		
		//use these if you want to run a single data set
		Driver test = new Driver("forestfires");//simpleData
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