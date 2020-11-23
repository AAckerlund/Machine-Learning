import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Driver extends Thread//extending Thread allows for multithreading
{
	String fileStart = "dataSets/", fileEnd = ".data", filePath, dataPath, trainer;
	int hiddenLayers, fold;
	int[] hiddenLayerCount;
	
	public Driver(String filePath, int[] hiddenLayerCount, int fold, String trainer)
	{
		this.filePath = filePath;
		dataPath = filePath;
		this.hiddenLayers = hiddenLayerCount.length;
		this.fold = fold;
		this.hiddenLayerCount = hiddenLayerCount;
		this.trainer = trainer;
	}
	
	public void run() //the method that is called when a Thread starts
	{
		//parse out the data in the file
		System.out.println(filePath);
		Parser p = new Parser();
		ArrayList<Node> nodes = null;
		boolean isRegression = false;
		switch(filePath)
		{//since each dataset is different it needs its own parse function
			case "abalone" -> {
				nodes = p.abaloneParser(fileStart + filePath + fileEnd);
				filePath +=  "_" + hiddenLayers + "_" + fold;
				isRegression = true;
				Printer.println(filePath, "Done Abalone");
			}
			case "breast-cancer-wisconsin" -> {
				nodes = p.cancerParser(fileStart + filePath + fileEnd);
				filePath +=  "_" + hiddenLayers + "_" + fold;
				Printer.println(filePath, "Done Cancer");
			}
			case "forestfires" -> {
				nodes = p.firesParser(fileStart + filePath + fileEnd);
				filePath +=  "_" + hiddenLayers + "_" + fold;
				isRegression = true;
				Printer.println(filePath, "Done Forest Fires");
			}
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				filePath +=  "_" + hiddenLayers + "_" + fold;
				Printer.println(filePath, "Done Glass");
			}
			case "machine" -> {
				nodes = p.machineParser(fileStart + filePath + fileEnd);
				filePath +=  "_" + hiddenLayers + "_" + fold;
				isRegression = true;
				Printer.println(filePath, "Done Machine");
			}
			case "soybean-small" -> {
				nodes = p.beanParser(fileStart + filePath + fileEnd);
				filePath +=  "_" + hiddenLayers + "_" + fold;
				Printer.println(filePath, "Done Soybean");
			}
			default -> System.err.println("Bad file path: " + filePath);
		}
		
		Normalization.zNormalize(nodes);    // use z-normalization to normalize the nodes
		
		// Parse the problematic feature out of the array and into the id, or compile classes into an array
		ArrayList<Node> parsedNodes = new ArrayList<>();
		double[] classes = new double[] {};
		if(isRegression)
		{
			for(Node node : nodes)
			{
				int ignoredAttr = node.getIgnoredAttr();
				double normalizedID = node.getData()[ignoredAttr];
				
				// Populate new data array without the ignored attribute
				double[] newData = new double[node.getData().length - 1];
				for(int i = 0; i < node.getData().length; i++)
				{
					if(i < ignoredAttr)
					{
						newData[i] = node.getData()[i];
					}
					else if(i > ignoredAttr)
					{    // past ignored attribute, offset index when assigning to new data
						newData[i - 1] = node.getData()[i];
					}
				}
				parsedNodes.add(new Node(normalizedID, newData, 128395));
			}
		}
		else
		{
			classes = getClasses(nodes);
			parsedNodes = nodes;
		}

		/*
		// Check if calcNumWeights is working
		int numWeights = calcNumWeights(parsedNodes.get(0).getData().length, classes, !isRegression);
		System.out.println("NumWeights: " + numWeights + " | input: " + parsedNodes.get(0).getData().length +
				" | hidden layers: " + Arrays.toString(hiddenLayerCount) + " | output: " + classes.length);
		*/

		// Tune, Train and test using the real test set
		runExperiment(parsedNodes, classes, isRegression);
		System.out.println("Finished running experiment for " + filePath);
	}
	
	public double[] getClasses(ArrayList<Node> dataset)
	{
		double[] classes;    // holds all classes found in a dataset
		
		// For classification, parse the dataset and compile the classes into an array
		ArrayList<Double> classList = new ArrayList<>();
		for(Node node : dataset)
		{    // Populate list with nodes
			if(!classList.contains(node.getId()))
			{
				classList.add(node.getId());
			}
		}
		classes = new double[classList.size()];
		for(int i = 0; i < classes.length; i++)
		{
			classes[i] = classList.get(i);    // copy arraylist to the double array
		}
		
		return classes;
	}
	
	public void runExperiment(ArrayList<Node> dataset, double[] classes, boolean isRegression)
	{
		//TODO: implement changing the sets for each fold
		double[] learningRates = new double[]{0.001, 0.01, 0.1, 1};
		double[] momentums = new double[]{0, 0.001, 0.01, 0.1, 1};    // includes 0 for no momentum

		TrainingGroups groups = new TrainingGroups(dataset);
		int numAttrValues = dataset.get(0).getData().length;
		int numWeights = calcNumWeights(dataset.get(0).getData().length, classes, !isRegression);

		// Tuning phase
		ArrayList<Node> tuningSet = groups.getTuningSet();
		ArrayList<Node> trainingSet = groups.getTrainingSet();

		Tuner tuner = new BackPropTuner(1000, tuningSet, trainingSet, learningRates, momentums, classes,
				!isRegression, filePath, hiddenLayerCount);	//Placeholder to make sure tuner is initialized
		switch(trainer) // choose tuner
		{
			case "PSO" -> {
				tuner = new PSOTuner(20, new double[]{0.001, 1}, new double[]{0.001, 1}, new double[]{0.001, 1},
						100, 0.001, numWeights, numAttrValues, hiddenLayerCount, classes, !isRegression);
			}
			
			case "GA" -> {
				tuner = new GATuner(new double[]{0, .1, .5, .9, 1}, new double[]{0, .1, .5, .9, 1}, new double[]{0.001, 0.01, 0.1},
						100, initPop(100), trainingSet.get(0).getData().length, hiddenLayerCount, classes, !isRegression);
			}
			default -> System.err.println("Bad trainer name: " + trainer);
		}

		System.out.println("Started tuning\t\t" + filePath + "\talgo " + trainer + "\tfold " + fold + "\tlayer " +
				hiddenLayers + "\thiddenNodes " + Arrays.toString(hiddenLayerCount) );
		tuner.tune(trainingSet, tuningSet);
		System.out.println("Finished tuning\t\t" + filePath + "\tfold " + fold + "\tlayer " + hiddenLayers);

		//double learningRate = tuner.getBestLearningRate();
		//double momentum = tuner.getBestMomentum();

		// Test phase
		ArrayList<Node> testSet = groups.getTestSet();
		Network net = new Network(dataset.get(0).getData().length, hiddenLayerCount, classes, !isRegression); // Train on this network

		// Placeholder trainer to make sure it's initialized
		Trainer trainingAlgo = null;
		/*Trainer trainingAlgo = new PSO(numWeights, 10000, 0.001, ((PSOTuner) tuner).getBestParticleCount(),
				((PSOTuner) tuner).getBestInertia(), ((PSOTuner) tuner).getBestCogBias(),
				((PSOTuner) tuner).getBestSocialBias(), net);
*/
		switch(trainer)	// choose trainer
		{
			case "PSO" -> {
				trainingAlgo = new PSO(numWeights, 10000, 0.001, ((PSOTuner) tuner).getBestParticleCount(),
						((PSOTuner) tuner).getBestInertia(), ((PSOTuner) tuner).getBestCogBias(),
						((PSOTuner) tuner).getBestSocialBias(), net);
			}
			case "GA" -> {
				trainingAlgo = new Genetic(initPop(((GATuner)tuner).getBestPopSize()), ((GATuner)tuner).getBestCrossoverRate(),
						((GATuner)tuner).getBestMutationRate(), ((GATuner)tuner).getBestVariance(), ((GATuner)tuner).getBestK(), net);
			}
		}
		System.out.println("Started training\t" + filePath + "\tfold " + fold + "\tlayer " + hiddenLayers);
		trainingAlgo.train(trainingSet);
		System.out.println("Finished training\t" + filePath + "\tfold " + fold + "\tlayer " + hiddenLayers);
		/*Printer.println(filePath, "\nFold " + fold + " | Number of Hidden Layers: " + hiddenLayers);
		Printer.println(filePath, "Number of nodes per hidden layer: " + Arrays.toString(hiddenLayerCount));
		Printer.println(filePath, "Learning Rate: " + learningRate + " | Momentum Constant: " + momentum);
		Printer.println(filePath, "Fold " + fold);*/

		if(isRegression)
		{
			for(Node node : testSet)
			{
				ArrayList<Neuron> output = net.feedForward(node.getData());
				for(Neuron neuron : output)
				{
					Printer.println(filePath, "Output: " + neuron.getValue() + " | Original: " + node.getId());
				}
			}
		}
		else
		{
			HashMap<Neuron, Double> classMap = net.getOutputToClass();
			for(Node node : testSet)
			{
				ArrayList<Neuron> output = net.feedForward(node.getData());
				Printer.println(filePath, "\nFor example with class " + node.getId() + ":");
				double highestValue = 0;
				double mostLikelyClass = 0;
				for(Neuron neuron : output)
				{
					double neuronClass = classMap.get(neuron);
					double neuronValue = neuron.getValue();
					Printer.println(filePath, "Output from Neuron corresponding to class " + neuronClass + ": " + neuronValue);
					if(neuronValue > highestValue)
					{    // find most likely class
						highestValue = neuronValue;
						mostLikelyClass = neuronClass;
					}
				}
				Printer.println(filePath, "Predicted class: " + mostLikelyClass);
			}
		}

		double MSE = net.calculateMSError(testSet);
		Printer.println(filePath, "Overall Mean-Squared Error for Fold " + fold + ": " + MSE + "\n");
	}

	public int calcNumWeights(int inputLayerNodeNum, double[] classes, boolean isClassification)
	{
		// Calculates number of weights by using a network generation
		Network nn = new Network(inputLayerNodeNum, hiddenLayerCount, classes, isClassification);
		return nn.getNumWeights();
	}
	
	public ArrayList<Chromosome> initPop(int size)
	{
		ArrayList<Chromosome> pop = new ArrayList<>();
		for(int i = 0; i < size; i++)
		{
			pop.add(new Chromosome());
		}
		return pop;
	}

	public static void main(String[] args)
	{
		//use these if you want to run a single data set
		/*Driver test = new Driver("machine");
		test.start();*/
		String[] trainers = {/*"GA", "DE", */"PSO"};
		//use these if you want to run all the data sets

		String[] files = {"abalone", "breast-cancer-wisconsin", "forestfires", "glass", "machine", "soybean-small"};
		int[][] nodesPerLayer = {
				{}, {5}, {1, 7},//abalone
				{}, {1}, {1, 9},//cancer
				{}, {10}, {3, 10},//fires
				{}, {9}, {4, 10},//glass
				{}, {10}, {1, 8},//machine
				{}, {29}, {10, 10},//beans
		};
		int nodeCountCounter = 0;
		for(String file : files)//create a new instance of the driver for each of the data sets.
		{
			/*for(int layer = 0; layer < 3; layer++)
			{
				for(int fold = 0; fold < 10; fold++)
				{*/
					for(String t : trainers)
					{
						new Driver(file, nodesPerLayer[nodeCountCounter], 0, t).start();//Starts a new thread
					}
				/*}
				nodeCountCounter++;
			}*/
		}

		//DataParser dp = new DataParser();
		//dp.backPropOutput("abalone");
		//dp.backPropOutput("forestfires");
		//dp.backPropOutput("machine");
		/*Genetic genetic = new Genetic();
		double answer = genetic.normalDistributionSelection(0.5, 0.5);
		System.out.println(answer);*/
	}
}