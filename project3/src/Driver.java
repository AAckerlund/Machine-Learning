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

		//TODO: Get rid of ignoredatt value and just place the variable of interest in ID, and not in data[]
		//Testing training a single example in backprop for a boolean classification
/*
		Network net = new Network(3, new int[]{2, 1}, 1, false);
		BackPropagation bp = new BackPropagation(net, 100000, true, false, 1, 0.5);

		ArrayList<Node> trainingSet = new ArrayList<>();
		trainingSet.add(new Node(1, new double[]{0.1, 0.5, 0.8}, -1));
		trainingSet.add(new Node(1, new double[]{0.2, 0.4, 0.7}, -1));
		trainingSet.add(new Node(0, new double[]{0.5, 0.1, 0.2}, -1));
		trainingSet.add(new Node(0, new double[]{0.6, 0.2, 0.4}, -1));
*/


/*
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

 */

		//Train using whole training set
/*
		bp.trainNetwork(trainingSet);
		System.out.println("After Training the network...");
		for (int i = 0; i < trainingSet.size(); i++) {
			ArrayList<Neuron> output = net.feedForward(trainingSet.get(i).getData());
			for (Neuron neuron : output) {
				System.out.println("Output: " + neuron.getValue() + " | Original: " + trainingSet.get(i).getId());
			}
		}
*/

		// TODO: This is just a bandaid. Should edit the response variable out of the data and have a way to normalize id
		// Parse the problematic feature out of the array and into the id
		ArrayList<Node> parsedNodes = new ArrayList<>();
		for (Node node : nodes) {
			int ignoredAttr = node.getIgnoredAttr();
			double normalizedID = node.getData()[ignoredAttr];

			// Populate new data array without the ignored attribute
			double[] newData = new double[node.getData().length-1];
			for (int i = 0; i < node.getData().length; i++) {
				if (i < ignoredAttr) {
					newData[i] = node.getData()[i];
				}
				else if (i == ignoredAttr) {
					continue;	// skip ignored attribute
				}
				else {	// past ignored attribute, offset index when assigning to new data
					newData[i-1] = node.getData()[i];
				}
			}
			parsedNodes.add(new Node(normalizedID, newData, 128395));
		}


		// Train and test using the real test set

		TrainingGroups groups = new TrainingGroups(parsedNodes);

		Network nn = new Network(parsedNodes.get(0).getData().length, new int[]{}, 1, !isRegression);	// 0 layers
		//Network nn = new Network(parsedNodes.get(0).getData().length, new int[]{parsedNodes.get(0).getData().length-1}, 1, !isRegression);	// 1 layer
		//Network nn = new Network(parsedNodes.get(0).getData().length, new int[]{parsedNodes.get(0).getData().length-1,
		// parsedNodes.get(0).getData().length-2}, 1, !isRegression);	// 2 layers

		BackPropagation bp = new BackPropagation(nn, 10000, false, !isRegression, 0.001, 0.1);

		ArrayList<Node> trainingSet = groups.getTrainingSet();
		bp.trainNetwork(trainingSet);
		System.out.println("After Training the network...");
		for (int i = 0; i < trainingSet.size(); i++) {
			ArrayList<Neuron> output = nn.feedForward(trainingSet.get(i).getData());
			for (Neuron neuron : output) {
				System.out.println("Output: " + neuron.getValue() + " | Original: " + trainingSet.get(i).getId());
			}
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