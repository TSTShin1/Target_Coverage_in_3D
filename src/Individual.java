
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Pair<A, B> {
    public A first;
    public B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }
}

public class Individual {
    private List<Integer> individual;
    private ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets;
    private ArrayList<Pair<Integer, ArrayList<Double>>> listPercents;  // Liệt kê các target đã phát hiện
    private int numTarget;
    //private int numOfSensor;
    
    // Khởi tạo một cách đảo lộn theo số sensor truyền vào 
    public Individual(ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets, ArrayList<Pair<Integer, ArrayList<Double>>> listPercents, int numTarget ) {
        this.individual = new ArrayList<Integer>();
        this.listTargets = listTargets;
        this.numTarget = numTarget;
        this.listPercents = listPercents;
        for (int i = 0; i < listTargets.size(); i++) {
            individual.add(listTargets.get(i).first);
        }
        Collections.shuffle(individual);
        updateListTargets(); 
        updateListPercents();
    }

    public Individual(List<Integer> individual, ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets, ArrayList<Pair<Integer, ArrayList<Double>>> listPercents, int numTarget) {
        this.listTargets = listTargets;
        this.numTarget = numTarget;
        this.individual = individual;
        this.listPercents = listPercents;
        updateListTargets(); 
        updateListPercents();
    }
    public List<Integer> getIndividual() {
        return individual;
    }
    public void setIndividual(List<Integer> individual) {
        this.individual = individual;
    }
    public int getSensor(int pos) {
        return individual.get(pos);
    }
    public void setSensor(int pos, int s) {
        individual.set(pos, s);
    }
    public void deleteSensor(int pos) {
        individual.remove(pos);
    }
    public int getLength() {
        return individual.size();
    }
    public int getNumtarget(){
        return numTarget;
    }

    private void updateListTargets() {
        ArrayList<Pair<Integer, ArrayList<Integer>>> newListTargets = new ArrayList<Pair<Integer, ArrayList<Integer>>>();
        for (int i = 0; i < individual.size(); i++) {
            for (int j = 0; j < individual.size(); j++) {
                if (individual.get(i) == listTargets.get(j).first) {
                    Pair<Integer, ArrayList<Integer>> pair = new Pair<>(individual.get(i), listTargets.get(j).second);
                    newListTargets.add(pair);
                    break;
                }
            }
        }
        this.listTargets = newListTargets;
    }
    private void updateListPercents() {
        ArrayList<Pair<Integer, ArrayList<Double>>> newListPercents = new ArrayList<Pair<Integer, ArrayList<Double>>>();
        for (int i = 0; i < individual.size(); i++) {
            for (int j = 0; j < individual.size(); j++) {
                if ((individual.get(i) - listPercents.get(j).first) == 0) {
                    Pair<Integer, ArrayList<Double>> pair = new Pair<>(individual.get(i), listPercents.get(j).second);
                    newListPercents.add(pair);
                    break;
                }
            }
        }
        this.listPercents = newListPercents;
    }
    public ArrayList<Pair<Integer, ArrayList<Integer>>> getListTarget(){
        return listTargets;
    }
     
    public int getFitness() {
        Set<Integer> setTarget = new HashSet<>();
        int k = 0;
        int dem = 0;
        List<Double> listPercent = new ArrayList<>();
        for (int i = 0; i < numTarget; i++) {
            listPercent.add(0.0);
        }
        for (int i = 0; i < individual.size(); i++) {
            for (int j = 0; j < numTarget; j++) {
                // setTarget.add(listTargets.get(i).get(j));
                listPercent.set(j, 1 - (1 - listPercent.get(j))*(1 - listPercents.get(i).second.get(j)));
            }
            for (int j = 0; j < numTarget; j++) {
                if (listPercent.get(j) >= 0.8) {
                    dem++;
                    if (dem == numTarget) {
                        dem = 0;
                        k++;
                        for (int t = 0; t < listPercent.size(); t++) {
                            listPercent.set(t, 0.0);
                        }
                    }
                }
                else {
                    dem = 0;
                    break;
                }
            }
        }
        return (k * numTarget + setTarget.size());
    }
    
    public int getCovers() {
        Set<Integer> setTarget = new HashSet<>();
        int k = 0;
        for (Pair<Integer, ArrayList<Integer>> sensorTargets : listTargets) {
            setTarget.addAll(sensorTargets.second);
            if (setTarget.size() == numTarget) {
                k++;
                setTarget.clear();
            }
        }
        return k;
    }

    public ArrayList<ArrayList<Integer>> getnumCovers() {
        Set<Integer> setTarget = new HashSet<>();
        ArrayList<ArrayList<Integer>> newCovers = new ArrayList<>();
        int k = 0;
    
        for (int i = 0; i < listTargets.size(); i++) {
            for (int j = 0; j < listTargets.get(i).second.size(); j++) {
                setTarget.add(listTargets.get(i).second.get(j));
            }
    
            if (setTarget.size() == numTarget) {
                ArrayList<Integer> covers = new ArrayList<>();
                for (int j = k; j <= i; j++) {
                    covers.add(individual.get(j));
                }
                k = i + 1;
                newCovers.add(covers);
                setTarget.clear();
            }
        }
    
        return newCovers;
    }
    public List<Integer> display() {
        return individual;
    }

    public ArrayList<ArrayList<Integer>> getCoverFitness() {
        Set<Integer> setTarget = new HashSet<>();
        int k = 0;
        int dem = 0;
        List<Double> listPercent = new ArrayList<>();
        ArrayList<ArrayList<Integer>> listCover = new ArrayList<>();
        for (int i = 0; i < numTarget; i++) {
            listPercent.add(0.0);
        }
        for (int i = 0; i < individual.size(); i++) {
            for (int j = 0; j < numTarget; j++) {
                // setTarget.add(listTargets.get(i).get(j));
                listPercent.set(j, 1 - (1 - listPercent.get(j))*(1 - listPercents.get(i).second.get(j)));
            }
            setTarget.add(individual.get(i));
            for (int j = 0; j < numTarget; j++) {
                if (listPercent.get(j) >= 0.8) {
                    dem++;
                    if (dem == numTarget) {
                        ArrayList<Integer> cover = new ArrayList<>(setTarget);
                        listCover.add(cover);
                        dem = 0;
                        k++;
                        for (int t = 0; t < listPercent.size(); t++) {
                            listPercent.set(t, 0.0);
                        }
                    }
                }
                else {
                    dem = 0;
                    break;
                }
            }
        }
        return listCover;
    }
}
