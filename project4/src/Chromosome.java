public class Chromosome {
    private double[] weights;
    private double fitness;

    public Chromosome()
    {
    }

    public Chromosome(int weightSize)
    {
        weights = new double[weightSize];
    }

    public Chromosome(double[] weights)
    {
        this.weights = weights;
    }

    public double[] getWeights()
    {
        return weights;
    }

    public void setWeights(double[] weights)
    {
        this.weights = weights;
    }

    public int getNumWeights()
    {
        return weights.length;
    }

    public double getFitness()
    {
        return fitness;
    }
}
