public class Genetic {
    public void runGenetic(Chromosome[] population) {
        Selection s = new Selection();
        Chromosome parent1 = new Chromosome();
        Chromosome parent2 = new Chromosome();
        parent1 = s.tournamentSelection(population);
        parent2 = s.tournamentSelection(population);
        while(parent1 == parent2){
            parent2 = s.tournamentSelection(population);
        }
    }
}
