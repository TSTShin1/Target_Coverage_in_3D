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

public class MPHSA{
    private int populationSize;
    private double hmcr;
    private double par;
    private List<Integer> listSensors;
    private ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets;
    private ArrayList<Pair<Integer, ArrayList<Double>>> listPercents;
    private int numTargets;

    public MPHSA(int populationSize, double hmcr, double par, List<Integer> listSensors, ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets, ArrayList<Pair<Integer, ArrayList<Double>>> listPercents, int numTargets) {
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

    public Individual Crossover(Population population, double par) {
        Individual child1 = new Individual(listTargets, listPercents, numTargets);
        Individual child2 = new Individual(listTargets, listPercents, numTargets);
        int index1 = (int) (Math.random()*10);
        int index2 = (int) (Math.random()*10);
        double random = Math.random();
        boolean used[] = new boolean[1000];
        if (random < hmcr) {
            Individual parent1 = population.getIndividual(index1);
            Individual parent2 = population.getIndividual(index2);
            double random1 = Math.random();
            if (random1 < par) {
                for (int i = 0; i < listSensors.size(); i++) {
                    double coin = Math.random();
                    if (coin >= 0.5) used[parent1.getSensor(i)] = true;
                    else used[parent1.getSensor(i)] = false;
                }
                int j = 0;
                for (int i = 0; i < listSensors.size(); i++) {
                    if (used[parent1.getSensor(i)] == false) child1.setSensor(i, parent1.getSensor(i));
                    else {
                        while (used[parent2.getSensor(j)] == false) {
                            j++;
                            if (used[parent2.getSensor(j)] == true) {
                                child1.setSensor(i, parent2.getSensor(j));
                                j++;
                                break;
                            }
                        }
                    }
                }
                for (int i = 0; i < listSensors.size(); i++) {
                    if (used[parent2.getSensor(i)] == false) child2.setSensor(i, parent2.getSensor(i));
                    else {
                        while (used[parent1.getSensor(j)] == false) {
                            j++;
                            if (used[parent1.getSensor(j)] == true) {
                                child1.setSensor(i, parent1.getSensor(j));
                                j++;
                                break;
                            }
                        }
                    }
                }
            }
            parent1.setIndividual(child1.getIndividual());
            parent2.setIndividual(child2.getIndividual());
            if (parent1.getFitness() > parent2.getFitness()) return parent1;
            else return parent2;
        }
        else {
            Individual newIndividual = new Individual(listTargets, listPercents, index2);
            return newIndividual;
        }
    }

    public Individual mutated(Individual individual) {
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
    private int findWorstIndex1(Population population) {
        int worstIndex = 0;
        int minFitness = population.getIndividual(0).getFitness();

        for (int i = 1; i < 10; i++) {
            int fitness = population.getIndividual(i).getFitness();
            if (fitness < minFitness) {
                minFitness = fitness;
                worstIndex = i;
            }
        }

        return worstIndex;
    }
    public Population replaceWorst1(Population population, Individual newIndividual) {
        int worstIndex = findWorstIndex1(population);
        int worstFitness = population.getIndividual(worstIndex).getFitness();
        int newFitness = newIndividual.getFitness();

        if (newFitness > worstFitness) {
            population.setIndividual(worstIndex, newIndividual);
        }

        return population;
    }
}
