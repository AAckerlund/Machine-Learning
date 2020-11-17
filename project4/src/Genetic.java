public class Genetic {
    public void runGenetic(Chromosome[] population) {
        Selection s = new Selection();
        Chromosome parent1 = s.tournamentSelection(population);
        Chromosome parent2 = s.tournamentSelection(population);
        while(parent1 == parent2){
            parent2 = s.tournamentSelection(population);
        }
    }
}
