import java.util.ArrayList;

public class DETuner extends Tuner
{
    private final ArrayList<Chromosome> weights;
    private double bestError;
    
    private double bestCrossoverRate;
    private final double[] crossoverRates;
    
    private double bestBeta;
    private final double[] betas;
    
    private final int inputLayerNodeNum;
    private final int[] hiddenLayerNodeNums;
    private final double[] outputLayerClasses;
    private final boolean isClassification;

    private final int maxPopulationSize;
    private int bestPopSize;
    
    public DETuner(ArrayList<Chromosome> weights, double[] crossoverRates, double[] betas, int populationSize, int inputLayerNodeNum, int[] hiddenLayerNodeNums, double[] outputLayerClasses, boolean isClassification)
    {
        this.weights = weights;
        bestError = Double.MAX_VALUE;
        
        this.crossoverRates = crossoverRates;
        this.betas = betas;

        this.maxPopulationSize = populationSize;
        bestPopSize = 0;
        
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
                for(int popSize = 10; popSize < maxPopulationSize; popSize+=10) {
                    DE = new DifferentialEvolution(weights, beta, crossoverRate, net);
                    DE.train(trainingSet);
                    error = net.calculateMSError(tuningSet);
                    if (error < bestError) {
                        bestCrossoverRate = crossoverRate;
                        bestBeta = beta;
                        bestPopSize = popSize;
                        bestError = error;
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
                bestCrossoverRate = .25;
                bestBeta = 2;
                bestPopSize = 60;
            }
            case "machine" -> {
                bestCrossoverRate = .25;
                bestBeta = 0;
                bestPopSize = 80;
            }
            case "soybean-small" -> {
                bestCrossoverRate = .75;
                bestBeta = 2;
                bestPopSize = 10;
            }
            case "glass" -> {
                bestCrossoverRate = .25;
                bestBeta = 0;
                bestPopSize = 20;
            }
            case "forestfires" -> {
                bestCrossoverRate = .75;
                bestBeta = 0;
                bestPopSize = 80;
            }
            case "abalone" -> {
                bestCrossoverRate = .25;
                bestBeta = 0;
                bestPopSize = 80;
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

    public int getBestPopSize()
    {
        return bestPopSize;
    }
}
