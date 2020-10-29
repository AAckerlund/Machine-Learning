import java.util.ArrayList;

public class RunWithTuning {
    private BackPropagation backProp;
    private ArrayList<Node> tuningSet, trainingSet;
    
    private double[] learningRates;
    private double bestLearningRate;
    
    private double[] momentums;
    private double bestMomentum;
    
    private double[] outputLayerClasses;
    private boolean isClassification;
    
    private int numHiddenLayers;
    private int[] bestNumNodesPerLayer;
    
    private String outFile;

    private int maxIterations;
    private int maxHiddenNodesPerLayer;
    private double bestError;

    public RunWithTuning(int maxHiddenNodesPerLayer, int maxIterations, ArrayList<Node> tuningSet, ArrayList<Node> trainingSet, double[] learningRates, double[] momentums, double[] outputLayerClasses, boolean isClassification, int numHiddenLayers, String outFile) {
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
    
    public void tune()
    {
        for(double momentum : momentums)
        {
            for(double learningRate : learningRates)
            {
                double error;
                Network n;
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