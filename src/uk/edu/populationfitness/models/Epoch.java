package uk.edu.populationfitness.models;

/**
 * Created by pete.callaghan on 04/07/2017.
 */
public class Epoch {

    private static final int UNDEFINED_YEAR = -1;

    // Indicates unlimited capacity
    public static final int UNLIMITED_CAPACITY = 0;

    // The default kill constant
    public static final double DEFAULT_KILL_CONSTANT = 1.0255;

    public int start_year;
    public int end_year = UNDEFINED_YEAR;

    // Defines the kill constant for this epoch
    public double kill_constant = DEFAULT_KILL_CONSTANT;

    // Defines the fitness adjustment for this epoch
    public double fitness_factor = 1.0;

    // Defines the holding capacity of the environment for this epoch
    public int environment_capacity = UNLIMITED_CAPACITY;

    // Use this to turn off fitness. By default fitness is enabled
    public boolean enable_fitness = true;

    // Max population actually expected for this epoch
    public int expected_max_population = 0;

    public Epoch(int start_year){
        this.start_year = start_year;
    }

    public boolean isCapacityUnlimited(){
        return environment_capacity == UNLIMITED_CAPACITY;
    }

    public boolean isFitnessEnabled(){
        return enable_fitness;
    }

    public Epoch killConstant(double kill_constant){
        this.kill_constant = kill_constant;
        return this;
    }

    public Epoch fitnessFactor(double fitness_factor){
        this.fitness_factor = fitness_factor;
        return this;
    }

    public Epoch environmentCapacity(int environment_capacity){
        this.environment_capacity = environment_capacity;
        return this;
    }

    public Epoch disableFitness(){
        this.enable_fitness = false;
        return this;
    }

    public Epoch maxPopulation(int expected_max_population){
        this.expected_max_population = expected_max_population;
        return this;
    }
}
