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
	
	private final int numHiddenLayers;
	private final int[] bestNumNodesPerLayer;
	
	private final String outFile;
	
	private final int maxIterations;
	private final int maxHiddenNodesPerLayer;
	private double bestError;
	
	public RunWithTuning(int maxHiddenNodesPerLayer, int maxIterations, ArrayList<Node> tuningSet, ArrayList<Node> trainingSet, double[] learningRates, double[] momentums, double[] outputLayerClasses, boolean isClassification, int numHiddenLayers, String outFile)
	{
		this.tuningSet = tuningSet;
		this.trainingSet = trainingSet;
		
		this.learningRates = learningRates;
		bestLearningRate = 0;
		
		this.momentums = momentums;
		bestMomentum = 0;
		
		this.outputLayerClasses = outputLayerClasses;
		this.isClassification = isClassification;
		
		this.numHiddenLayers = numHiddenLayers;
		bestNumNodesPerLayer = new int[numHiddenLayers];
		bestError = Double.MAX_VALUE;
		
		this.outFile = outFile;
		
		this.maxIterations = maxIterations;     // Maximum number of gradient descent iterations to tune with
		this.maxHiddenNodesPerLayer = maxHiddenNodesPerLayer;
	}
	
	//tunes the network to determine the best momentum, learning rate, and number of nodes per hidden layer.
	public void tune()
	{
		for(double momentum : momentums)
		{
			for(double learningRate : learningRates)
			{
				double error;
				Network n;
				BackPropagation backProp;
				if(numHiddenLayers == 0)
				{
					n = new Network(tuningSet.get(0).getData().length, new int[] {}, outputLayerClasses, isClassification);
					backProp = new BackPropagation(n, maxIterations, learningRate, momentum, outFile);
					backProp.trainNetwork(trainingSet);
					error = backProp.calculateMSError(tuningSet);
					if(error <= bestError)
					{
						bestError = error;
						bestLearningRate = learningRate;
						bestMomentum = momentum;
					}
				}
				else if(numHiddenLayers == 1)
				{
					for(int k = 1; k < maxHiddenNodesPerLayer; k++)
					{
						n = new Network(tuningSet.get(0).getData().length, new int[] {k}, outputLayerClasses, isClassification);
						backProp = new BackPropagation(n, maxIterations, learningRate, momentum, outFile);
						backProp.trainNetwork(trainingSet);
						error = backProp.calculateMSError(tuningSet);
						if(error <= bestError)
						{
							bestError = error;
							bestLearningRate = learningRate;
							bestMomentum = momentum;
							bestNumNodesPerLayer[0] = k;
						}
					}
				}
				else//numHiddenLayers == 2
				{
					for(int k = 0; k < maxHiddenNodesPerLayer; k++)
					{
						for(int l = 0; l < maxHiddenNodesPerLayer; l++)
						{
							n = new Network(tuningSet.get(0).getData().length, new int[] {k, l}, outputLayerClasses, isClassification);
							backProp = new BackPropagation(n, maxIterations, learningRate, momentum, outFile);
							backProp.trainNetwork(trainingSet);
							error = backProp.calculateMSError(tuningSet);
							if(error <= bestError)
							{
								bestError = error;
								bestLearningRate = learningRate;
								bestMomentum = momentum;
								bestNumNodesPerLayer[0] = k;
								bestNumNodesPerLayer[1] = l;
							}
						}
					}
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
	
	public int[] getBestNumNodesPerLayer()
	{
		return bestNumNodesPerLayer;
	}
}