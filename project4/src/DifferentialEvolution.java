import java.util.Random;

public class DifferentialEvolution extends Trainer{
    private Chromosome[] population;
    private double beta;
    private double crossoverRate;

    public DifferentialEvolution(Chromosome[] population, double beta, double crossoverRate){
        this.population = population;
        this.beta = beta;
        this.crossoverRate = crossoverRate;
    }

    public Chromosome mutation(Chromosome[] population, Chromosome targetChromosome, int targetChromosomeIndex){
        Random random = new Random();
        int k1 = random.nextInt(population.length);
        while(k1 == targetChromosomeIndex){ //first chromosome can't be the same as target
            k1 = random.nextInt(population.length);
        }
        int k2 = random.nextInt(population.length);
        while(k2 == targetChromosomeIndex || k2 == k1){ //second chromosome can't be the same as target or first
            k2 = random.nextInt(population.length);
        }
        int k3 = random.nextInt(population.length);
        while(k3 == targetChromosomeIndex || k3 == k1 || k3 == k2){ //third chromosome can't be the same as target, first, or second
            k3 = random.nextInt(population.length);
        }

        Chromosome chromosome1 = population[k1];
        Chromosome chromosome2 = population[k2];
        Chromosome chromosome3 = population[k3];

        Chromosome trialChromosome = new Chromosome();
        double[] betaMultiplierWeights = new double[]{};

        for(int i = 0; i<chromosome2.getWeights().length; i++){
            double difference = chromosome2.getWeights()[i] - chromosome3.getWeights()[i];
            betaMultiplierWeights[i] = difference*beta;
        }

        double[] trialChromosomeWeights = new double[]{};

        for(int i = 0; i<chromosome1.getWeights().length; i++){
            double sum = chromosome1.getWeights()[i] + betaMultiplierWeights[i];
            trialChromosomeWeights[i] = sum;
        }
        trialChromosome.setWeights(trialChromosomeWeights);
        return trialChromosome;
    }

    public Chromosome binomialCrossover(Chromosome targetChromosome, Chromosome trialChromosome){
        Random random = new Random();
        Chromosome crossedChromosome = new Chromosome();
        double[] crossedChromosomeWeights = new double[]{};

        for(int i = 0; i<targetChromosome.getWeights().length; i++){
            double doCrossover = random.nextDouble();
            if(doCrossover < crossoverRate){
                crossedChromosomeWeights[i] = targetChromosome.getWeights()[i];
            }
            else{
                crossedChromosomeWeights[i] = trialChromosome.getWeights()[i];
            }
        }
        crossedChromosome.setWeights(crossedChromosomeWeights);
        return crossedChromosome;
    }

    public Chromosome[] elitistReplacement(Chromosome[] mutatedChildren){
        Chromosome[] newPopulation = new Chromosome[]{};
        for(int i = 0; i< population.length; i++){
            if(mutatedChildren[i].getFitness() < population[i].getFitness()){
                newPopulation[i] = mutatedChildren[i];
            }
            else{
                newPopulation[i] = population[i];
            }
        }
        return newPopulation;
    }

    @Override
    void train()
    {
        //TODO: run Neural Network with original population, setting the fitness
        Chromosome[] mutatedChildren = new Chromosome[]{};
        for(int i = 0; i< population.length; i++){
            Chromosome trialChromosome = mutation(population, population[i], i);
            Chromosome child = binomialCrossover(population[i], trialChromosome);
            mutatedChildren[i] = child;
        }
        //TODO: run Neural Network with mutated children, setting a fitness
        Chromosome[] newPopulation = elitistReplacement(mutatedChildren);
    }
}