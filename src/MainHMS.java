import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
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

public class MainHMS {
    public static void main(String[] args) {
        try {
            // Đọc dữ liệu từ file sensor.input
            List<Sensor> sensors = readSensors("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\data\\sensor.input");

            // Đọc dữ liệu từ file target.input
            List<Target> targets = readTargets("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\data\\sentarget.input");

            // Các tham số đầu vào
            double f = 1.0;
            double a = 0.11*(f*f)/(1 + f*f) + 44*f*f/(4100 + f) + 2.75/10000*f*f+0.003;
            double P_f = 0.001;
            double P_r = 0.001;
            double P_d = 0.0001;
            // double alpha = Math.pow(10, a/5);
            ArrayList<Pair<Integer, ArrayList<Integer>>> listTargets = new ArrayList<>(); // Tập các target mà từng sensor phát hiện được
            ArrayList<Pair<Integer, ArrayList<Double>>> listPercents = new ArrayList<>(); // Xác suất phát hiện các target của từng sensor
            ArrayList<Double> listPowers = new ArrayList<>(); // Năng lượng hiện tại của từng sensor
            ArrayList<Double> listPowersConsumption = new ArrayList<>();
            int sen = 0;
            for (Sensor sensor : sensors) {
                ArrayList<Integer> coveredTargets = new ArrayList<>();
                ArrayList<Double> coveredPercents = new ArrayList<>();
                for (int i = 0; i < targets.size(); i++) {
                    Target target = targets.get(i);
                    double distance =  calculateDistance(sensor, target);
                    if (distance <= sensor.getRadius()) {
                        coveredTargets.add(i);
                    }
                    if (distance <= 100) {
                        coveredPercents.add(1.0);
                    }
                    else if (distance <= sensor.getRadius()) {
                        coveredPercents.add(1/((Math.pow(distance/20, 1.5))*Math.pow(10, 0.2*a*(distance-20))));
                    }
                    else coveredPercents.add(0.0);
                }
                Pair<Integer, ArrayList<Integer>> pairTarget = new Pair<>(sen, coveredTargets);
                Pair<Integer, ArrayList<Double>> pairPercents = new Pair<>(sen, coveredPercents);
                listTargets.add(pairTarget);
                listPercents.add(pairPercents);
                sen++;
            }

            List<Integer> listSensors = new ArrayList<>();
            for (int i = 0; i < sensors.size(); i++) {
                listSensors.add(i);
            }
            List<Double> listPercent = new ArrayList<>();
            for (int i = 0; i < targets.size(); i++) {
                listPercent.add(0.0);
            }

            // Tính năng lượng tiêu thụ của từng sensor
            for (int i = 0; i < sensors.size(); i++) {
                listPowersConsumption.add(0.01*Math.pow(sensors.get(i).getRadius(), 2));
            }

            for (int i = 0; i < sensors.size(); i++) {
                listPowers.add(10000.0);
            }

            // Thực hiện Harmony Search Algorithm
            int time = 0;
            int nTime = 0;
            int populationSize = 200;
            double hmcr = 0.9; // Điều chỉnh giá trị hmcr theo nhu cầu
            double par = 0.2; // Điều chỉnh giá trị par theo nhu cầu
            int generationSize = 10000;
            int numTargets = targets.size();  // Số lượng target
            Individual newIndividual = new Individual(listTargets, listPercents, numTargets);
            System.out.println(newIndividual.getFitness());
            while (newIndividual.getFitness() >= 20) {
                nTime++;
                HarmonySearch hms = new HarmonySearch(populationSize, hmcr, par, listSensors, listTargets, listPercents, numTargets);
                Population population = hms.initPopulation();

                writeStartGen("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\result\\genHMS.out");

                File benchmarkFile = new File("C:\\Users\\Admin\\Desktop\\vscode_java\\Out\\resultHMS.csv");
                BufferedWriter bench = new BufferedWriter(new FileWriter(benchmarkFile));
                bench.write("Generation,Best,Time,Total_Time\n");

                double totalTime = 0.0;
                int best[] = new int[100000];
                int generation = 0;

                while (generation >= 0) {
                    System.out.println(generation);
                    Long genBegin = Calendar.getInstance().getTimeInMillis();
                    for (int i = 0; i < 20; i++) {
                    if (Math.random() >hmcr) {
                        Individual newHarmony = population.getRandomIndividual();
                        newHarmony = hms.mutated(newHarmony);
                        population = hms.replaceWorst(population, newHarmony);
                    }
                    else{

                        Individual newHarmony = hms.Crossover(population);
                        newHarmony = hms.mutate(newHarmony);
                        //System.out.println("pass");
                        population = hms.replaceWorst(population, newHarmony);
                    }
                }
                    Long genEnd = Calendar.getInstance().getTimeInMillis();
                        bench.write(String.format("%d,", generation));
                        double genTime = (double)(genEnd - genBegin)/1000;
                        // Lấy cá thể có độ thích nghi tốt nhất trong thế hệ hiện tại
                        Individual bestIndividual = hms.getFittest(population);
                        bench.write(String.format("%d,", bestIndividual.getFitness()));
                        bench.write(String.format("%.3f,", genTime));
                        totalTime += genTime;
                        bench.write(String.format("%.3f\n", totalTime));

                    writeGeneration("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\result\\genHMS.out", generation, bestIndividual.getFitness());
                    best[generation] = bestIndividual.getFitness();
                    if (generation >= 300) {
                        if (best[generation] == best[generation-100]) break;
                    }
                    generation++;
                }

                // Lưu kết quả vào file resultHMS.out
                Individual maxFitness = hms.getFittest(population);
                writeResult("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\result\\resultHMS.out", maxFitness);

                bench.close();
                int k = 0;
                // Set<Integer> setTarget = new HashSet<>();
                List<Double> listPercents1 = new ArrayList<>();
                Set<Integer> setSensor = new HashSet<>();
                ArrayList<Pair<Double, Integer>> listPCC = new ArrayList<>();
                ArrayList<ArrayList<Integer>> listCover = new ArrayList<>();
                int dem = 0;
                for (int i = 0 ; i < numTargets; i++) {
                    listPercents1.add(0.0);
                }
                // bug in so luong cover
                int cnt1 = 0;
                for (int i = 0; i < listTargets.size(); i++) {
                    int cnt2 = maxFitness.getSensor(i);
                    for (int j = 0; j < listPercents.size(); j++) {
                        if (cnt2 == listPercents.get(j).first) cnt2 = j; 
                    }
                    for (int j = 0; j < numTargets; j++) {
                        listPercents1.set(j, 1 - (1 - listPercents1.get(j))*(1 - listPercents.get(cnt2).second.get(j)));
                    }
                    setSensor.add(maxFitness.getSensor(i));
                    for (int j = 0; j < numTargets; j++) {
                        if (listPercents1.get(j) >= 0.8) {
                            cnt1++;
                            if (cnt1 == numTargets) {
                                cnt1 = 0;
                                double sum = 0.0;
                                for (int l = dem; l <= i; l++) {
                                    sum += listPowersConsumption.get(maxFitness.getSensor(l));
                                }
                                ArrayList<Integer> cover = new ArrayList<>(setSensor);
                                listCover.add(cover);
                                dem = i + 1;
                                listPCC.add(new Pair(sum, k));
                                k++;
                                // writeCover("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\result\\coverGA.out", k, setSensor);
                                // setTarget.clear();
                                setSensor.clear();
                                for (int t = 0; t < listPercents1.size(); t++) {
                                    listPercents1.set(t, 0.0);
                                }
                            }
                        }
                        else {
                            cnt1 = 0;
                            break;
                        }
                    }
                }
                System.out.println(k);
                Collections.sort(listPCC, new Comparator<Pair<Double, Integer>>() {
                @Override
                public int compare(Pair<Double, Integer> o1, Pair<Double, Integer> o2) {
                    return Double.compare(o1.first, o2.first);
                }
                });
                for (int i = 0; i < listCover.size(); i++) {
                    System.out.println(listCover.get(i));
                }
                int curCover = 0;
                int cnt = listPCC.get(curCover).second;
                int cover = 0;
                int status[] = new int[1000];
                for (int i = 0; i < maxFitness.getLength(); i++) {
                    status[maxFitness.getSensor(i)] = 0;
                }
                // status = 0 <--> sensor co the hoat dong binh thuong --> xet P_d va P_f
                // status = 1 <--> sensor dang gap truc trac --> xet P_r va P_d
                // status = 2 <--> sensor da chet --> Khong xet gi het, loai sensor ra khoi list
                while (cover == 0) {
                    time++;
                    for (int i = 0; i < listCover.get(cnt).size(); i++) {
                        listPowers.set(listCover.get(cnt).get(i), listPowers.get(listCover.get(cnt).get(i)) - listPowersConsumption.get(listCover.get(cnt).get(i)));
                        if (listPowers.get(listCover.get(cnt).get(i)) < 0) {
                            cover++;
                            status[listCover.get(cnt).get(i)] = 2;
                        }
                    }
                    for (int i = 0 ; i < maxFitness.getLength(); i++) {
                        if (status[maxFitness.getSensor(i)] == 0) {
                            double Pf = Math.random();
                            double Pd = Math.random();
                            if (Pf < P_f) status[maxFitness.getSensor(i)] = 1;
                            if (Pd < P_d) status[maxFitness.getSensor(i)] = 2;
                        }
                        else if (status[maxFitness.getSensor(i)] == 1) {
                            double Pr = Math.random();
                            double Pd = Math.random();
                            if (Pr < P_r) status[maxFitness.getSensor(i)] = 0;
                            if (Pd < P_d) status[maxFitness.getSensor(i)] = 2;
                        }
                    }
                    for (int i = 0; i < listCover.get(cnt).size(); i++) {
                        if (status[listCover.get(cnt).get(i)] != 0) cover++;
                        while (cover > 0) {
                            curCover++;
                            if (curCover == listPCC.size()) {
                                cover++;
                                break;
                            }
                            cnt = listPCC.get(curCover).second;
                            cover = 0;
                            for (int j = 0; j < listCover.get(cnt).size(); j++) {
                                if (status[listCover.get(cnt).get(j)] != 0) cover++;
                            }
                        }
                        if (curCover == listPCC.size()) break;
                    }
                }
                Iterator<Integer> iterator = listSensors.iterator();
                while (iterator.hasNext()) {
                    int num = iterator.next();
                    if (status[num] == 2) iterator.remove();
                }
                Iterator<Pair<Integer, ArrayList<Integer>>> iterator2 = listTargets.iterator();
                while (iterator2.hasNext()) {
                    Pair<Integer, ArrayList<Integer>> num = iterator2.next();
                    if (status[num.first] == 2) iterator2.remove();
                }
                Iterator<Pair<Integer, ArrayList<Double>>> iterator3 = listPercents.iterator();
                while (iterator3.hasNext()) {
                    Pair<Integer, ArrayList<Double>> num = iterator3.next();
                    if (status[num.first] == 2) iterator3.remove();
                }
                System.out.println(listSensors.size() + " " + listTargets.size() + " " + listPercents.size());
                newIndividual = new Individual(listTargets, listPercents, numTargets);
                System.out.println(time);
                writeGeneration("C:\\Users\\Admin\\Desktop\\vscode_java\\DE_Sensor-main\\result\\timeHMS.out", nTime, time);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private static void displayListTargets(ArrayList<ArrayList<Integer>> listTargets) {
        for (int i = 0; i < listTargets.size(); i++) {
            System.out.print("Sensor " + i + " covers targets: ");
            ArrayList<Integer> coveredTargets = listTargets.get(i);
            for (int j = 0; j < coveredTargets.size(); j++) {
                System.out.print(coveredTargets.get(j) + " ");
            }
            System.out.println();
        }
    }

    private static List<Sensor> readSensors(String fileName) throws IOException {
        List<Sensor> sensors = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(" ");
            double x = Double.parseDouble(values[0]);
            double y = Double.parseDouble(values[1]);
            double z = Double.parseDouble(values[2]);
            double radius = Double.parseDouble(values[3]);
            sensors.add(new Sensor(x, y,z, radius));
        }
        br.close();
        return sensors;
    }

    private static List<Target> readTargets(String fileName) throws IOException {
        List<Target> targets = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        String line;
        while ((line = br.readLine()) != null) {
            String[] values = line.split(" ");
            double x = Double.parseDouble(values[0]);
            double y = Double.parseDouble(values[1]);
            double z = Double.parseDouble(values[2]);
            targets.add(new Target(x, y,z));
        }
        br.close();
        return targets;
    }

    private static double calculateDistance(Sensor sensor, Target target) {
        double x1 = sensor.getX();
        double y1 = sensor.getY();
        double z1 = sensor.getZ();
        double x2 = target.getX();
        double y2 = target.getY();
        double z2 = target.getZ();
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)+ (z1-z2) * (z1 - z2));
    }

    private static void writeResult(String fileName, Individual maxFitness) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        List<Integer> displayResult = maxFitness.display();
        
        writer.write(String.join(" ", displayResult.stream().map(Object::toString).toArray(String[]::new)));
        writer.close();
    }
    private static void writeGeneration(String fileName, int index, int maxFitness) throws IOException {
        FileWriter writer = new FileWriter(fileName, true);
        writer.write("Best fit for generation " + index + ": " + maxFitness + "\n");
        writer.close();
    }  
    private static void writeStartGen(String fileName) throws IOException {
        FileWriter writer = new FileWriter(fileName);
        writer.write("Maximum fit for each generation: " + "\n");
        writer.close();
    }
    
   

}
