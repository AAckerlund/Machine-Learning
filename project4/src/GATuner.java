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

    public GATuner(double[] crossoverRates, double[] mutationRates, double[] variances, int populationSize, ArrayList<Chromosome> weights)
    {
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
    void tune()
    {
        Trainer GA;
        double error;
        for(double crossoverRate : crossoverRates)
        {
            for(double mutationRate : mutationRates)
            {
                for(double variance : variances)
                {
                    for(int i = 1; i < populationSize; i++)
                    {
                        GA = new Genetic(weights, crossoverRate, mutationRate, variance, i);
                        GA.train();
                        error = GA.calcMSE();
                        if(error < bestError)
                        {
                            bestCrossoverRate = crossoverRate;
                            bestMutationRate = mutationRate;
                            bestVariance = variance;
                            bestK = i;
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
