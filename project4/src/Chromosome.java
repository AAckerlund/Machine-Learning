import java.util.ArrayList;

public class Chromosome {
    //private double[] weights;
    private ArrayList<Double> weights;
    private double fitness;

    public Chromosome()
    {
    }

    public Chromosome(int weightSize)
    {
        weights = new ArrayList<>();
        for(int i = 0; i < weightSize; i++)
        {
            weights.add(Math.random());
        }
    }

    public Chromosome(ArrayList<Double> weights)
    {
        this.weights = weights;
    }

    // Overloaded constructor to take double array arg
    public Chromosome(double[] weights)
    {
        setWeights(weights);
    }
    public ArrayList<Double> getWeights()
    {
        return weights;
    }

    public void setWeights(ArrayList<Double> weights)
    {
        this.weights = weights;
    }

    // Overloaded to make this work with double arrays in PSO
    public void setWeights(double[] weights)
    {
        ArrayList<Double> newWeights = new ArrayList<>();
        for (int i = 0; i < weights.length; i++)
        {
            newWeights.add(weights[i]);
        }
        this.weights = newWeights;
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
