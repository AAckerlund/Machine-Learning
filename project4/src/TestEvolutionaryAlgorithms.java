import java.util.ArrayList;

public class TestEvolutionaryAlgorithms {
    public static void main(String[] args) {
        Chromosome Chromosome1 = new Chromosome();
        Chromosome Chromosome2 = new Chromosome();
        Chromosome Chromosome3 = new Chromosome();
        Chromosome Chromosome4 = new Chromosome();

        ArrayList<Double> c1weights = new ArrayList<>();
        c1weights.add(0.1);
        c1weights.add(0.1);

        ArrayList<Double> c2weights = new ArrayList<>();
        c2weights.add(0.2);
        c2weights.add(0.2);

        ArrayList<Double> c3weights = new ArrayList<>();
        c3weights.add(0.3);
        c3weights.add(0.3);

        ArrayList<Double> c4weights = new ArrayList<>();
        c4weights.add(0.4);
        c4weights.add(0.4);

        Chromosome1.setWeights(c1weights);
        Chromosome2.setWeights(c2weights);
        Chromosome3.setWeights(c3weights);
        Chromosome4.setWeights(c4weights);

        Chromosome1.setFitness(0.5);
        Chromosome2.setFitness(0.6);
        Chromosome3.setFitness(0.7);
        Chromosome4.setFitness(0.8);

        ArrayList<Chromosome> population = new ArrayList<>();
        population.add(Chromosome1);
        population.add(Chromosome2);
        population.add(Chromosome3);
        population.add(Chromosome4);

        Genetic genetic = new Genetic(population, 1, 1, 0.1, 2);
        DifferentialEvolution diffev = new DifferentialEvolution(population, 1, 0.8);
        genetic.train();
        diffev.train();
    }
}


