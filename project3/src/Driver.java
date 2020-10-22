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
		//Normalization.zNormalize(nodes);	// use z-normalization to normalize the nodes

		//TODO: Get rid of ignoredatt value and just place the variable of interest in ID, and not in data[]
		//Testing training a single example in backprop for a boolean classification
		Network net = new Network(3, new int[]{}, 1, false);
		BackPropagation bp = new BackPropagation(net, 1000, false, true, 1);

		ArrayList<Node> trainingSet = new ArrayList<>();
		trainingSet.add(new Node(1, new double[]{0.1, 0.5, 0.8}, -1));
		trainingSet.add(new Node(1, new double[]{0.2, 0.4, 0.7}, -1));
		trainingSet.add(new Node(0, new double[]{0.5, 0.1, 0.2}, -1));
		trainingSet.add(new Node(0, new double[]{0.6, 0.2, 0.4}, -1));

		//Before Training an example
		System.out.println("Before Training an example...");
		ArrayList<Neuron> output = net.feedForward(trainingSet.get(0).getData());
		for (Neuron neuron : output) {
			System.out.println(neuron.getValue());
		}

		bp.trainExample(trainingSet.get(0));

		//After Training an example, should be closer to the expected class
		System.out.println("After Training an example...");
		output = net.feedForward(trainingSet.get(0).getData());
		for (Neuron neuron : output) {
			System.out.println(neuron.getValue());
		}

		/*
		Network net;
		for(int i = 0; i < 2; i++)
		{
			int[] hiddenLayers = new int[i];
			for(int j = 0; j < i; j++)
			{
				hiddenLayers[j] = (int) (Math.random() * nodes.get(0).getData().length - 1);
			}
			net = new Network(nodes.get(0).getData().length, hiddenLayers, 1, .5, !isRegression);
			for(Node node : nodes)
			{
				ArrayList<Neuron> output = net.feedForward(node.getData());
				for(Neuron neuron : output)
				{
					System.out.println(neuron.getValue());
				}
			}
		}
		 */
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