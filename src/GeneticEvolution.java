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

public class GeneticEvolution {
    private double crossoverRate;
    private double mutationRate;
    private int populationSize;
    private List<Integer> listSensors;
    private ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets;
    private ArrayList<Pair<Integer, ArrayList<Double>>> listPercents;
    private int numTargets;

    public GeneticEvolution(double crossoverRate, double mutationRate, int populationSize, List<Integer> listSensors, ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets, ArrayList<Pair<Integer, ArrayList<Double>>> listPercents, int numTargets) {
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.populationSize = populationSize;
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

    public Individual crossover(Individual target, Individual mutated) {
        int k = new Random().nextInt(target.getLength());

        List<Integer> newGenes = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            newGenes.add(target.getSensor(i));
        }

        for (int i = 0; i < mutated.getLength(); i++) {
            int sensor = mutated.getSensor(i);
            if (!newGenes.contains(sensor)) {
                newGenes.add(sensor);
            }
        }

        Individual newIndividual = new Individual(newGenes, listTargets, listPercents, numTargets);

        return newIndividual;
    }

    public Individual mutate(Individual individual) {
        // Đột biến bằng cách tráo đổi 2 gen
        int pos1 = new Random().nextInt(individual.getLength());
        int pos2 = new Random().nextInt(individual.getLength());
    
        List<Integer> mutatedGenes = new ArrayList<>(individual.getIndividual());
        Collections.swap(mutatedGenes, pos1, pos2);
    
        return new Individual(mutatedGenes, listTargets, listPercents, numTargets);
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
