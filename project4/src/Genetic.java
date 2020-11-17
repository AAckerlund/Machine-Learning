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

        if(chromosome1.getFitness() > chromosome2.getFitness()){  //selects participant with highest fitness
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

            int splitPoint = random.nextInt(father.getNumWeights());
            while(splitPoint == father.getNumWeights()-1){   //can not split at the end of chromosome
                splitPoint = random.nextInt(father.getNumWeights());
            }
            for(int i = 0; i<father.getNumWeights(); i++){
                double fatherGene = father.getWeights()[i];
                double motherGene = mother.getWeights()[i];
                if(i>splitPoint){
                    child1.getWeights()[i] = motherGene;
                    child2.getWeights()[i] = fatherGene;
                }
                else{
                    child1.getWeights()[i] = fatherGene;
                    child2.getWeights()[i] = motherGene;
                }
            }
            return new Chromosome[]{child1, child2};    //children created using crossover of father and mother genes
        }
    }

    public double normalDistributionSelection(double gene, double variance){
        //TODO:return value based on normal distribution with given gene as the mean, variance will be tuned
        return 0;
    }

    public Chromosome mutation(Chromosome chromosome, double mutationRate, double variance){
        Chromosome mutatedChromosome = new Chromosome();
        for(int i = 0; i<chromosome.getNumWeights(); i++){
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
                double mutatedGene = normalDistributionSelection(chromosome.getWeights()[i], variance);
                mutatedChromosome.getWeights()[i] = mutatedGene;
            }
            else{
                mutatedChromosome.getWeights()[i] = chromosome.getWeights()[i];   //no mutation
            }
        }
        return mutatedChromosome;
    }

    public Chromosome[] replacement(){
        //TODO: replace population with new offspring
        return null;
    }

    public void runGenetic(Chromosome[] population, double probabilityOfCrossover, double mutationRate, double variance) {
        Chromosome father = tournamentSelection(population);
        Chromosome mother = tournamentSelection(population);
        while(father == mother){    //father and mother can not be the same
            mother = tournamentSelection(population);
        }
        Chromosome[] children = singlePointCrossover(father, mother, probabilityOfCrossover);
        for(Chromosome child: children){
            Chromosome mutatedChild = mutation(child, mutationRate, variance);
        }
    }
}