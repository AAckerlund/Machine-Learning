import java.util.ArrayList;
import java.util.Random;

public class DifferentialEvolution extends Trainer{
    private ArrayList<Chromosome> population;
    private double beta;
    private double crossoverRate;

    public DifferentialEvolution(ArrayList<Chromosome> population, double beta, double crossoverRate){
        this.population = population;
        this.beta = beta;
        this.crossoverRate = crossoverRate;
    }

    public Chromosome mutation(ArrayList<Chromosome> population, Chromosome targetChromosome, int targetChromosomeIndex){
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
        //double[] betaMultiplierWeights = new double[]{};
        ArrayList<Double> betaMultiplierWeights = new ArrayList<>();
        for(int i = 0; i<chromosome2.getWeights().size(); i++){
            double difference = Math.abs(chromosome2.getWeights().get(i) - chromosome3.getWeights().get(i));
            //betaMultiplierWeights[i] = difference*beta;
            betaMultiplierWeights.add(difference*beta);
        }

        //double[] trialChromosomeWeights = new double[]{};
        ArrayList<Double> trialChromosomeWeights = new ArrayList<>();

        for(int i = 0; i<chromosome1.getWeights().size(); i++){
            double sum = chromosome1.getWeights().get(i) + betaMultiplierWeights.get(i);
            //trialChromosomeWeights[i] = sum;
            trialChromosomeWeights.add(sum);
        }
        trialChromosome.setWeights(trialChromosomeWeights);
        return trialChromosome;
    }

    public Chromosome binomialCrossover(Chromosome targetChromosome, Chromosome trialChromosome){
        Random random = new Random();
        Chromosome crossedChromosome = new Chromosome();
        //double[] crossedChromosomeWeights = new double[]{};
        ArrayList<Double> crossedChromosomeWeights = new ArrayList<>();

        for(int i = 0; i<targetChromosome.getWeights().size(); i++){
            double doCrossover = random.nextDouble();
            if(doCrossover < crossoverRate){
                //crossedChromosomeWeights.get(i) = targetChromosome.getWeights().get(i);
                crossedChromosomeWeights.add(targetChromosome.getWeights().get(i));
            }
            else{
                //crossedChromosomeWeights[i] = trialChromosome.getWeights().get(i);
                crossedChromosomeWeights.add(trialChromosome.getWeights().get(i));
            }
        }
        crossedChromosome.setWeights(crossedChromosomeWeights);
        return crossedChromosome;
    }

    public ArrayList<Chromosome> elitistReplacement(ArrayList<Chromosome> mutatedChildren){
        ArrayList<Chromosome> newPopulation = new ArrayList<>();
        for(int i = 0; i< population.size(); i++){
            if(mutatedChildren.get(i).getFitness() < population.get(i).getFitness()){
                newPopulation.add(mutatedChildren.get(i));
            }
            else{
                newPopulation.add(population.get(i));
            }
        }
        return newPopulation;
    }

    @Override
    void train(ArrayList<Node> trainingSet)
    {
        //TODO: run Neural Network with original population, setting the fitness


        /*for(Chromosome member: population){
            System.out.println(member.getWeights());
        }
        System.out.println();*/

        ArrayList<Chromosome> mutatedChildren = new ArrayList<>();
        for(int i = 0; i< population.size(); i++){
            Chromosome trialChromosome = mutation(population, population.get(i), i);
            Chromosome child = binomialCrossover(population.get(i), trialChromosome);
            mutatedChildren.add(child);
        }
        //TODO: run Neural Network with mutated children, setting a fitness
        ArrayList<Chromosome> newPopulation = elitistReplacement(mutatedChildren);


        /*for(Chromosome member: newPopulation){
            System.out.println(member.getWeights());
        }
        System.out.println();*/
    }
}