import java.util.Random;

public class Genetic {

    public Chromosome tournamentSelection(Chromosome[] population){
        Random random = new Random();
        int k1 = random.nextInt(population.length); //randomly selects first tournament participant
        int k2 = random.nextInt(population.length); //randomly selects second tournament participant
        while(k1 == k2){    //first and second participants can not be the same
            k2 = random.nextInt(population.length);
        }
        //finds participants in population
        Chromosome chromosome1 = population[k1];
        Chromosome chromosome2 = population[k2];

        if(chromosome1.fitness > chromosome2.fitness){  //selects participant with highest fitness
            return chromosome1;
        }
        else{
            return chromosome2;
        }
    }

    public Chromosome[] singlePointCrossover(Chromosome father, Chromosome mother, double probabilityOfCrossover){
        boolean doCrossover;
        Random random = new Random();
        double chanceOfCrossover = random.nextDouble();
        if(chanceOfCrossover <= probabilityOfCrossover){
            doCrossover = true;
        }
        else{
            doCrossover = false;
        }
        if(!doCrossover){
            return new Chromosome[]{father, mother};    //no offspring is created, father and mother remain in the population
        }
        else{
            Chromosome child1 = new Chromosome();
            Chromosome child2 = new Chromosome();

            int splitPoint = random.nextInt(father.weights.length);
            while(splitPoint == father.weights.length-1){   //can not split at the end of chromosome
                splitPoint = random.nextInt(father.weights.length);
            }
            for(int i = 0; i<father.weights.length; i++){
                double fatherGene = father.weights[i];
                double motherGene = mother.weights[i];
                if(i>splitPoint){
                    child1.weights[i] = motherGene;
                    child2.weights[i] = fatherGene;
                }
                else{
                    child1.weights[i] = fatherGene;
                    child2.weights[i] = motherGene;
                }
            }
            return new Chromosome[]{child1, child2};    //children created using crossover of father and mother genes
        }
    }

    public double normalDistributionSelection(double gene, double variance){
        //TODO:return value based on normal distribution with given gene as the mean, variance will be tuned
    }

    public Chromosome mutation(Chromosome chromosome, double mutationRate, double variance){
        Chromosome mutatedChromosome = new Chromosome();
        for(int i = 0; i<chromosome.weights.length; i++){
            boolean doMutation;
            Random random = new Random();
            double chanceOfMutation = random.nextDouble();
            if(chanceOfMutation <= mutationRate){
                doMutation = true;
            }
            else{
                doMutation = false;
            }
            if(doMutation){
                double mutatedGene = normalDistributionSelection(chromosome.weights[i], variance);
                mutatedChromosome.weights[i] = mutatedGene;
            }
            else{
                mutatedChromosome.weights[i] = chromosome.weights[i];   //no mutation
            }
        }
        return mutatedChromosome;
    }

    public void runGenetic(Chromosome[] population) {
        Chromosome father = tournamentSelection(population);
        Chromosome mother = tournamentSelection(population);
        while(father == mother){    //father and mother can not be the same
            mother = tournamentSelection(population);
        }
    }
}