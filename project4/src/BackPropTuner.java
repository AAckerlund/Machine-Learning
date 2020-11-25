import java.util.ArrayList;

public class BackPropTuner extends Tuner
{
	
	private final double[] learningRates;
	
	private final double[] momentums;
	
	private final double[] outputLayerClasses;
	private final boolean isClassification;

	private final int[] bestNumNodesPerLayer;
	
	private final String outFile;
	
	private final int maxIterations;
	private double bestError;
	
	public BackPropTuner(int maxIterations, double[] learningRates, double[] momentums, double[] outputLayerClasses, boolean isClassification, String outFile, int[] hiddenLayerNodeNum)
	{
		
		this.learningRates = learningRates;
		
		this.momentums = momentums;
		
		this.outputLayerClasses = outputLayerClasses;
		this.isClassification = isClassification;

		bestNumNodesPerLayer = hiddenLayerNodeNum;
		bestError = Double.MAX_VALUE;
		
		this.outFile = outFile;
		
		this.maxIterations = maxIterations;     // Maximum number of gradient descent iterations to tune with
	}
	
	//tunes the network to determine the best momentum, learning rate, and number of nodes per hidden layer.
	@Override
	public void tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet)
	{
		for(double momentum : momentums)
		{
			for(double learningRate : learningRates)
			{
				Network net = new Network(tuningSet.get(0).getData().length, bestNumNodesPerLayer, outputLayerClasses, isClassification);
				BackPropagation backProp = new BackPropagation(net, maxIterations, learningRate, momentum, outFile);
				backProp.trainNetwork(trainingSet);
				double error = backProp.calculateMSError(tuningSet);
				if(error <= bestError)
				{
					bestError = error;
				}
			}
		}
	}
	
	@Override
	public void tune(String dataSet)
	{
	
	}
	
}