import java.util.ArrayList;
import java.util.Random;

public class Genetic extends Trainer
{
    private ArrayList<Chromosome> population;
    private final double crossoverRate, mutationRate, variance;
    private final int replacedIndividuals;
    private Network net;

    public Genetic(ArrayList<Chromosome> population, double crossoverRate, double mutationRate, double variance, int replacedIndividuals, Network net)
    {
        this.population = population;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.variance =  variance;
        this.replacedIndividuals = replacedIndividuals;
        this.net = net;
    }
    public Chromosome tournamentSelection(ArrayList<Chromosome> population){
        Random random = new Random();
        int k1 = random.nextInt(population.size()); //randomly selects first tournament participant
        int k2 = random.nextInt(population.size()); //randomly selects second tournament participant
        while(k1 == k2){    //first and second participants can not be the same
            k2 = random.nextInt(population.size());
        }
        //finds participants in population
        Chromosome chromosome1 = population.get(k1);
        Chromosome chromosome2 = population.get(k2);

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
            Chromosome child1 = new Chromosome(new ArrayList<>());
            Chromosome child2 = new Chromosome(new ArrayList<>());

            int splitPoint = random.nextInt(father.getNumWeights());
            while(splitPoint == father.getNumWeights()-1){   //can not split at the end of chromosome
                splitPoint = random.nextInt(father.getNumWeights());
            }
            for(int i = 0; i<father.getNumWeights(); i++){
                double fatherGene = father.getWeights().get(i);
                double motherGene = mother.getWeights().get(i);
                if(i>splitPoint){
                    child1.addWeight(motherGene);
                    child2.addWeight(fatherGene);
                }
                else{
                    child1.addWeight(fatherGene);
                    child2.addWeight(motherGene);
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
        if(selection > 1){
            return 1;
        }
        return selection;
    }

    public Chromosome mutation(Chromosome chromosome, double mutationRate, double variance){
        Chromosome mutatedChromosome = new Chromosome(new ArrayList<>());
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
                double mutatedGene = normalDistributionSelection(chromosome.getWeights().get(i), variance);
                mutatedChromosome.addWeight(mutatedGene);
            }
            else{
                mutatedChromosome.addWeight(chromosome.getWeights().get(i));    //no mutation, take from original chromosome
            }
        }
        return mutatedChromosome;
    }

    public ArrayList<Chromosome> steadyStateReplacement(ArrayList<Chromosome> mutatedChildren){
        ArrayList<Chromosome> chromosomesToKill = new ArrayList<>();
        ArrayList<Chromosome> chromosomesToSurvive = new ArrayList<>();
        for(int i = 0; i<replacedIndividuals; i++) {
            double worstFitness = 0;
            Chromosome weakestChromosome = new Chromosome();
            for (int j = 0; j < population.size(); j++) {
                if (population.get(j).getFitness() > worstFitness && !chromosomesToKill.contains(population.get(j))) {
                    weakestChromosome = population.get(j);
                    worstFitness = population.get(j).getFitness();
                }
            }
            chromosomesToKill.add(weakestChromosome);
        }
        for(int i = 0; i<population.size(); i++){
            if(!chromosomesToKill.contains(population.get(i))){
                chromosomesToSurvive.add(population.get(i));
            }
        }

        ArrayList<Chromosome> bestOfChildrenAndParents = new ArrayList<>();

        for(int i = 0; i<mutatedChildren.size(); i++){
            Chromosome best = new Chromosome();
            best.setFitness(Double.POSITIVE_INFINITY);
            double bestFitness = best.getFitness();
            for(int j = 0; j<mutatedChildren.size(); j++){
                if(mutatedChildren.get(j).getFitness() < bestFitness){
                    bestFitness = mutatedChildren.get(j).getFitness();
                    best = mutatedChildren.get(j);
                }
            }
            for(int j = 0; j<chromosomesToKill.size(); j++){
                if(chromosomesToKill.get(j).getFitness() < bestFitness){
                    bestFitness = mutatedChildren.get(j).getFitness();
                    best = mutatedChildren.get(j);
                }
            }
            bestOfChildrenAndParents.add(best);
        }

        ArrayList<Chromosome> newPopulationList = new ArrayList<>();
        for(Chromosome best: bestOfChildrenAndParents){
            newPopulationList.add(best);
        }
        for(Chromosome chromosomeToSurvive: chromosomesToSurvive){
            newPopulationList.add(chromosomeToSurvive);
        }
        return newPopulationList;
    }

    @Override
    void train(ArrayList<Node> trainingSet)
    {
        double bestMSE = Double.POSITIVE_INFINITY;
        //min iteration (15 to 20)

        for(int j = 0; j<15; j++) {
            for (Chromosome member : population) {
                net.updateWeights(member);
                member.setFitness(net.calculateMSError(trainingSet));
            }

        /*for(Chromosome member: population){
            System.out.println(member.getWeights());
        }
        System.out.println();*/

            ArrayList<Chromosome> mutatedChildren = new ArrayList<>();
            for (int i = 0; i < ((population.size() - replacedIndividuals) / 2); i++) {
                Chromosome father = tournamentSelection(population);
                Chromosome mother = tournamentSelection(population);
                while (father == mother) {    //father and mother can not be the same
                    mother = tournamentSelection(population);
                }
                Chromosome[] children = singlePointCrossover(father, mother, crossoverRate);
                for (int k = 0; k < children.length; k++) {
                    Chromosome mutatedChild = mutation(children[j], mutationRate, variance);
                    mutatedChildren.add(mutatedChild);
                }
            }

            for (Chromosome mutatedChild : mutatedChildren) {
                net.updateWeights(mutatedChild);
                mutatedChild.setFitness(net.calculateMSError(trainingSet));
            }


            ArrayList<Chromosome> newPopulation = steadyStateReplacement(mutatedChildren);
            population = newPopulation;

            for (Chromosome member : population) {
                if (member.getFitness() < bestMSE) {
                    bestMSE = member.getFitness();
                }
            }
            System.out.println(bestMSE);
        }


        /*for(Chromosome member: newPopulation){
            System.out.println(member.getWeights());
        }
        System.out.println();*/

    }
}