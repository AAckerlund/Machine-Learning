import java.util.ArrayList;
import java.util.Random;

public class Genetic extends Trainer
{
    private Chromosome[] population;
    private final double crossoverRate, mutationRate, variance;
    private final int replacedIndividuals;

    public Genetic(Chromosome[] population, double crossoverRate, double mutationRate, double variance, int replacedIndividuals)
    {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.variance =  variance;
        this.replacedIndividuals = replacedIndividuals;
    }
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

        if(chromosome1.getFitness() < chromosome2.getFitness()){  //selects participant with highest fitness(lowest error)
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
        Random random = new Random();
        double selection = random.nextGaussian()*variance + gene;
        if(selection < 0){
            return 0;
        }
        if(selection < 1){
            return 1;
        }
        return selection;
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

    public Chromosome[] steadyStateReplacement(){
        //TODO: replace population with new offspring
        ArrayList<Chromosome> chromosomesToKill = new ArrayList<>();
        for(int i = 0; i<replacedIndividuals; i++) {
            double worstFitness = 0;
            Chromosome weakestChromosome = new Chromosome();
            for (int j = 0; j < population.length; j++) {
                if (population[j].getFitness() > worstFitness && !chromosomesToKill.contains(population[j])) {
                    weakestChromosome = population[j];
                    worstFitness = population[j].getFitness();
                }
            }
            chromosomesToKill.add(weakestChromosome);
        }
    }

    @Override
    void train()
    {
        //TODO: run Neural Network with original population, setting the fitness
        Chromosome[] mutatedChildren = new Chromosome[]{};
        ArrayList<Chromosome> mutatedChildrenList = new ArrayList<>();
        for(int i = 0; i<(population.length/2); i++){
            Chromosome father = tournamentSelection(population);
            Chromosome mother = tournamentSelection(population);
            while(father == mother){    //father and mother can not be the same
                mother = tournamentSelection(population);
            }
            Chromosome[] children = singlePointCrossover(father, mother, crossoverRate);
            for(int j = 0; j<children.length; j++){
                Chromosome mutatedChild = mutation(children[j], mutationRate, variance);
                mutatedChildrenList.add(mutatedChild);
            }
        }
        for(int i = 0; i<mutatedChildrenList.size(); i++){
            mutatedChildren[i] = mutatedChildrenList.get(i);//converts arraylist to list
        }



    }
}