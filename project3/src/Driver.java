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