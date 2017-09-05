package uk.edu.populationfitness.models;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class Epoch {

    private static final int UNDEFINED_YEAR = -1;

    // Indicates unlimited capacity
    public static final int UNLIMITED_CAPACITY = 0;

    private final Config config;

    public final int start_year;

    public int end_year = UNDEFINED_YEAR;

    // Defines the kill constant for this epoch
    private double kill_constant = 1.0;

    // Defines the fitness adjustment for this epoch
    private double fitness_factor = 1.0;

    // Defines the holding capacity of the environment for this epoch
    public int environment_capacity = UNLIMITED_CAPACITY;

    // Use this to turn off fitness. By default fitness is enabled
    public boolean enable_fitness = true;

    // Max population actually expected for this epoch
    public int expected_max_population = 0;

    // Probability of a pair breeding in a given year
    private double probability_of_breeding;

    private boolean isDisease = false;

    public Epoch(Config config, int start_year){
        this.start_year = start_year;
        this.config = config;
        this.probability_of_breeding = config.probability_of_breeding;
    }

    public boolean isCapacityUnlimited(){
        return environment_capacity == UNLIMITED_CAPACITY;
    }

    public boolean isFitnessEnabled(){
        return enable_fitness;
    }

    public Epoch breedingProbability(double probability){
        this.probability_of_breeding = probability;
        return this;
    }

    public double breedingProbability(){
        return probability_of_breeding;
    }

    public Epoch disease(boolean isDisease){
        this.isDisease = isDisease;
        return this;
    }

    public boolean disease(){
        return isDisease;
    }

    public Epoch kill(double kill_constant){
        this.kill_constant = kill_constant;
        return this;
    }

    public double kill(){
        return this.kill_constant;
    }

    public Epoch fitness(double fitness_factor){
        this.fitness_factor = fitness_factor;
        /*System.out.print("Epoch ");
        System.out.print(start_year);
        System.out.print(" f=");
        System.out.println(fitness_factor);*/
        return this;
    }

    public double fitness(){
        return this.fitness_factor;
    }

    public Epoch capacity(int environment_capacity){
        this.environment_capacity = environment_capacity;
        return this;
    }

    public Epoch disableFitness(){
        this.enable_fitness = false;
        return this;
    }

    public Epoch max(int expected_max_population){
        this.expected_max_population = expected_max_population;
        return this;
    }
}
