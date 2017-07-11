package uk.edu.populationfitness.models;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by pete.callaghan on 03/07/2017.
 */
public class Population {
    private Config config;

    public ArrayList<Individual> individuals;

    public double total_fitness = 0.0;

    public double max_fitness = 0.0;

    public Population(Config config){
        this.config = config;
        individuals = new ArrayList<>();
    }

    /**
     * Adds new individuals to the population. Adds the number configured as the population size.
     *
     * @param birth_year
     */
    public void addNewIndividuals(int birth_year){
        for(int i = 0; i < config.initial_population_size; i++){
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
        ArrayList<Individual> babies = new ArrayList<Individual>();

        for(int i = 0; i < individuals.size() - 1; i+=2){
            Individual father = individuals.get(i);
            Individual mother = individuals.get(i + 1);
            boolean pairCanBreed = RepeatableRandom.generateNext() < epoch.breedingProbability();
            if (pairCanBreed && father.canBreed(current_year) && mother.canBreed(current_year)){
                Individual baby = new Individual(config, current_year);
                baby.inheritFromParentsAndMutate(mother, father);
                babies.add(baby);
            }
        }
        individuals.addAll(babies);
        return babies;
    }

    private boolean isUnfit(double fitness, double kill_constant){
        return fitness < RepeatableRandom.generateNext() * kill_constant;
    }

    private boolean isUnfitForEnvironment(Individual individual, double fitness_factor, double environment_capacity, double kill_constant){
        individual.fitness = individual.genes.fitness(fitness_factor) * environment_capacity;
        return isUnfit(individual.fitness, kill_constant);
    }

    private boolean isUnfit(Individual individual, double fitness_factor, double kill_constant){
        individual.fitness = individual.genes.fitness(fitness_factor);
        return isUnfit(individual.fitness, kill_constant);
    }

    private int addSurvivors(Predicate<Individual> survivor){
        int population_size = individuals.size();
        individuals = new ArrayList<>(individuals.stream().filter(survivor).collect(Collectors.toList()));
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
        if (individuals.size() < 1)
            return 0;

        if (!epoch.isFitnessEnabled()){
            return addSurvivors(i -> !i.isReadyToDie(current_year));
        }

        if (epoch.isCapacityUnlimited()){
            return addSurvivors(i -> !(i.isReadyToDie(current_year) || isUnfit(i, epoch.fitnessFactor(), epoch.killConstant())));
        }

        double environment_capacity = (double)(epoch.environment_capacity) / individuals.size();
        return addSurvivors(i -> !(i.isReadyToDie(current_year) || isUnfitForEnvironment(i, epoch.fitnessFactor(), environment_capacity, epoch.killConstant())));
    }

    public void getStatistics(){
        total_fitness = 0;
        max_fitness = 0;
    }

    public double averageFitness(){
        return individuals.size() > 0 ? total_fitness / individuals.size() : 0;
    }
}

