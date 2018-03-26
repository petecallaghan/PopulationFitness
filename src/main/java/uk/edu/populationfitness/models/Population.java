package uk.edu.populationfitness.models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Population {
    public final Config config;

    public ArrayList<Individual> individuals;

    public int average_age = 0;

    private double total_fitness = 0.0;
    private double total_factored_fitness = 0.0;
    private int checked_fitness = 0;
    private ArrayList<Double> fitnesses;
    private double environment_capacity = 0.0;

    public Population(Config config){
        this.config = config;
        individuals = new ArrayList<>();
        fitnesses = new ArrayList<>();
    }

    public Population(Population source){
        this(source.config);
        individuals.addAll(source.individuals);
        fitnesses.addAll(source.fitnesses);
    }

    /**
     * Adds new individuals to the population. Adds the number configured as the population size.
     *
     * @param birth_year
     */
    public void addNewIndividuals(int birth_year){
        for(int i = 0; i < config.getInitialPopulation(); i++){
            Individual individual = new Individual(config, birth_year);
            individual.genes.buildFromRandom();
            individuals.add(individual);
        }
    }

    /**
     * Produces a new generation. Adds the generation to the population.
     *
     * @param current_year
     * @return The newly created set of babies
     */
    public List<Individual> addNewGeneration(Epoch epoch, int current_year){
        long totalAge = 0;
        ArrayList<Individual> babies = new ArrayList<>();

        for(int i = 0; i < individuals.size() - 1; i+=2){
            Individual father = individuals.get(i);
            Individual mother = individuals.get(i + 1);
            totalAge += father.age(current_year);
            totalAge += mother.age(current_year);
            boolean pairCanBreed = RepeatableRandom.generateNext() < epoch.breedingProbability();
            if (pairCanBreed && father.canBreed(current_year) && mother.canBreed(current_year)){
                Individual baby = new Individual(config, current_year);
                baby.inheritFromParents(mother, father);
                babies.add(baby);
            }
        }
        individuals.addAll(babies);
        average_age = individuals.size() > 0 ? (int)(totalAge / individuals.size()) : 0;
        return babies;
    }

    private boolean isUnfit(Individual individual, int current_year, double fitness_factor, double kill_constant){
        final double fitness = individual.genes.fitness();
        final double factored_fitness = fitness * fitness_factor;
        total_fitness += fitness;
        total_factored_fitness += factored_fitness;
        checked_fitness++;
        fitnesses.add(fitness);
        return individual.isReadyToDie(current_year) ? true : factored_fitness < (RepeatableRandom.generateNext() * kill_constant);
    }

    private int addSurvivors(Predicate<Individual> survivor){
        int population_size = individuals.size();
        individuals = individuals.stream().filter(survivor).collect(Collectors.toCollection(ArrayList::new));
        return population_size - individuals.size();
    }

    /**
     * Kills those in the population who are ready to die and returns the number of fatalities
     *
     * @param current_year
     * @param epoch
     * @return
     */
    public int killThoseUnfitOrReadyToDie(int current_year, Epoch epoch){
        total_fitness = 0.0;
        total_factored_fitness = 0.0;
        checked_fitness = 0;
        environment_capacity = 0.0;
        fitnesses = new ArrayList<>();

        if (individuals.size() < 1)
            return 0;

        if (epoch.isCapacityUnlimited()){
            return addSurvivors(i -> !isUnfit(i, current_year, epoch.fitness(), epoch.kill()));
        }

        environment_capacity = (double)(epoch.capacityForYear(current_year)) / individuals.size();
        epoch.addCapacityFactor(environment_capacity);
        return addSurvivors(i -> !isUnfit(i, current_year, epoch.fitness() * environment_capacity, epoch.kill()));
    }

    public double averageFitness(){
        return checked_fitness > 0 ? total_fitness / checked_fitness : 0;
    }

    public double averageFactoredFitness(){
        return checked_fitness > 0 ? total_factored_fitness / checked_fitness : 0;
    }

    public double capacityFactor() {
        return environment_capacity;
    }

    public double standardDeviationFitness(){
        if (checked_fitness < 1) return 0.0;
        double mean = averageFitness();
        double variance = 0.0;
        for(double f: fitnesses){
            double difference = f-mean;
            variance += difference*difference;
        }
        return Math.sqrt(variance/checked_fitness);
    }
}

