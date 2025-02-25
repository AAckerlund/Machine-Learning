import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

//TODO Show average performance: Go through one of the output files we have and show the numbers in there.

public class Driver extends Thread//extending Thread allows for multithreading
{
	String fileStart = "dataSets/", fileEnd = ".data", filePath;
	
	public Driver(String filePath)
	{
		this.filePath = filePath;
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
				isRegression = true;
				Printer.println(filePath, "Done Abalone");
			}
			case "breast-cancer-wisconsin" -> {
				nodes = p.cancerParser(fileStart + filePath + fileEnd);
				Printer.println(filePath, "Done Cancer");
			}
			case "forestfires" -> {
				nodes = p.firesParser(fileStart + filePath + fileEnd);
				isRegression = true;
				Printer.println(filePath, "Done Forest Fires");
			}
			case "glass" -> {
				nodes = p.glassParser(fileStart + filePath + fileEnd);
				Printer.println(filePath, "Done Glass");
			}
			case "machine" -> {
				nodes = p.machineParser(fileStart + filePath + fileEnd);
				isRegression = true;
				Printer.println(filePath, "Done Machine");
			}
			case "soybean-small" -> {
				nodes = p.beanParser(fileStart + filePath + fileEnd);
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
		double[] learningRates = new double[]{0.001, 0.01, 0.1, 1};
		double[] momentums = new double[]{0, 0.001, 0.01, 0.1, 1};    // includes 0 for no momentum

		for(int layers = 0; layers <= 2; layers++)
		{
			TrainingGroups groups = new TrainingGroups(dataset);
			double totalMSE = 0;
			for(int fold = 0; fold < 10; fold++)
			{
				// Tuning phase
				ArrayList<Node> tuningSet = groups.getTuningSet();
				ArrayList<Node> trainingSet = groups.getTrainingSet();
				RunWithTuning tuner = new RunWithTuning(50, 1000, tuningSet, trainingSet, learningRates, momentums, classes, !isRegression, layers, filePath);
				System.out.println("Started tuning\t\t" + filePath + "\tfold " + fold + " layer " + layers);
				tuner.tune();
				System.out.println("Finished tuning\t\t" + filePath + "\tfold " + fold + " layer " + layers);
				
				double learningRate = tuner.getBestLearningRate();
				double momentum = tuner.getBestMomentum();
				int[] hiddenLayerNodeNums = tuner.getBestNumNodesPerLayer();
				
				// Test phase
				ArrayList<Node> testSet = groups.getTestSet();
				Network net = new Network(dataset.get(0).getData().length, hiddenLayerNodeNums, classes, !isRegression);
				BackPropagation bp = new BackPropagation(net, 10000, learningRate, momentum, filePath);
				
				System.out.println("Started training\t" + filePath + "\tfold " + fold + " layer " + layers);
				bp.trainNetwork(trainingSet);
				System.out.println("Finished training\t" + filePath + "\tfold " + fold + " layer " + layers);
				Printer.println(filePath, "\nFold " + fold + " | Number of Hidden Layers: " + layers);
				Printer.println(filePath, "Number of nodes per hidden layer: " + Arrays.toString(hiddenLayerNodeNums));
				Printer.println(filePath, "Learning Rate: " + learningRate + " | Momentum Constant: " + momentum);
				Printer.println(filePath, "Fold " + fold);

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
				
				double MSE = bp.calculateMSError(testSet);
				totalMSE += MSE;

				groups.iterateTestSet();    // Move to next fold
			}
			totalMSE /= 10;    // take average
			Printer.println(filePath, "Average Mean-Squared Error for " + layers + " hidden layers: " + totalMSE + "\n");
		}
	}
	
	public static void main(String[] args)
	{
		//TODO 1/2 show sample output by running a single data set and showing the file being generated
		//use these if you want to run a single data set
		/*Driver test = new Driver("soybean-small");
		test.start();*/
		
		//use these if you want to run all the data sets
		String[] files = {"abalone", "breast-cancer-wisconsin", "forestfires", "glass", "machine", "soybean-small"};
		for(String file : files)//create a new instance of the driver for each of the data sets.
		{
			Driver d = new Driver(file);
			d.start();//Starts a new thread
		}
	}
}