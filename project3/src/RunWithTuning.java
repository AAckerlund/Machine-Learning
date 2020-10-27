import java.util.ArrayList;

public class RunWithTuning {
    private BackPropagation backProp;
    ArrayList<Node> tuningSet, trainingSet;
    
    private double[] learningRates;
    private double bestLearningRate;
    
    private double[] momentums;
    private double bestMomentum;
    
    private double[] outputLayerClasses;
    private boolean isClassification;
    
    private int numHiddenLayers;
    private int[] bestNumNodesPerLayer;
    
    private String outFile;
    
    private double bestError;

    public RunWithTuning(ArrayList<Node> tuningSet, ArrayList<Node> trainingSet, double[] learningRates, double[] momentums, double[] outputLayerClasses, boolean isClassification, int numHiddenLayers, String outFile) {
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
    }
    
    public void tune()
    {
        for(double momentum : momentums)
        {
            Printer.println(outFile, "Momentum is" + momentum);
            for(double learningRate : learningRates)
            {
                double error;
                Network n;
                if(numHiddenLayers == 0)
                {
                    n = new Network(tuningSet.get(0).getData().length, new int[] {}, outputLayerClasses, isClassification);
                    backProp = new BackPropagation(n, 10000, learningRate, momentum, outFile);
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
                    for(int k = 1; k < 100; k++)
                    {
                        n = new Network(tuningSet.get(0).getData().length, new int[] {k}, outputLayerClasses, isClassification);
                        backProp = new BackPropagation(n, 10000, learningRate, momentum, outFile);
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
                    for(int k = 0; k < 100; k++)
                    {
                        for(int l = 0; l < 100; l++)
                        {
                            n = new Network(tuningSet.get(0).getData().length, new int[] {k, l}, outputLayerClasses, isClassification);
                            backProp = new BackPropagation(n, 10000, learningRate, momentum, outFile);
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

    public double runLearningRate() {
        double optimalLearningRate = learningRates[0];
        double lowestError = Double.POSITIVE_INFINITY;
        ArrayList<Double> errors = new ArrayList<>();
        for (double learningRate : learningRates) {
            backProp.learningRate = learningRate;
            double error = backProp.trainNetwork(tuningSet);
            errors.add(error);
            if(error < lowestError){
                lowestError = error;
                optimalLearningRate = learningRate;
            }
        }
        System.err.println("Errors from Learning Rates: ");
        for(double error: errors){
            System.out.println(error);
        }
        return optimalLearningRate;
    }

    public double runMomentum() {
        double optimalMomentum = momentums[0];
        double lowestError = Double.POSITIVE_INFINITY;
        ArrayList<Double> errors = new ArrayList<>();
        for (double momentum : momentums) {
            backProp.momentumScale = momentum;
            double error = backProp.trainNetwork(tuningSet);
            errors.add(error);
            if(error < lowestError){
                lowestError = error;
                optimalMomentum = momentum;
            }
        }
        System.err.println("Errors from Momentums: ");
        for(double error: errors){
            System.out.println(error);
        }
        return optimalMomentum;
    }
    
    /*public ArrayList<Neuron> runNodesPerLayerNum() {
	ArrayList<Neuron> optimalNodesPerLayerNum = hiddenLayerNodeNums[0];
	double lowestError = Double.POSITIVE_INFINITY;
	ArrayList<Double> errors = new ArrayList<>();
	for (int hiddenLayer : hiddenLayerNodeNums) {
		backPropForTuning.nn.setHiddenLayers(hiddenLayerN);
		double error = backPropForTuning.trainNetwork(trainingSet);
		errors.add(error);
		if(error < lowestError){
			lowestError = error;
			optimalNodesPerLayerNum = hiddenLayer;
		}
	}
	System.err.println("Errors from Number of Hidden Layer Nodes: ");
	for(double error: errors){
		System.out.println(error);
	}
	return optimalNodesPerLayerNum;
}*/
    
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
    
    public double getBestError()
    {
        return bestError;
    }

}