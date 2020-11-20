import java.util.ArrayList;

public class RunWithTuning
{
	private final ArrayList<Node> tuningSet, trainingSet;
	
	private final double[] learningRates;
	private double bestLearningRate;
	
	private final double[] momentums;
	private double bestMomentum;
	
	private final double[] outputLayerClasses;
	private final boolean isClassification;

	private final int[] bestNumNodesPerLayer;
	
	private final String outFile;
	
	private final int maxIterations;
	private double bestError;
	
	public RunWithTuning(int maxIterations, ArrayList<Node> tuningSet, ArrayList<Node> trainingSet, double[] learningRates, double[] momentums, double[] outputLayerClasses, boolean isClassification, String outFile, int[] hiddenLayerNodeNum)
	{
		this.tuningSet = tuningSet;
		this.trainingSet = trainingSet;
		
		this.learningRates = learningRates;
		bestLearningRate = 0;
		
		this.momentums = momentums;
		bestMomentum = 0;
		
		this.outputLayerClasses = outputLayerClasses;
		this.isClassification = isClassification;

		bestNumNodesPerLayer = hiddenLayerNodeNum;
		bestError = Double.MAX_VALUE;
		
		this.outFile = outFile;
		
		this.maxIterations = maxIterations;     // Maximum number of gradient descent iterations to tune with
	}
	
	//tunes the network to determine the best momentum, learning rate, and number of nodes per hidden layer.
	public void tune()
	{
		for(double momentum : momentums)
		{
			for(double learningRate : learningRates)
			{
				Network n = new Network(tuningSet.get(0).getData().length, bestNumNodesPerLayer, outputLayerClasses, isClassification);
				BackPropagation backProp = new BackPropagation(n, maxIterations, learningRate, momentum, outFile);
				backProp.trainNetwork(trainingSet);
				double error = backProp.calculateMSError(tuningSet);
				if(error <= bestError)
				{
					bestError = error;
					bestLearningRate = learningRate;
					bestMomentum = momentum;
				}
			}
		}
	}
	
	public double getBestLearningRate()
	{
		return bestLearningRate;
	}
	
	public double getBestMomentum()
	{
		return bestMomentum;
	}
}