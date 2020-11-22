import java.util.ArrayList;

public class DETuner extends Tuner
{
    private final ArrayList<Chromosome> weights;
    private double bestError;
    
    private double bestCrossoverRate;
    private double[] crossoverRates;
    
    private double bestBeta;
    private double[] betas;
    
    private int inputLayerNodeNum;
    private int[] hiddenLayerNodeNums;
    private double[] outputLayerClasses;
    private boolean isClassification;
    
    public DETuner(ArrayList<Chromosome> weights, double[] crossoverRates, double[] betas, int inputLayerNodeNum, int[] hiddenLayerNodeNums, double[] outputLayerClasses, boolean isClassification)
    {
        this.weights = weights;
        bestError = Double.MAX_VALUE;
        
        this.crossoverRates = crossoverRates;
        this.betas = betas;
        
        this.inputLayerNodeNum = inputLayerNodeNum;
        this.hiddenLayerNodeNums = hiddenLayerNodeNums;
        this.outputLayerClasses = outputLayerClasses;
        this.isClassification = isClassification;
    }
    @Override
    void tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet)
    {
        Trainer DE;
        double error;
        Network net = new Network(inputLayerNodeNum, hiddenLayerNodeNums, outputLayerClasses, isClassification);
        for(double crossoverRate: crossoverRates)
        {
            for(double beta : betas)
            {
                DE = new DifferentialEvolution(weights, beta, crossoverRate);
                DE.train(trainingSet);
                error = net.calculateMSError(tuningSet);
                if(error < bestError)
                {
                    bestCrossoverRate = crossoverRate;
                    bestBeta = beta;
                    bestError = error;
                }
            }
        }
    }
    
    public double getBestCrossoverRate()
    {
        return bestCrossoverRate;
    }
    
    public double getBestBeta()
    {
        return bestBeta;
    }
}
