public class Chromosome {
    public double[] weights;
    public double fitness;

    public Chromosome(int weightSize)
    {
        weights = new double[weightSize];
    }

    public Chromosome(double[] weights)
    {
        this.weights = weights;
    }
}
