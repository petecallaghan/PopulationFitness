package uk.edu.populationfitness.models.population;

import java.util.ArrayList;

class Fitnesses {
    private double total_fitness;
    private long checked_fitness;
    private ArrayList<Double> fitnesses = new ArrayList<>();

    Fitnesses(){
        resetCounts();
    }

    void resetCounts(){
        total_fitness = 0.0;
        checked_fitness = 0;
        fitnesses = new ArrayList<>();
    }

    double averageFitness(){
        return checked_fitness > 0 ? total_fitness / checked_fitness : 0;
    }

    double standardDeviationFitness(){
        if (checked_fitness < 1) return 0.0;
        double mean = averageFitness();
        double variance = 0.0;
        for(double f: fitnesses){
            double difference = f-mean;
            variance += difference*difference;
        }
        return Math.sqrt(variance/checked_fitness);
    }

    void copy(Fitnesses source) {
        fitnesses.addAll(source.fitnesses);
    }

    public double add(double fitness){
        total_fitness += fitness;
        checked_fitness++;
        fitnesses.add(fitness);
        return fitness;
    }
}
