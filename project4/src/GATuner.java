import java.util.ArrayList;

public class GATuner extends Tuner
{
    private final ArrayList<Chromosome> weights;

    private final double[] crossoverRates;
    private double bestCrossoverRate;

    private final double[] mutationRates;
    private double bestMutationRate;

    private final double[] variances;
    private double bestVariance;

    private final int maxPopulationSize;
    private int bestPopSize;
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

        this.maxPopulationSize = populationSize;
        bestK = 0;
        bestPopSize = 0;

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
                    for(int popSize = 8; popSize < maxPopulationSize; popSize+=4)
                    {
                        for(int i = 2; i < popSize; i += 2)
                        {
                            GA = new Genetic(weights, crossoverRate, mutationRate, variance, i, net);
                            GA.train(trainingSet);
                            error = net.calculateMSError(tuningSet);
                            if(error < bestError)
                            {
                                bestCrossoverRate = crossoverRate;
                                bestMutationRate = mutationRate;
                                bestVariance = variance;
                                bestK = i;
                                bestPopSize = popSize;
                                bestError = error;

                                System.out.println("Best crossover Rate: " + bestCrossoverRate);
                                System.out.println("Best mutation Rate: " + bestMutationRate);
                                System.out.println("Best variance: " + bestVariance);
                                System.out.println("Best K: " + bestK);
                                System.out.println("Best pop size" + bestPopSize);
                            }
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public void tune(String dataSet)
    {
        switch(dataSet)
        {
            case "breast-cancer-wisconsin" -> {
                bestCrossoverRate = 0.25;
                bestMutationRate = 0.25;
                bestVariance = 0.1;
                bestK = 22;
                bestPopSize = 40;
            }
            case "machine" -> {
                bestCrossoverRate = 0.75;
                bestMutationRate = 0.25;
                bestVariance = 0.1;
                bestK = 16;
                bestPopSize = 84;
            }
            case "soybean-small" -> {
                bestCrossoverRate = 0.75;
                bestMutationRate = 0.75;
                bestVariance = 0.1;
                bestK = 4;
                bestPopSize = 12;
            }
            case "glass" -> {
                bestCrossoverRate = 0.25;
                bestMutationRate = 0.75;
                bestVariance = 0.1;
                bestK = 10;
                bestPopSize = 20;
            }
            case "forestfires" -> {
                bestCrossoverRate = 0.75;
                bestMutationRate = 0.25;
                bestVariance = 0.1;
                bestK = 32;
                bestPopSize = 88;
            }
            case "abalone" -> {
                bestCrossoverRate = 0.75;
                bestMutationRate = 0.25;
                bestVariance = 0.1;
                bestK = 44;
                bestPopSize = 80;
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
    
    public int getBestPopSize()
    {
        return bestPopSize;
    }
}
