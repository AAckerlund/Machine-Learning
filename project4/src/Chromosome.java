import java.util.ArrayList;

public class Chromosome {
    //private double[] weights;
    private ArrayList<Double> weights;
    private double fitness;

    public Chromosome()
    {
    }

    /*public Chromosome(int weightSize)
    {
        weights = new double[weightSize];
    }*/

    public Chromosome(ArrayList<Double> weights)
    {
        this.weights = weights;
    }

    public ArrayList<Double> getWeights()
    {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights)
    {
        this.weights = weights;
    }

    public void addWeight(double weight){this.weights.add(weight);}

    public int getNumWeights()
    {
        return weights.size();
    }

    public double getFitness()
    {
        return fitness;
    }

    public void setFitness(double fitness) {this.fitness = fitness;}
}
