import java.util.ArrayList;

public class PSOTuner extends Tuner
{
    private double bestError;
    private final int maxIterations;
    private final double MSECutoff;
    private final int numWeights;
    
    private double[] outputLayerClasses;
    private final boolean isClassification;
    
    private final int inputLayerNodeNum;
    private final int[] hiddenLayerNodeNums;

    private int bestParticleCount;
    private final int maxParticleCount;

    private double bestInertia;
    private double[] inertias;

    private double bestCogBias;
    private double[] cogBiases;

    private double bestSocialBias;
    private double[] socialBiases;

    public PSOTuner(int particleCount, double[] inertias, double[] cogBiases, double[] socialBiases, int maxIterations,
                    double MSECutoff, int numWeights, int inputLayerNodeNum, int[] hiddenLayerNodeNums,
                    double[] outputLayerClasses, boolean isClassification)
    {
        this.numWeights = numWeights;
        this.maxIterations = maxIterations;
        this.MSECutoff = MSECutoff;
        maxParticleCount = particleCount;
        
        this.inputLayerNodeNum = inputLayerNodeNum;
        this.hiddenLayerNodeNums = hiddenLayerNodeNums;
        
        this.outputLayerClasses = outputLayerClasses;
        this.isClassification = isClassification;

        this.inertias = inertias;
        this.cogBiases = cogBiases;
        this.socialBiases = socialBiases;

        bestError = Double.MAX_VALUE;
    }

    @Override
    void tune(ArrayList<Node> trainingSet, ArrayList<Node> tuningSet)
    {
        PSO pso;
        // Initialize network with certain size
        Network net = new Network(inputLayerNodeNum,hiddenLayerNodeNums,outputLayerClasses,isClassification);
        for(double inertia : inertias)
        {
            for(double cogBias : cogBiases)
            {
                for(double socialBias : socialBiases)
                {
                    for(int i = 2; i < maxParticleCount; i++)   // Start with at least 2 particles
                    {
                        //System.out.println("Tuning PSO with inertia " + inertia + "\tcogBias " + cogBias + "\tsocBias " + socialBias + "\tparticles " + i);
                        pso = new PSO(numWeights, maxIterations, MSECutoff, i, inertia, cogBias, socialBias, net);
                        pso.train(trainingSet);
                        double error = net.calculateMSError(tuningSet);
                        if(error < bestError)
                        {
                            bestError = error;
                            bestCogBias = cogBias;
                            bestInertia = inertia;
                            bestParticleCount = i;
                            bestSocialBias = socialBias;
                        }
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
