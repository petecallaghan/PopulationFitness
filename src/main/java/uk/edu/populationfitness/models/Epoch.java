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

    // Defines the minimum fitness for this epoch
    private double min_fitness = 1.0;

    // Defines the holding capacity of the environment for this epoch
    public int environment_capacity = UNLIMITED_CAPACITY;

    public int prev_environment_capacity;

    // Max population actually expected for this epoch
    public int expected_max_population = 0;

    // Probability of a pair breeding in a given year
    private double probability_of_breeding;

    private boolean isDisease = false;

    private int max_age;

    private int max_breeding_age;

    private boolean isModern = false;

    public Epoch(Config config, int start_year){
        this.start_year = start_year;
        this.config = config;
        this.probability_of_breeding = config.getProbabilityOfBreeding();
        this.max_breeding_age = config.getMaxBreedingAge();
        this.max_age = config.getMaxAge();
    }

    public Epoch(Epoch source){
        this.expected_max_population = source.expected_max_population;
        this.start_year = source.start_year;
        this.end_year = source.end_year;
        this.environment_capacity = source.environment_capacity;
        this.config = source.config;
        this.min_fitness = source.min_fitness;
        this.isDisease = source.isDisease;
        this.probability_of_breeding = source.probability_of_breeding;
        this.prev_environment_capacity = source.prev_environment_capacity;
        this.max_age = source.max_age;
        this.max_breeding_age = source.max_breeding_age;
        this.isModern = source.isModern;
    }

    public boolean isCapacityUnlimited(){
        return environment_capacity == UNLIMITED_CAPACITY;
    }

    public int capacity()
    {
        return isCapacityUnlimited() ? expected_max_population : environment_capacity;
    }

    public int capacityForYear(int year){
        final int capacityRange = capacity() - prev_environment_capacity;
        final int yearRange = end_year - start_year;
        if (yearRange < 1) {
            return capacity();
        }
        return prev_environment_capacity + ((capacityRange * (year - start_year)) / yearRange);
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

    public Epoch modern(boolean isModern){
        this.isModern = isModern;
        return this;
    }

    public boolean modern(){
        return isModern;
    }

    public boolean historic(){
        return !isModern && !isDisease;
    }

    public boolean historicDisease() {
        return isDisease && !isModern;
    }

    public boolean modernDisease(){
        return isDisease && isModern;
    }

    public Epoch fitness(double fitness_factor){
        this.min_fitness = fitness_factor;
        return this;
    }

    public double fitness(){
        return this.min_fitness;
    }

    public Epoch capacity(int environment_capacity){
        this.environment_capacity = environment_capacity;
        this.expected_max_population = environment_capacity;
        return this;
    }

    public Epoch max(int expected_max_population){
        this.expected_max_population = expected_max_population;
        return this;
    }

    public int maxAge() {
        return max_age;
    }

    public Epoch maxAge(int max_age) {
        this.max_age = max_age;
        return this;
    }

    public int maxBreedingAge() {
        return max_breeding_age;
    }

    public Epoch maxBreedingAge(int max_breeding_age) {
        this.max_breeding_age = max_breeding_age;
        return this;
    }


    /**
     * Reduces the populations by the ratio
     *
     * P' = P/ratio
     *
     * @param ratio
     */
    Epoch reducePopulation(int ratio){
        this.expected_max_population = this.expected_max_population / ratio;
        this.environment_capacity = this.environment_capacity / ratio;
        this.prev_environment_capacity = this.prev_environment_capacity / ratio;
        return this;
    }

    /**
     * Increases the populations by the ratio
     *
     * P' = P * ratio
     *
     * @param ratio
     */
    Epoch increasePopulation(int ratio){
        this.expected_max_population = this.expected_max_population * ratio;
        this.environment_capacity = this.environment_capacity * ratio;
        this.prev_environment_capacity = this.prev_environment_capacity * ratio;
        return this;
    }

    public Config config(){
        return this.config;
    }
}
