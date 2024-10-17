import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class Pair<A, B> {
    public A first;
    public B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}

public class HarmonySearch{
    private int populationSize;
    private double hmcr;
    private double par;
    private List<Integer> listSensors;
    private ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets;
    private ArrayList<Pair<Integer, ArrayList<Double>>> listPercents;
    private int numTargets;

    public HarmonySearch(int populationSize, double hmcr, double par, List<Integer> listSensors, ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets, ArrayList<Pair<Integer, ArrayList<Double>>> listPercents, int numTargets) {
        this.populationSize = populationSize;
        this.hmcr = hmcr;
        this.par = par;
        this.listSensors = listSensors;
        this.listTargets = listTargets;
        this.numTargets = numTargets;
        this.listPercents = listPercents;
    }

    public Population initPopulation() {
        return new Population(populationSize, listSensors.size(), listSensors, listTargets, listPercents, numTargets);
    }

    public Individual getFittest(Population population) {
        Individual[] individuals = population.getPopulation();
        int fittestIndex = 0;
        int maxFitness = individuals[0].getFitness();

        for (int i = 1; i < populationSize; i++) {
            int fitness = individuals[i].getFitness();
            if (fitness > maxFitness) {
                maxFitness = fitness;
                fittestIndex = i;
            }
        }

        return individuals[fittestIndex];
    }

    public Individual Crossover(Population population) {
        Individual newIndividual = new Individual(listTargets, listPercents, numTargets);
    
        // Tạo danh sách chưa được sử dụng ban đầu
        List<Integer> unusedGenes = new ArrayList<>(listSensors);
    
        for (int i = 0; i < listSensors.size(); i++) {
            if (Math.random() < hmcr && !unusedGenes.isEmpty()) {
                // Crossover
                int randomIndex = new Random().nextInt(unusedGenes.size());
                int gene = unusedGenes.remove(randomIndex); 
    
                newIndividual.setSensor(i, gene);
            } else {
                
                if (!unusedGenes.isEmpty()) {
                    int randomIndex = new Random().nextInt(unusedGenes.size());
                    int gene = unusedGenes.remove(randomIndex);
                    newIndividual.setSensor(i, gene);
                } else {
                    
                    int randomGene = listSensors.get(new Random().nextInt(listSensors.size()));
                    newIndividual.setSensor(i, randomGene);
                }
            }
        }
    
        return newIndividual;
    }
    
    
    

    public Individual mutate(Individual individual) {
        Individual mutatedIndividual = new Individual(individual.getIndividual(), listTargets, listPercents, numTargets);

        if (Math.random() < par) {
            // Mutate
            for (int i = 0; i < listSensors.size(); i++) {
                if (Math.random() < par) {
                    
                    int coin = new Random().nextInt(2);

                    if (coin == 1) {
                        int randomPosition = new Random().nextInt(listSensors.size());
                        int temp = mutatedIndividual.getSensor(i);
                        mutatedIndividual.setSensor(i, mutatedIndividual.getSensor(randomPosition));
                        mutatedIndividual.setSensor(randomPosition, temp);
                    }
                }
            }
        }

        return mutatedIndividual;
    }

    public Individual mutated(Individual individual) {
        // Đột biến bằng cách tráo đổi 2 gen
        int pos1 = new Random().nextInt(individual.getLength());
        int pos2 = new Random().nextInt(individual.getLength());
    
        List<Integer> mutatedGenes = new ArrayList<>(individual.getIndividual());
        Collections.swap(mutatedGenes, pos1, pos2);
    
        return new Individual(mutatedGenes, listTargets, listPercents,numTargets);
    }
    

    public Population replaceWorst(Population population, Individual newIndividual) {
        int worstIndex = findWorstIndex(population);
        int worstFitness = population.getIndividual(worstIndex).getFitness();
        int newFitness = newIndividual.getFitness();

        if (newFitness > worstFitness) {
            population.setIndividual(worstIndex, newIndividual);
        }

        return population;
    }

    private int findWorstIndex(Population population) {
        int worstIndex = 0;
        int minFitness = population.getIndividual(0).getFitness();

        for (int i = 1; i < populationSize; i++) {
            int fitness = population.getIndividual(i).getFitness();
            if (fitness < minFitness) {
                minFitness = fitness;
                worstIndex = i;
            }
        }

        return worstIndex;
    }
}
