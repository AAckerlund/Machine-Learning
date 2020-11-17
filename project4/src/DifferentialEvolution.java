import java.util.Random;

public class DifferentialEvolution {
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
        double beta = 2*random.nextDouble();    //random decimal value between 0 and 2
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

    public Chromosome binomialCrossover(Chromosome targetChromosome, Chromosome trialChromosome, double mutationProbability){
        boolean chooseTrialChromosome;
        Random random = new Random();
        double chanceOfCrossover = random.nextDouble();
        if(chanceOfCrossover <= mutationProbability){
            chooseTrialChromosome = true;
        }
        else{
            chooseTrialChromosome = false;
        }
        if(chooseTrialChromosome){
            return trialChromosome;
        }
        else{
            return targetChromosome;
        }
    }

    public Chromosome[] replacement(){
        //TODO: replace population with new offspring
        return null;
    }

    public void runDifferentialEvolution(Chromosome[] population, double mutationProbability){
        Chromosome[] mutatedChildren = new Chromosome[]{};
        for(int i = 0; i< population.length; i++){
            Chromosome trialChromosome = mutation(population, population[i], i);
            Chromosome child = binomialCrossover(population[i], trialChromosome, mutationProbability);
        }
    }
}
