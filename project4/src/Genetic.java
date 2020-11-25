import java.util.ArrayList;
import java.util.Random;

public class Genetic extends Trainer {
    private ArrayList<Chromosome> population;
    private final double crossoverRate, mutationRate, variance;
    private final int replacedIndividuals;
    private Network net;

    public Genetic(ArrayList<Chromosome> population, double crossoverRate, double mutationRate, double variance, int replacedIndividuals, Network net) {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.variance = variance;
        this.replacedIndividuals = replacedIndividuals;
        this.net = net;
    }

    //selects parents for generating children
    public Chromosome tournamentSelection(ArrayList<Chromosome> population) {
        Random random = new Random();
        int k1 = random.nextInt(population.size()); //randomly selects first tournament participant
        int k2 = random.nextInt(population.size()); //randomly selects second tournament participant
        while (k1 == k2) {    //first and second participants can not be the same
            k2 = random.nextInt(population.size());
        }
        //finds participants in population
        Chromosome chromosome1 = population.get(k1);
        Chromosome chromosome2 = population.get(k2);

        if (chromosome1.getFitness() < chromosome2.getFitness()) {  //selects participant with highest fitness(lowest error)
            return chromosome1;
        } else {
            return chromosome2;
        }
    }

    //crosses the genes of the father and mother to create children
    public Chromosome[] singlePointCrossover(Chromosome father, Chromosome mother, double probabilityOfCrossover) {
        boolean doCrossover;
        Random random = new Random();
        double chanceOfCrossover = random.nextDouble();
        if (chanceOfCrossover <= probabilityOfCrossover) {
            doCrossover = true;
        } else {
            doCrossover = false;
        }
        if (!doCrossover) {
            return new Chromosome[]{father, mother};    //no offspring is created, father and mother remain in the population
        } else {
            Chromosome child1 = new Chromosome(new ArrayList<>());
            Chromosome child2 = new Chromosome(new ArrayList<>());

            int splitPoint = random.nextInt(father.getNumWeights());
            while (splitPoint == father.getNumWeights() - 1) {   //can not split at the end of chromosome
                splitPoint = random.nextInt(father.getNumWeights());
            }
            for (int i = 0; i < father.getNumWeights(); i++) {
                double fatherGene = father.getWeights().get(i);
                double motherGene = mother.getWeights().get(i);
                if (i > splitPoint) {
                    child1.addWeight(motherGene);
                    child2.addWeight(fatherGene);
                } else {
                    child1.addWeight(fatherGene);
                    child2.addWeight(motherGene);
                }
            }
            return new Chromosome[]{child1, child2};    //children created using crossover of father and mother genes
        }
    }

    //uses a Gaussian distribution to vary weights for mutation
    public double normalDistributionSelection(double gene, double variance) {
        Random random = new Random();
        double selection = random.nextGaussian() * variance + gene;   //selects random point on distribution
        if (selection < 0) {  //a weight can not be lower than 0
            return 0;
        }
        if (selection > 1) {  //a weight can not be higher than 1
            return 1;
        }
        return selection;
    }

    //changes genes of a population member to explore more weight possibilites on the neural network
    public Chromosome mutation(Chromosome chromosome, double mutationRate, double variance) {
        Chromosome mutatedChromosome = new Chromosome(new ArrayList<>());
        for (int i = 0; i < chromosome.getNumWeights(); i++) {
            boolean doMutation;
            Random random = new Random();
            double chanceOfMutation = random.nextDouble();
            if (chanceOfMutation <= mutationRate) {
                doMutation = true;
            } else {
                doMutation = false;
            }
            if (doMutation) {
                double mutatedGene = normalDistributionSelection(chromosome.getWeights().get(i), variance);
                mutatedChromosome.addWeight(mutatedGene);   //takes value from normal distribution
            } else {
                mutatedChromosome.addWeight(chromosome.getWeights().get(i));    //no mutation, take from original chromosome
            }
        }
        return mutatedChromosome;
    }

    //evaluates fitness of children generated from parents and current population
    public ArrayList<Chromosome> generationalReplacement(ArrayList<Chromosome> mutatedChildren) {
        ArrayList<Chromosome> chromosomesToSurvive = new ArrayList<>(); //strongest from original population
        for (int i = 0; i < replacedIndividuals; i++) {
            double bestFitness = Double.POSITIVE_INFINITY;

            Chromosome strongestChromosome = new Chromosome();
            for (int j = 0; j < population.size(); j++) {
                if (population.get(j).getFitness() < bestFitness && !chromosomesToSurvive.contains(population.get(j))) {
                    strongestChromosome = population.get(j);
                    bestFitness = population.get(j).getFitness();
                }
            }
            chromosomesToSurvive.add(strongestChromosome);
        }

        ArrayList<Chromosome> newPopulationList = new ArrayList<>();
        for (Chromosome mutatedChild : mutatedChildren) {
            newPopulationList.add(mutatedChild);    //all children move on to next generation, high selection pressure
        }
        for (Chromosome chromosomeToSurvive : chromosomesToSurvive) {
            newPopulationList.add(chromosomeToSurvive); //strongest from original population move on to next generation
        }
        return newPopulationList;
    }

    //trains neural network with genetic algorithm
    @Override
    void train(ArrayList<Node> trainingSet) {
        for (int j = 0; j < 15; j++) { //15 generations
            for (Chromosome member : population) {  //assigns population genes to neural network, MSE is set as the fitness of a chromosome
                net.updateWeights(member);
                member.setFitness(net.calculateMSError(trainingSet));
            }

            ArrayList<Chromosome> mutatedChildren = new ArrayList<>();
            for (int i = 0; i < ((population.size() - replacedIndividuals) / 2); i++) {
                Chromosome father = tournamentSelection(population);
                Chromosome mother = tournamentSelection(population);
                while (father == mother) {    //father and mother can not be the same
                    mother = tournamentSelection(population);
                }
                Chromosome[] children = singlePointCrossover(father, mother, crossoverRate);    //children created from parents
                for (int k = 0; k < children.length; k++) { //all children mutated
                    Chromosome mutatedChild = mutation(children[k], mutationRate, variance);
                    mutatedChildren.add(mutatedChild);
                }
            }

            for (Chromosome mutatedChild : mutatedChildren) {   //children ran on neural network to set fitness using MSE
                net.updateWeights(mutatedChild);
                mutatedChild.setFitness(net.calculateMSError(trainingSet));
            }
    
            population = generationalReplacement(mutatedChildren); //next generation is ready to create more children
        }
    }
}