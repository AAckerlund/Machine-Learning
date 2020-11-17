import java.util.Random;

public class Genetic {

    public Chromosome tournamentSelection(Chromosome[] population){
        Random random = new Random();
        int k1 = random.nextInt(population.length);
        int k2 = random.nextInt(population.length);
        while(k1 == k2){
            k2 = random.nextInt(population.length);
        }
        Chromosome chromosome1 = population[k1];
        Chromosome chromosome2 = population[k2];

        if(chromosome1.fitness > chromosome2.fitness){
            return chromosome1;
        }
        else{
            return chromosome2;
        }
    }

    public void runGenetic(Chromosome[] population) {
        Chromosome parent1 = new Chromosome();
        Chromosome parent2 = new Chromosome();
        parent1 = tournamentSelection(population);
        parent2 = tournamentSelection(population);
        while(parent1 == parent2){
            parent2 = tournamentSelection(population);
        }
    }
}
