import java.util.ArrayList;
import java.util.HashMap;

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
		int numClasses = 0;
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

		//TODO: Get rid of ignoredatt value and just place the variable of interest in ID, and not in data[]

		// TODO: This is just a bandaid. Should edit the response variable out of the data and have a way to normalize id
		// Parse the problematic feature out of the array and into the id, or compile classes into an array
		ArrayList<Node> parsedNodes = new ArrayList<>();
		double[] classes = new double[]{};
		if (isRegression) {
			for (Node node : nodes) {
				int ignoredAttr = node.getIgnoredAttr();
				double normalizedID = node.getData()[ignoredAttr];

				// Populate new data array without the ignored attribute
				double[] newData = new double[node.getData().length - 1];
				for (int i = 0; i < node.getData().length; i++) {
					if (i < ignoredAttr) {
						newData[i] = node.getData()[i];
					} else if (i == ignoredAttr) {
						continue;    // skip ignored attribute
					} else {    // past ignored attribute, offset index when assigning to new data
						newData[i - 1] = node.getData()[i];
					}
				}
				parsedNodes.add(new Node(normalizedID, newData, 128395));
			}
		}
		else {
			classes = getClasses(nodes);
		}

		// Train and test using the real test set

		TrainingGroups groups = new TrainingGroups(parsedNodes);

		//Network net = new Network(parsedNodes.get(0).getData().length, new int[]{}, classes, !isRegression);	// 0 layers
		//Network net = new Network(parsedNodes.get(0).getData().length, new int[]{parsedNodes.get(0).getData().length-1}, classes, !isRegression);	// 1 layer
		Network net = new Network(parsedNodes.get(0).getData().length, new int[]{parsedNodes.get(0).getData().length-1,
				parsedNodes.get(0).getData().length-2}, classes, !isRegression);	// 2 layers

		BackPropagation bp = new BackPropagation(net, 10000, 0.0001, 0.1);

		ArrayList<Node> trainingSet = groups.getTrainingSet();
		bp.trainNetwork(trainingSet);
		System.out.println("After Training the network...");
		if (isRegression) {
			for (int i = 0; i < trainingSet.size(); i++) {
				ArrayList<Neuron> output = net.feedForward(trainingSet.get(i).getData());
				for (Neuron neuron : output) {
					System.out.println("Output: " + neuron.getValue() + " | Original: " + trainingSet.get(i).getId());
				}
			}
		}
		else {
			HashMap<Neuron, Double> classMap = net.getOutputToClass();
			for (int i = 0; i < trainingSet.size(); i++) {
				ArrayList<Neuron> output = net.feedForward(trainingSet.get(i).getData());
				System.out.println("\nFor example with class " + trainingSet.get(i).getId() + ":");
				double highestValue = 0;
				double mostLikelyClass = 0;
				for (Neuron neuron : output) {
					double neuronClass = classMap.get(neuron);
					double neuronValue = neuron.getValue();
					System.out.println("Output from Neuron corresponding to class " + neuronClass + ": " + neuronValue);
					if (neuronValue > highestValue) {	// find most likely class
						highestValue = neuronValue;
						mostLikelyClass = neuronClass;
					}
				}
				System.out.println("Predicted class: " + mostLikelyClass);
			}
		}

	}

	public double[] getClasses(ArrayList<Node> dataset) {
		double[] classes = new double[]{};	// holds all classes found in a dataset

		// For classification, parse the dataset and compile the classes into an array
		ArrayList<Double> classList = new ArrayList<>();
		for (Node node : dataset) {	// Populate list with nodes
			if (!classList.contains(node.getId())) {
				classList.add(node.getId());
			}
		}
		classes = new double[classList.size()];
		for (int i = 0; i < classes.length; i++) {
			classes[i] = classList.get(i);	// copy arraylist to the double array
		}

		return classes;
	}

	public void runExperiment(ArrayList<Node> dataset, double[] classes) {
		double[] learningRates = new double[]{0.001, 0.01, 0.1, 1};
		double[] momentums = new double[]{0, 0.001, 0.01, 0.1, 1};	// includes 0 for no momentum
		//TODO: may just shift this to runwithtuning to automatically try values based on size of input layer
		int[] hiddenLayerNodeNums = new int[]{1, 2, 3, 4, 5, 6};

		TrainingGroups folds = new TrainingGroups(dataset);
		ArrayList<Node> tuningSet = folds.getTrainingSet();
		RunWithTuning tuner = new RunWithTuning(tuningSet, learningRates, momentums, hiddenLayerNodeNums);
		tuner.tune();

		for (int i = 0; i < 10; i++) {

		}
	}

	public static void main(String[] args)
	{
		//use these if you want to run a single data set
		Driver test = new Driver("forestfires");
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