import java.util.ArrayList;
import java.util.Random;

public class DifferentialEvolution extends Trainer{
    private ArrayList<Chromosome> population;
    private double beta;
    private double crossoverRate;
    private Network net;

    public DifferentialEvolution(ArrayList<Chromosome> population, double beta, double crossoverRate, Network net){
        this.population = population;
        this.beta = beta;
        this.crossoverRate = crossoverRate;
        this.net = net;
    }

    //uses random members from the population to create a new one, having a possibility to move on to next generation
    public Chromosome mutation(ArrayList<Chromosome> population, int targetChromosomeIndex){
        Random random = new Random();
        int k1 = random.nextInt(population.size());
        while(k1 == targetChromosomeIndex){ //first chromosome can't be the same as target
            k1 = random.nextInt(population.size());
        }
        int k2 = random.nextInt(population.size());
        while(k2 == targetChromosomeIndex || k2 == k1){ //second chromosome can't be the same as target or first
            k2 = random.nextInt(population.size());
        }
        int k3 = random.nextInt(population.size());
        while(k3 == targetChromosomeIndex || k3 == k1 || k3 == k2){ //third chromosome can't be the same as target, first, or second
            k3 = random.nextInt(population.size());
        }

        Chromosome chromosome1 = population.get(k1);
        Chromosome chromosome2 = population.get(k2);
        Chromosome chromosome3 = population.get(k3);

        Chromosome trialChromosome = new Chromosome();
        ArrayList<Double> betaMultiplierWeights = new ArrayList<>();
        for(int i = 0; i<chromosome2.getWeights().size(); i++){
            double difference = Math.abs(chromosome2.getWeights().get(i) - chromosome3.getWeights().get(i));
            betaMultiplierWeights.add(difference*beta);
        }
        ArrayList<Double> trialChromosomeWeights = new ArrayList<>();

        for(int i = 0; i<chromosome1.getWeights().size(); i++){
            double sum = chromosome1.getWeights().get(i) + betaMultiplierWeights.get(i);
            trialChromosomeWeights.add(sum);
        }
        trialChromosome.setWeights(trialChromosomeWeights);
        return trialChromosome; //mutated chromosome
    }

    //uses crossover rate to determine which genes are mutated or not
    public Chromosome binomialCrossover(Chromosome targetChromosome, Chromosome trialChromosome){
        Random random = new Random();
        Chromosome crossedChromosome = new Chromosome();
        ArrayList<Double> crossedChromosomeWeights = new ArrayList<>();

        for(int i = 0; i<targetChromosome.getWeights().size(); i++){    //all genes in chromosome
            double doCrossover = random.nextDouble();
            if(doCrossover < crossoverRate){
                crossedChromosomeWeights.add(targetChromosome.getWeights().get(i)); //no mutation to gene
            }
            else{
                crossedChromosomeWeights.add(trialChromosome.getWeights().get(i));  //mutation to gene
            }
        }
        crossedChromosome.setWeights(crossedChromosomeWeights);
        return crossedChromosome;
    }

    //if the child generated by the population member is stronger than that member, it is replaced by the child
    public ArrayList<Chromosome> elitistReplacement(ArrayList<Chromosome> mutatedChildren){
        ArrayList<Chromosome> newPopulation = new ArrayList<>();
        for(int i = 0; i< population.size(); i++){
            if(mutatedChildren.get(i).getFitness() < population.get(i).getFitness()){
                newPopulation.add(mutatedChildren.get(i));  //member replaced by child
            }
            else{
                newPopulation.add(population.get(i));   //child doesn't make it to next generation
            }
        }
        return newPopulation;
    }

    //trains neural network with differential evolution algorithm
    @Override
    void train(ArrayList<Node> trainingSet)
    {
        for(int j = 0; j<15; j++) { //15 generations
            for (Chromosome member : population) {  //assigns population genes to neural network, MSE is set as the fitness of a chromosome
                net.updateWeights(member);
                member.setFitness(net.calculateMSError(trainingSet));
            }

            ArrayList<Chromosome> mutatedChildren = new ArrayList<>();  //children generated from parents
            for (int i = 0; i < population.size(); i++) {   //child for every population member
                Chromosome trialChromosome = mutation(population, i);
                Chromosome child = binomialCrossover(population.get(i), trialChromosome);
                mutatedChildren.add(child);
            }

            for (Chromosome mutatedChild : mutatedChildren) {   //children ran on neural network to set fitness using MSE
                net.updateWeights(mutatedChild);
                mutatedChild.setFitness(net.calculateMSError(trainingSet));
            }

            ArrayList<Chromosome> newPopulation = elitistReplacement(mutatedChildren);  //next generation is generated
            population = newPopulation; //next generation is ready to create more children
        }
    }
}