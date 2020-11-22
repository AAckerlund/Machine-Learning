import java.util.ArrayList;

public class GATuner extends Tuner
{
    private final ArrayList<Chromosome> weights;

    private double[] crossoverRates;
    private double bestCrossoverRate;

    private double[] mutationRates;
    private double bestMutationRate;

    private double[] variances;
    private double bestVariance;

    private int populationSize;
    private int bestK;//1 to population size

    private double bestError;
    
    private final int inputLayerNodeNum;
    private final int[] hiddenLayerNodeNums;
    private final double[] outputLayerClasses;
    private final boolean isClassification;

    public GATuner(double[] crossoverRates, double[] mutationRates, double[] variances, int populationSize, ArrayList<Chromosome> weights,
                   int inputLayerNodeNum, int[] hiddenLayerNodeNums, double[] outputLayerClasses, boolean isClassification)
    {
        this.inputLayerNodeNum = inputLayerNodeNum;
        this.hiddenLayerNodeNums = hiddenLayerNodeNums;
        this.outputLayerClasses = outputLayerClasses;
        this.isClassification = isClassification;
        
        this.weights = weights;

        this.crossoverRates = crossoverRates;
        bestCrossoverRate = 0;

        this.mutationRates = mutationRates;
        bestMutationRate = 0;

        this.variances = variances;
        bestVariance = 0;

        this.populationSize = populationSize;
        bestK = 0;

        bestError = Double.MAX_VALUE;
    }

    @Override
    void tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet)
    {
        Trainer GA;
        double error;
        Network net = new Network(inputLayerNodeNum,hiddenLayerNodeNums,outputLayerClasses,isClassification);
        for(double crossoverRate : crossoverRates)
        {
            for(double mutationRate : mutationRates)
            {
                for(double variance : variances)
                {
                    for(int i = 1; i < populationSize; i++)
                    {
                        GA = new Genetic(weights, crossoverRate, mutationRate, variance, i, net, tuningSet);
                        GA.train(trainingSet);
                        error = net.calculateMSError(tuningSet);
                        if(error < bestError)
                        {
                            bestCrossoverRate = crossoverRate;
                            bestMutationRate = mutationRate;
                            bestVariance = variance;
                            bestK = i;
                            bestError = error;
                        }
                    }
                }
            }
        }
    }

    public double getBestCrossoverRate()
    {
        return bestCrossoverRate;
    }

    public double getBestMutationRate()
    {
        return bestMutationRate;
    }

    public double getBestVariance()
    {
        return bestVariance;
    }

    public int getBestK()
    {
        return bestK;
    }
}
