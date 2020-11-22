public class PSOTuner extends Tuner
{
    private double bestError;
    private final int maxIterations;
    private final int numValues;

    private int bestParticleCount;
    private final int maxParticleCount;

    private double bestInertia;
    private double[] inertias;

    private double bestCogBias;
    private double[] cogBiases;

    private double bestSocialBias;
    private double[] socialBiases;

    public PSOTuner(int particleCount, double[] inertias, double[] cogBiases, double[] socialBiases, int maxIterations, int numValues)
    {
        this.numValues = numValues;
        this.maxIterations = maxIterations;
        maxParticleCount = particleCount;

        this.inertias = inertias;
        this.cogBiases = cogBiases;
        this.socialBiases = socialBiases;

        bestError = Double.MAX_VALUE;
    }

    @Override
    void tune()
    {
        PSO pso;
        for(double inertia : inertias)
        {
            for(double cogBias : cogBiases)
            {
                for(double socialBias : socialBiases)
                {
                    for(int i = 1; i < maxParticleCount; i++)
                    {
                        // TODO: Fix this by storing parameters somewhere
                        /*pso = new PSO(numValues, maxIterations, i, inertia, cogBias, socialBias, new Network(inputLayerNodeNum,hiddenLayerNodeNums,outputLayerClasses,isClassification));
                        pso.train();
                        double error = pso.calcMSE();
                        if(error < bestError)
                        {
                            bestError = error;
                            bestCogBias = cogBias;
                            bestInertia = inertia;
                            bestParticleCount = i;
                            bestSocialBias = socialBias;
                        }*/
                    }
                }
            }
        }
    }

    public int getBestParticleCount()
    {
        return bestParticleCount;
    }

    public double getBestInertia()
    {
        return bestInertia;
    }

    public double getBestCogBias()
    {
        return bestCogBias;
    }

    public double getBestSocialBias()
    {
        return bestSocialBias;
    }
}
