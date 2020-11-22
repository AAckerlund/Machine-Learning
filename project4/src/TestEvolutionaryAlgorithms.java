import java.util.ArrayList;

public class TestEvolutionaryAlgorithms {
    public static void main(String[] args) {
        Chromosome Chromosome1 = new Chromosome();
        Chromosome Chromosome2 = new Chromosome();
        Chromosome Chromosome3 = new Chromosome();
        Chromosome Chromosome4 = new Chromosome();
        Chromosome Chromosome5 = new Chromosome();
        Chromosome Chromosome6 = new Chromosome();
        Chromosome Chromosome7 = new Chromosome();
        Chromosome Chromosome8 = new Chromosome();

        ArrayList<Double> c1weights = new ArrayList<>();
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);
        c1weights.add(0.1);

        ArrayList<Double> c2weights = new ArrayList<>();
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);
        c2weights.add(0.2);

        ArrayList<Double> c3weights = new ArrayList<>();
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);
        c3weights.add(0.3);

        ArrayList<Double> c4weights = new ArrayList<>();
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);
        c4weights.add(0.4);

        ArrayList<Double> c5weights = new ArrayList<>();
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);
        c5weights.add(0.1);

        ArrayList<Double> c6weights = new ArrayList<>();
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);
        c6weights.add(0.2);

        ArrayList<Double> c7weights = new ArrayList<>();
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);
        c7weights.add(0.3);

        ArrayList<Double> c8weights = new ArrayList<>();
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);
        c8weights.add(0.4);

        Chromosome1.setWeights(c1weights);
        Chromosome2.setWeights(c2weights);
        Chromosome3.setWeights(c3weights);
        Chromosome4.setWeights(c4weights);
        Chromosome5.setWeights(c5weights);
        Chromosome6.setWeights(c6weights);
        Chromosome7.setWeights(c7weights);
        Chromosome8.setWeights(c8weights);

        Chromosome1.setFitness(0.87);
        Chromosome2.setFitness(0.12);
        Chromosome3.setFitness(0.24);
        Chromosome4.setFitness(0.63);
        Chromosome5.setFitness(0.43);
        Chromosome6.setFitness(0.23);
        Chromosome7.setFitness(0.89);
        Chromosome8.setFitness(0.26);

        ArrayList<Chromosome> population = new ArrayList<>();
        population.add(Chromosome1);
        population.add(Chromosome2);
        population.add(Chromosome3);
        population.add(Chromosome4);
        population.add(Chromosome5);
        population.add(Chromosome6);
        population.add(Chromosome7);
        population.add(Chromosome8);

        //Network nn = new Network();

        //Genetic genetic = new Genetic(population, 1, 1, 0.1, 4);
        //DifferentialEvolution diffev = new DifferentialEvolution(population, 1, 0.8);
        //genetic.train();
        //diffev.train();


    }
}


