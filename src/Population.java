
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

public class Population {
    private Individual[] population;
    private List<Integer> listSensors;
    private ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets;
    private ArrayList<Pair<Integer, ArrayList<Double>>> listPercents;
    private int numTargets;

    public Population(int populationSize, int numSensor, List<Integer> listSensors, ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets, ArrayList<Pair<Integer, ArrayList<Double>>> listPercents, int numTargets) {
        this.listSensors = listSensors;
        this.listTargets = listTargets;
        this.numTargets = numTargets;
        this.population = new Individual[populationSize];
        this.listPercents = listPercents;
        initializePopulation(numSensor);
    }

    private void initializePopulation(int numSensor) {
        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual(listTargets,listPercents,numTargets);
        }
    }

    public Individual[] getPopulation() {
        return population;
    }

    public void setPopulation(Individual[] population) {
        this.population = population;
    }

    public void setIndividual(int pos, Individual individual) {
        population[pos] = individual;
    }

    public Individual getIndividual(int pos) {
        return population[pos];
    }

    public ArrayList<Pair<Integer, ArrayList<Integer>>> getListTargets() {
        return listTargets;
    }

    public Individual getFittest(int pos) {
        Arrays.sort(this.population, Comparator.comparingInt(Individual::getFitness));
        return population[pos];
    }

    public void shufflePopulation() {
        List<Individual> populationList = Arrays.asList(population);
        Collections.shuffle(populationList);
        population = populationList.toArray(new Individual[0]);
    }

    public Individual getRandomIndividual() {
        Random random = new Random();
        int randomIndex = random.nextInt(population.length);
        return population[randomIndex];
    }
}
